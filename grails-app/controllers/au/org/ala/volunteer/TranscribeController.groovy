package au.org.ala.volunteer

import org.apache.commons.lang.StringUtils

class TranscribeController {

    def grailsApplication
    def fieldSyncService
    def auditService
    def taskService
    def userService
    def logService

    static allowedMethods = [saveTranscription: "POST"]

    def index = {
        if (params.id) {
            log.debug("index redirect to showNextFromProject: " + params.id)
            redirect(action: "showNextFromProject", id: params.id)
        } else {
            flash.message = "Something unexpected happened. Try pressing the back button to return to the previous task and trying again."
            redirect(uri:"/")
        }

    }

    def task = {

        def taskInstance = Task.get(params.int('id'))
        def currentUserId = userService.currentUserId
        userService.registerCurrentUser()

        if (taskInstance) {

            boolean isLockedByOtherUser = auditService.isTaskLockedForUser(taskInstance, currentUserId)

            def isAdmin = userService.isAdmin()
            if (isLockedByOtherUser && !isAdmin) {
                def lastView = auditService.getLastViewForTask(taskInstance)
                // task is already being viewed by another user (with timeout period)
                log.debug("Task ${taskInstance.id} is currently locked by ${lastView.userId}. Another task will be allocated")
                flash.message  = "The requested task (id: " + taskInstance.id + ") is being viewed/edited by another user. You have been allocated a new task"
                // redirect to another task
                redirect(action: "showNextFromProject", id: taskInstance.project.id, params: [prevId: taskInstance.id, prevUserId: lastView?.userId])
                return
            } else {
                if (isLockedByOtherUser) {
                    flash.message = "This task is currently locked by another user. Because you are an admin you are able to work on this task, but only do so if you are confident that no-one else is working on this task as well, as data will be lost if two people save the same task!"
                }
                // go ahead with this task
                auditService.auditTaskViewing(taskInstance, currentUserId)
            }

            def project = Project.findById(taskInstance.project.id)
            def isReadonly = false

            def isValidator = userService.isValidator(project)
            logService.log(currentUserId + " has role: ADMIN = " + userService.isAdmin() + " &&  VALIDATOR = " + isValidator)
            if (taskInstance.fullyTranscribedBy && taskInstance.fullyTranscribedBy != currentUserId && !userService.isAdmin()) {
                isReadonly = "readonly"
            }

            //retrieve the existing values
            Map recordValues = fieldSyncService.retrieveFieldsForTask(taskInstance)
            def adjacentTasks = taskService.getAdjacentTasksBySequence(taskInstance)
            render(view: 'task', model: [taskInstance: taskInstance, recordValues: recordValues, isReadonly: isReadonly, template: project.template, nextTask: adjacentTasks.next, prevTask: adjacentTasks.prev, sequenceNumber: adjacentTasks.sequenceNumber])
        } else {
            redirect(view: 'list', controller: "task")
        }
    }

    def showNextAction = {
        log.debug("rendering view: nextAction")
        def taskInstance = Task.get(params.id)
        render(view: 'nextAction', model: [id: params.id, taskInstance: taskInstance, userId: userService.currentUserId])
    }

    /**
     * Save the values of selected fields to their picklists, if the value does not already exist...
     */
    def updatePicklists(Task task) {

        // Add the name of the picklist here if it is to be updated with user entered values
        def updateablePicklists = ['recordedBy']

        // Find the template fields used by this tasks template
        def templateFields = TemplateField.findAllByTemplate(task.project.template)

        // Isolate the fields whose names coincide with a picklist, and for which this task has a value
        for (TemplateField tf : templateFields) {
            def f = task.fields.find { it.name == tf.fieldType.name() }
            // The fieldname/picklist name must also be in the list of updateable picklists
            if (f && updateablePicklists.contains(f.name) && StringUtils.isNotEmpty(f.value)) {
                log.debug("Checking picklist ${f.name} for value ${f.value}")
                // Check that the picklist actually exists...
                def picklist = Picklist.findByName(f.name)
                if (picklist) {
                    // And see if it already contains this value
                    def existing = PicklistItem.findByPicklistAndValue(picklist, f.value)
                    if (existing) {
                        log.debug("Will not update picklist: value ${f.value} already exisits in picklist ${picklist.name}.")
                    } else {
                        // Add the new value to the picklist
                        log.debug("Adding new picklist item to picklist '${picklist.name} with value '${f.value}")
                        def newItem = new PicklistItem(picklist: picklist, value: f.value)
                        newItem.save(flush: true)
                    }
                }
            }
        }

    }

