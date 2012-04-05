package au.org.ala.volunteer

import grails.converters.*
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

class TaskController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def taskService
    def fieldSyncService
    def fieldService
    def authService
    def ROLE_ADMIN = grailsApplication.config.auth.admin_role
    def ROLE_VALIDATOR = grailsApplication.config.auth.validator_role

    def load = {
        [projectList: Project.list()]
    }

    def project = {
        params.max = Math.min(params.max ? params.int('max') : 20, 50)
        params.order = params.order ? params.order : "asc"
        params.sort = params.sort ? params.sort : "id"
        //render(view: "list", model:[taskInstanceList: Task.list(params), taskInstanceTotal: Task.count()])
        if (params.id) {
            renderProjectListWithSearch(params, ["catalogNumber","scientificName"], "list")
        } else {
            render(view: "list", model:[taskInstanceList: Task.list(params), taskInstanceTotal: Task.count()])
        }
    }

    def projectAdmin = {
        def currentUser = authService.username()
        if (currentUser != null && (authService.userInRole(ROLE_ADMIN) || authService.userInRole(ROLE_VALIDATOR))) {
            renderProjectListWithSearch(params, ["catalogNumber","scientificName"], "adminList")
        } else {
            flash.message = "You do not have permission to view the Admin Task List page (${ROLE_ADMIN} required)"
            redirect(controller: "project", action: "index", id: params.id)
        }
    }

    def renderProjectListWithSearch(GrailsParameterMap params, List fieldNames, String view) {
        def projectInstance = Project.get(params.id)

        if (projectInstance) {
            params.max = Math.min(params.max ? params.int('max') : 20, 50)
            params.order = params.order ? params.order : "asc"
            params.sort = params.sort ? params.sort : "id"
            def taskInstanceList
            def taskInstanceTotal
            def extraFields = [:] // Map
            def query = params.q
            log.debug("q = " + query)
            if (query) {
                def fullList = Task.findAllByProject(projectInstance, [max: 999])
                taskInstanceList = fieldService.findAllFieldsWithTasksAndQuery(fullList, query, params)
                taskInstanceTotal = fieldService.countAllFieldsWithTasksAndQuery(fullList, query)
                if (taskInstanceTotal) {
                    fieldNames.each {
                        extraFields[it] = fieldService.getLatestFieldsWithTasks(it, taskInstanceList, params)
                    }
                    //extraField = fieldService.getLatestFieldsWithTasks(fieldName, taskInstanceList, params)
                }
            } else {
                taskInstanceList = Task.findAllByProject(projectInstance, params)
                taskInstanceTotal = Task.countByProject(projectInstance)
                if (taskInstanceTotal) {
                    fieldNames.each {
                        extraFields[it] = fieldService.getLatestFieldsWithTasks(it, taskInstanceList, params)
                    }
                    //extraField = fieldService.getLatestFieldsWithTasks(fieldName, taskInstanceList, params)
                }
            }
            // add some associated "field" values
            render(view: view, model: [taskInstanceList: taskInstanceList, taskInstanceTotal: taskInstanceTotal,
                    projectInstance: projectInstance, extraFields: extraFields])
        }
        else {
            flash.message = "No project found for ID " + params.id
        }
    }

    def renderListWithSearch(GrailsParameterMap params, List fieldNames, String view) {
        params.max = Math.min(params.max ? params.int('max') : 20, 50)
        params.order = params.order ? params.order : "asc"
        params.sort = params.sort ? params.sort : "id"
        def taskInstanceList
        def taskInstanceTotal
        def extraFields = [:] // Map
        def query = params.q
        log.info("q = " + query)
        if (query) {
            def max = params.max // store it
            def offset = params.offset?:0
            params.max = 999 // to get full list
            params.offset = 0
            def fullList = Task.list(params)
            params.max = max // reset for paging
            params.offset = offset
            taskInstanceList = fieldService.findAllFieldsWithTasksAndQuery(fullList, query, params)
            taskInstanceTotal = fieldService.countAllFieldsWithTasksAndQuery(fullList, query)
            if (taskInstanceTotal) {
                fieldNames.each {
                    extraFields[it] = fieldService.getLatestFieldsWithTasks(it, taskInstanceList, params)
                }
            }
        } else {
            taskInstanceList = Task.list(params)
            taskInstanceTotal = Task.count()
            fieldNames.each {
                extraFields[it] = fieldService.getLatestFieldsWithTasks(it, taskInstanceList, params)
            }
        }

        render(view: view, model: [taskInstanceList: taskInstanceList, taskInstanceTotal: taskInstanceTotal,
                extraFields: extraFields])
    }

    /**
     * Webservice for Google Maps to display task details in infowindow
     */
    def details = {
        def taskInstance = Task.get(params.id)
        Map recordValues = fieldSyncService.retrieveFieldsForTask(taskInstance)
        def jsonObj = [:]
        jsonObj.put("cat", recordValues?.get(0)?.catalogNumber)
        jsonObj.put("name", recordValues?.get(0)?.scientificName)
        jsonObj.put("transcriber", User.findByUserId(taskInstance.fullyTranscribedBy).displayName)
        render jsonObj as JSON
    }

    def loadCSV = {
        def projectId = params.int('projectId')
        flash.message = taskService.loadCSV(projectId, params.csv)
        redirect(action: "list", params: [id:  projectId])
    }

    def index = {
        redirect(action: "list", params: params)
    }

    /** list all tasks  */
    def list = {
        params.max = Math.min(params.max ? params.int('max') : 20, 50)
        params.order = params.order ? params.order : "asc"
        params.sort = params.sort ? params.sort : "id"
        //render(view: "list", model:[taskInstanceList: Task.list(params), taskInstanceTotal: Task.count()])
        if (params.id) {
            //redirect(action: "project", params: params)
            renderProjectListWithSearch(params, ["catalogNumber","scientificName"], "list")
        } else {
            //render(view: "list", model:[taskInstanceList: Task.list(params), taskInstanceTotal: Task.count()])
            renderListWithSearch(params, ["catalogNumber","scientificName"], "list")
        }
    }

    def thumbs = {
        params.max = Math.min(params.max ? params.int('max') : 8, 16)
        params.order = params.order ? params.order : "asc"
        params.sort = params.sort ? params.sort : "id"
        [taskInstanceList: Task.list(params), taskInstanceTotal: Task.count()]
    }

    def create = {
        def currentUser = authService.username()
        if (currentUser != null && authService.userInRole(ROLE_ADMIN)) {
            def taskInstance = new Task()
            taskInstance.properties = params
            return [taskInstance: taskInstance]
        } else {
            flash.message = "You do not have permission to view this page (${ROLE_ADMIN} required)"
            redirect(view: '/index')
        }
    }

    def save = {
        def taskInstance = new Task(params)
        if (taskInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'task.label', default: 'Task'), taskInstance.id])}"
            redirect(action: "show", id: taskInstance.id)
        }
        else {
            render(view: "create", model: [taskInstance: taskInstance])
        }
    }

    def show = {
        def taskInstance = Task.get(params.id)
        if (!taskInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
            redirect(action: "list")
        }
        else {
            [taskInstance: taskInstance]
        }
    }

    def edit = {
        def currentUser = authService.username()
        if (currentUser != null && authService.userInRole(ROLE_ADMIN)) {
            def taskInstance = Task.get(params.id)
            if (!taskInstance) {
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
                redirect(action: "list")
            }
            else {
                return [taskInstance: taskInstance]
            }
        } else {
            flash.message = "You do not have permission to view this page (${ROLE_ADMIN} required)"
            redirect(view: '/index')
        }
    }

    def update = {
        def taskInstance = Task.get(params.id)
        if (taskInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (taskInstance.version > version) {

                    taskInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'task.label', default: 'Task')] as Object[], "Another user has updated this Task while you were editing")
                    render(view: "edit", model: [taskInstance: taskInstance])
                    return
                }
            }
            taskInstance.properties = params
            if (!taskInstance.hasErrors() && taskInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'task.label', default: 'Task'), taskInstance.id])}"
                redirect(action: "show", id: taskInstance.id)
            }
            else {
                render(view: "edit", model: [taskInstance: taskInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def taskInstance = Task.get(params.id)
        if (taskInstance) {
            try {
                taskInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
            redirect(action: "list")
        }
    }
}