    /**
     * Sync fields.
     * done in the form.
     */
    def save = {
        def currentUser = userService.currentUserId

        if (!params.id && params.failoverTaskId) {
            redirect(action:'task', id: params.failoverTaskId)
            return
        }

        if (currentUser != null) {
            def taskInstance = Task.get(params.id)
            def project = Project.findById(taskInstance.project.id)
            def template = Template.findById(project.template.id)
            WebUtils.cleanRecordValues(params.recordValues)
            fieldSyncService.syncFields(taskInstance, params.recordValues, currentUser, true, false, null)
            if (!taskInstance.hasErrors()) {
                updatePicklists(taskInstance)
                redirect(action: 'showNextAction', id: params.id)
            }
            else {
                def msg = "Task save failed: " + taskInstance.hasErrors()
                log.error(msg)
                flash.message = msg
                render(view: template.viewName, model: [taskInstance: taskInstance, recordValues: params.recordValues])
            }
        } else {
            redirect(view: '../index')
        }
    }

    /**
     * Sync fields.
     */
    def savePartial() {
        def currentUser = userService.currentUserId
        if (currentUser) {

            if (!params.id && params.failoverTaskId) {
                redirect(action:'task', id: params.failoverTaskId)
                return
            }

            def taskInstance = Task.get(params.id)
            WebUtils.cleanRecordValues(params.recordValues) // removes strange characters from UTF-8 pages
            fieldSyncService.syncFields(taskInstance, params.recordValues, currentUser, false, false, null)
            if (!taskInstance.hasErrors()) {
                updatePicklists(taskInstance)
                redirect(action: 'showNextAction', id: params.id)
            }
            else {
                def msg = "Task save partial failed: " + taskInstance.hasErrors()
                log.error(msg)
                flash.message = msg
                render(view: template.viewName, model: [taskInstance: taskInstance, recordValues: params.recordValues])
            }
        } else {
            redirect(view: '/index')
        }
    }

    /**
     * Show the next task for the supplied project.
     */
    def showNextFromProject = {
        def currentUser = userService.currentUserId
        def project = Project.get(params.id)

        if (project == null) {
            log.error("Project not found for id: " + params.id)
            redirect(view: '/index')
        }

        log.debug("project id = " + params.id + " || msg = " + params.msg + " || prevInt = " + params.prevId)

        if (params.msg) {
            flash.message = params.msg
        }
        def previousId = params.prevId?:-1
        def prevUserId = params.prevUserId?:-1
        def taskInstance = taskService.getNextTask(currentUser, project)
        //retrieve the details of the template
        if (taskInstance && taskInstance.id == previousId.toInteger() && currentUser != prevUserId) {
            log.debug "1."
            render(view: 'noTasks')
        } else if (taskInstance) {
            log.debug "2."
            redirect(action: 'task', id: taskInstance.id)
        } else {
            log.debug "4."
            render(view: 'noTasks')
        }
    }

    def geolocationToolFragment() {
    }

    def imageViewerFragment() {
        def multimedia = Multimedia.get(params.int("multimediaId"))
        def height = params.height?.toInteger() ?: 400
        def rotate = params.int("rotate") ?: 0
        def hideControls = params.boolean("hideControls") ?: false
        def hideShowInOtherWindow = params.boolean("hideShowInOtherWindow") ?: false
        def hidePinImage = params.boolean("hidePinImage") ?: false
        [multimedia: multimedia, height: height, rotate: rotate, hideControls: hideControls, hideShowInOtherWindow: hideShowInOtherWindow, hidePinImage: hidePinImage]
    }

    def taskLockTimeoutFragment() {
        def taskInstance = Task.get(params.int("taskId"))
        def validator = params.boolean("validator")
        [taskInstance: taskInstance, isValidator: validator]
    }

}