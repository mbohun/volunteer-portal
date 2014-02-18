package au.org.ala.volunteer

import grails.converters.*
import groovy.time.TimeCategory
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.MultipartFile

class TaskController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    public static final String PROJECT_LIST_STATE_SESSION_KEY = "project.admin.list.state"
    public static final String PROJECT_LIST_LAST_PROJECT_ID_KEY = "project.admin.list.lastProjectId"

    def taskService
    def fieldSyncService
    def fieldService
    def authService
    def taskLoadService
    def logService
    def userService
    def grailsApplication
    def stagingService

    def load = {
        [projectList: Project.list()]
    }

    def project = {
        params.max = Math.min(params.max ? params.int('max') : 20, 50)
        params.order = params.order ? params.order : "asc"
        params.sort = params.sort ? params.sort : "id"
        //render(view: "list", model:[taskInstanceList: Task.list(params), taskInstanceTotal: Task.count()])
        if (params.id) {
            renderProjectListWithSearch(params, "list")
        } else {
            render(view: "list", model:[taskInstanceList: Task.list(params), taskInstanceTotal: Task.count()])
        }
    }

    def projectAdmin = {
        def currentUser = authService.username()
        def project = Project.get(params.int("id"))
        if (project && currentUser && userService.isValidator(project)) {
            renderProjectListWithSearch(params, "adminList")
        } else {
            flash.message = "You do not have permission to view the Admin Task List page (you need to be either an adminstrator or a validator)"
            redirect(controller: "project", action: "index", id: params.id)
        }
    }

    def extra_fields_default = ["catalogNumber","scientificName"]

    def extra_fields_FieldNoteBook = []

    def extra_fields_FieldNoteBookDoublePage = []

    private def renderProjectListWithSearch(GrailsParameterMap params, String view) {

        def projectInstance = Project.get(params.id)

        def currentUser = authService.username()
        def userInstance = User.findByUserId(currentUser)

        String[] fieldNames = null;

        def extraFieldProperty = this.metaClass.properties.find() { it.name == "extra_fields_" + projectInstance.template.name }

        if (extraFieldProperty) {
            fieldNames = extraFieldProperty.getProperty(this)
        }

        if (fieldNames == null) {
            fieldNames = extra_fields_default
        }

        if (projectInstance) {
            // The last time we were at this view, was it for the same project?
            def lastProjectId = session[PROJECT_LIST_LAST_PROJECT_ID_KEY]
            if (lastProjectId && lastProjectId != params.id) {
                // if not, remove the state from the session
                session.removeAttribute(PROJECT_LIST_STATE_SESSION_KEY)
            }

            def lastState = session[PROJECT_LIST_STATE_SESSION_KEY] ?: [ max: 20, order: 'asc', sort: 'id', offset: 0 ]

            params.max = Math.min(params.max ? params.int('max') : lastState.max, 50)
            params.order = params.order ?: lastState.order
            params.sort = params.sort ?: lastState.sort
            params.offset = params.offset ?: lastState.offset

            // Save the current view state in the session, including the current project id
            session[PROJECT_LIST_STATE_SESSION_KEY] = [
                max: params.max,
                order: params.order,
                sort: params.sort,
                offset: params.offset
            ]
            session[PROJECT_LIST_LAST_PROJECT_ID_KEY] = params.id

            def taskInstanceList
            def taskInstanceTotal
            def extraFields = [:] // Map
            def query = params.q as String

            if (query) {
                // def fullList = Task.findAllByProject(projectInstance, [max: 9999])
                def fieldNameList = Arrays.asList(fieldNames)
                taskInstanceList = fieldService.findAllTasksByFieldValues(projectInstance, query, params, fieldNameList)
                taskInstanceTotal = fieldService.countAllTasksByFieldValueQuery(projectInstance, query, fieldNameList)
            } else {
                taskInstanceList = Task.findAllByProject(projectInstance, params)
                taskInstanceTotal = Task.countByProject(projectInstance)
            }

            if (taskInstanceTotal) {
                fieldNames.each {
                    extraFields[it] = fieldService.getLatestFieldsWithTasks(it, taskInstanceList, params).groupBy { it.task.id }
                }
            }


            def views = [:]
            if (taskInstanceList) {
                def c = ViewedTask.createCriteria()
                views = c {
                    'in'("task", taskInstanceList)
                }
                views = views?.groupBy { it.task }
            }

            def lockedMap = [:]
            views?.values().each { List viewList ->
                def max = viewList.max { it.lastView }
                use (TimeCategory) {
                    if (new Date(max.lastView) > 2.hours.ago) {
                        lockedMap[max.task?.id] = max
                    }
                }
            }

            // add some associated "field" values
            render(view: view, model: [taskInstanceList: taskInstanceList, taskInstanceTotal: taskInstanceTotal,
                    projectInstance: projectInstance, extraFields: extraFields, userInstance: userInstance, lockedMap: lockedMap])
        }
        else {
            flash.message = "No project found for ID " + params.id
        }
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

        if (params.csv) {
            def csv = params.csv;
            flash.message = taskService.loadCSV(projectId, csv)
        }
    }

    def loadCSVAsync = {
        def projectId = params.int('projectId')
        def replaceDuplicates = params.duplicateMode == 'replace'
        if (projectId && params.csv) {
            def project = Project.get(projectId)
            if (project) {
                def (success, message) = taskLoadService.loadTaskFromCSV(project, params.csv, replaceDuplicates)
                if (!success) {
                    flash.message = message + " - Try again when current load is complete."
                }
                redirect( controller:'loadProgress', action:'index')
            }
        }
    }

    def cancelLoad = {
        taskLoadService.cancelLoad()
        flash.message = "Cancelled!"
        redirect( controller:'loadProgress', action:'index')
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
            renderProjectListWithSearch(params, "list")
        } else {
            redirect(controller: 'project', action:'list')
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
        if (currentUser != null && authService.userInRole(CASRoles.ROLE_ADMIN)) {
            def taskInstance = new Task()
            taskInstance.properties = params
            return [taskInstance: taskInstance]
        } else {
            flash.message = "You do not have permission to view this page (${CASRoles.ROLE_ADMIN} required)"
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

    def showDetails = {
        def taskInstance = Task.get(params.int('id'))

        def c = Field.createCriteria()
        def fields = c.list {
            eq('task', taskInstance)
            and {
                order('name', 'asc')
                order('created', 'asc')
            }
        }

        // def fields = Field.findAllByTask(taskInstance, [order: 'updated,superceded'])
        [taskInstance: taskInstance, fields: fields]
    }

    def show = {
        def taskInstance = Task.get(params.id)
        if (!taskInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
            redirect(action: "list")
        } else {

            def currentUser = authService.username()

            def readonly = false
            def msg = ""


            if (taskInstance) {

                // first check is user is logged in...
                if (!currentUser) {
                    readonly = true
                    msg = "You are not logged in. In order to transcribe tasks you need to register and log in."
                } else {
                    // work out if the task is currently being edited by someone else...
                    def prevUserId = null
                    def prevLastView = 0
                    taskInstance.viewedTasks.each { viewedTask ->
                        // viewedTasks is a set so order is not guaranteed
                        if (viewedTask.lastView > prevLastView) {
                            // store the most recent viewedTask
                            prevUserId = viewedTask.userId
                            prevLastView = viewedTask.lastView
                        }
                    }

                    log.debug "<task.show> userId = " + currentUser + " || prevUserId = " + prevUserId + " || prevLastView = " + prevLastView
                    def millisecondsSinceLastView = (prevLastView > 0) ? System.currentTimeMillis() - prevLastView : null

                    if (prevUserId != currentUser && millisecondsSinceLastView && millisecondsSinceLastView < grailsApplication.config.viewedTask.timeout) {
                        // task is already being viewed by another user (with timeout period)
                        log.warn "Task was recently viewed: " + (millisecondsSinceLastView / (60 * 1000)) + " min ago by ${prevUserId}"
                        msg = "This task is being viewed/edited by another user, and is currently read-only"
                        readonly = true
                    } else if (taskInstance.fullyValidatedBy && taskInstance.isValid != null) {
                        msg = "This task has been validated, and is currently read-only."
                        if (userService.isValidator(taskInstance.project)) {
                            def link = createLink(controller: 'validate', action: 'task', id: taskInstance.id)
                            msg += ' As a validator you may review/edit this task by clicking <a href="' + link + '">here</a>.'
                        }
                        readonly = true
                    }
                }
            }

            flash.message = msg
            if (!readonly) {
                redirect(controller: 'transcribe', action: 'task', id: params.id)
            } else {
                def project = Project.findById(taskInstance.project.id)
                def template = Template.findById(project.template.id)
                def isReadonly = 'readonly'
                def isValidator = userService.isValidator(project)
                logService.log currentUser + " has role: ADMIN = " + authService.userInRole(CASRoles.ROLE_ADMIN) + " &&  VALIDATOR = " + isValidator

                def imageMetaData = taskService.getImageMetaData(taskInstance)

                //retrieve the existing values
                Map recordValues = fieldSyncService.retrieveFieldsForTask(taskInstance)
                render(view: '/transcribe/task', model: [taskInstance: taskInstance, recordValues: recordValues, isReadonly: isReadonly, template: template, imageMetaData: imageMetaData])
            }
        }
    }

    def edit = {
        def currentUser = authService.username()
        if (currentUser != null && authService.userInRole(CASRoles.ROLE_ADMIN)) {
            def taskInstance = Task.get(params.id)
            if (!taskInstance) {
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
                redirect(action: "list")
            }
            else {
                return [taskInstance: taskInstance]
            }
        } else {
            flash.message = "You do not have permission to view this page (${CASRoles.ROLE_ADMIN} required)"
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

    def showImage = {

        if (params.id) {
            def task = Task.findById(params.int("id"))

            if (task) {
                Task prevTask = null
                Task nextTask = null
                Integer sequenceNumber = null;
                def field = Field.findByTaskAndName(task, "sequenceNumber")
                if (field) {
                    sequenceNumber = Integer.parseInt(field.value)
                    prevTask = taskService.findByProjectAndFieldValue(task.project, "sequenceNumber", (sequenceNumber - 1).toString())
                    nextTask = taskService.findByProjectAndFieldValue(task.project, "sequenceNumber", (sequenceNumber + 1).toString())
                }

                [taskInstance: task, sequenceNumber: sequenceNumber, prevTask:prevTask, nextTask:nextTask]
            }
        }
    }

    def taskBrowserFragment = {
        if (params.projectId) {
            Task task = null;
            if (params.taskId) {
                task = Task.get(params.int("taskId"))
            }
            def projectInstance = Project.get(params.int("projectId"))
            [projectInstance: projectInstance, taskInstance: task]
        }
    }


    def taskBrowserTaskList = {
        if (params.taskId) {
            def task = Task.get(params.int("taskId"))
            def projectInstance = task?.project
            def taskList = taskService.transcribedDatesByUserAndProject(getUserId(), projectInstance.id, params.search_text)

            taskList = taskList.sort { it.lastEdit }

            if (task) {
                taskList.remove(task)
            }
            [projectInstance: projectInstance, taskList: taskList.toList(), taskInstance: task]
        }

    }

    def getUserId = {
        def userId = authService.username();
        return userId;
    }

    def taskDetailsFragment = {
        def task = Task.get(params.int("taskId"))
        if (task) {

            def userId = getUserId();

            def c = Field.createCriteria();

            def fields = c {
                and {
                    eq("task", task)
                    eq("superceded", false)
                    eq("transcribedByUserId", userId)
                }
            }

            def lastEdit = fields.max({ it.updated })?.updated

            def projectInstance = task.project;
            def template = projectInstance.template;
            def templateFields = TemplateField.findAllByTemplate(template)

            def fieldMap = [:]
            def fieldLabels = [:]
            for (Field field : fields) {

                def templateField = templateFields.find {
                    it.fieldType.toString() == field.name
                }
                if (templateField && field.value && templateField.type != FieldType.hidden) {
                    def category = templateField.category;
                    if (templateField.fieldType == DarwinCoreField.occurrenceRemarks) {
                        category = FieldCategory.labelText
                    } else if (templateField.fieldType == DarwinCoreField.verbatimLocality) {
                        category = FieldCategory.location
                    }

                    def fieldList = fieldMap[category]
                    if (!fieldList) {
                        fieldList = []
                        fieldMap[category] = fieldList
                    }
                    fieldList << field;
                    fieldLabels[field.name] = templateField.label ?: templateField.fieldType.label
                }
            }

            // These categories should be in this order
            def categoryNames  =[ FieldCategory.labelText, FieldCategory.collectionEvent, FieldCategory.location, FieldCategory.identification, FieldCategory.miscellaneous ]
            // any remaining categories can go in any order...
            for (TemplateField tf : templateFields) {
                if (!categoryNames.contains(tf.category)) {
                    categoryNames << tf.category
                }
            }

            def catalogNumberField = fieldService.getFieldForTask(task, "catalogNumber")

            [taskInstance: task, fieldMap: fieldMap, fieldLabels: fieldLabels, sortedCategories: categoryNames, dateTranscribed: lastEdit, catalogNumber: catalogNumberField?.value ]
        }
    }

    def ajaxTaskData = {
        def task = Task.get(params.int("taskId"))

        def username = getUserId();

        if (task) {
            def c = Field.createCriteria();

            def fields = c {
                and {
                    eq("task", task)
                    eq("superceded", false)
                    eq("transcribedByUserId", username)
                }
            }

            def projectInstance = task.project;
            def template = projectInstance.template;
            def templateFields = TemplateField.findAllByTemplate(template, )
            def results = [:]
            for (Field field : fields) {

                def templateField = templateFields.find {
                    it.fieldType.toString() == field.name
                }
                if (templateField && field.value && templateField.type != FieldType.hidden) {
                    results["recordValues\\.${field.recordIdx?:0}\\.${field.name}"] = field.value
                }
            }

            render results as JSON
        }
    }

    def staging() {
        def projectInstance = Project.get(params.int("projectId"))
        if (!projectInstance) {
            redirect(controller: 'index')
            return
        }
        def profile = ProjectStagingProfile.findByProject(projectInstance)
        if (!profile) {
            profile = new ProjectStagingProfile(project: projectInstance)
            profile.save(flush: true, failOnError: true)
            profile.addToFieldDefinitions(new StagingFieldDefinition(fieldDefinitionType: FieldDefinitionType.NameRegex, format: "^(.*)\$", fieldName: "externalIdentifier"))
        }

        def images = stagingService.buildTaskMetaDataList(projectInstance)

        [projectInstance: projectInstance, images: images, profile:profile, hasDataFile: stagingService.projectHasDataFile(projectInstance), dataFileUrl:stagingService.dataFileUrl(projectInstance)]
    }

    def uploadStagingDataFile() {
        def projectInstance = Project.get(params.int("projectId"))
        if (projectInstance) {
            if(request instanceof MultipartHttpServletRequest) {
                MultipartFile f = ((MultipartHttpServletRequest) request).getFile('dataFile')
                if (f != null) {
                    def allowedMimeTypes = ['text/plain','text/csv']
                    if (!allowedMimeTypes.contains(f.getContentType())) {
                        flash.message = "The image file must be one of: ${allowedMimeTypes}"
                        redirect(action:'staging', params:[projectId:projectInstance?.id])
                        return
                    }
                    stagingService.uploadDataFile(projectInstance, f)
                }
            }
        }
        redirect(action:'staging', params:[projectId:projectInstance?.id])
    }

    def uploadTaskDataFile() {
        def projectInstance = Project.get(params.int("projectId"))
        if (projectInstance) {
            if(request instanceof MultipartHttpServletRequest) {
                MultipartFile f = ((MultipartHttpServletRequest) request).getFile('dataFile')
                if (f != null) {
                    def allowedMimeTypes = ['text/plain','text/csv']
                    if (!allowedMimeTypes.contains(f.getContentType())) {
                        flash.message = "The image file must be one of: ${allowedMimeTypes}"
                        redirect(action:'loadTaskData', params:[projectId:projectInstance?.id])
                        return
                    }
                    stagingService.uploadDataFile(projectInstance, f)
                }
            }
        }
        redirect(action:'loadTaskData', params:[projectId:projectInstance?.id])
    }

    def clearTaskDataFile() {
        def projectInstance = Project.get(params.int("projectId"))
        if (projectInstance) {
            stagingService.clearDataFile(projectInstance)
        }
        redirect(action:'loadTaskData', params:[projectId:projectInstance?.id])
    }

    def clearStagedDataFile() {
        def projectInstance = Project.get(params.int("projectId"))
        if (projectInstance) {
            stagingService.clearDataFile(projectInstance)
        }
        redirect(action:'staging', params:[projectId:projectInstance?.id])
    }

    def deleteAllStagedImages() {
        def projectInstance = Project.get(params.int("projectId"))
        if (projectInstance) {
            stagingService.deleteStagedImages(projectInstance)
        }
        redirect(action:'staging', params:[projectId:projectInstance?.id])
    }

    def stageImage() {
        def projectInstance = Project.get(params.int("projectId"))
        if (projectInstance) {
            if(request instanceof MultipartHttpServletRequest) {
                ((MultipartHttpServletRequest) request).getMultiFileMap().imageFile.each { f ->
                    if (f != null) {
                        def allowedMimeTypes = ['image/jpeg', 'image/gif', 'image/png']
                        if (!allowedMimeTypes.contains(f.getContentType())) {
                            flash.message = "The image file must be one of: ${allowedMimeTypes}"
                            return
                        }

                        try {
                            stagingService.stageImage(projectInstance, f)
                        } catch (Exception ex) {
                            flash.message = "Failed to upload image file: " + ex.message;
                        }
                    }

                }
            }

        }
        redirect(action:'staging', params:[projectId:projectInstance?.id])
    }

    def unstageImage() {
        def projectInstance = Project.get(params.int("projectId"))
        def imageName = params.imageName
        if (projectInstance && imageName) {
            try {
                stagingService.unstageImage(projectInstance, imageName)
            } catch (Exception ex) {
                flash.message = "Failed to delete image: " + ex.message
            }
        }
        redirect(action:'staging', params:[projectId:projectInstance?.id])
    }

    def addFieldDefinition() {
        def projectInstance = Project.get(params.int("projectId"))
        def fieldName = params.fieldName
        if (projectInstance && fieldName) {
            def profile = ProjectStagingProfile.findByProject(projectInstance)
            profile.addToFieldDefinitions(new StagingFieldDefinition(fieldDefinitionType: FieldDefinitionType.Literal, format: "", fieldName: fieldName))
        }

        redirect(action:'staging', params:[projectId:projectInstance?.id])
    }

    def updateFieldDefinitionType() {
        def projectInstance = Project.get(params.int("projectId"))
        def fieldDefinition = StagingFieldDefinition.get(params.int("fieldDefinitionId"))
        String newFieldType = params.newFieldType
        if (projectInstance && fieldDefinition && newFieldType) {
            fieldDefinition.fieldDefinitionType = newFieldType
        }
        redirect(action:'staging', params:[projectId:projectInstance?.id])
    }

    def updateFieldDefinitionFormat() {
        def projectInstance = Project.get(params.int("projectId"))
        def fieldDefinition = StagingFieldDefinition.get(params.int("fieldDefinitionId"))
        String newFieldFormat = params.newFieldFormat
        if (projectInstance && fieldDefinition && newFieldFormat) {
            fieldDefinition.format = newFieldFormat
        }
        redirect(action:'staging', params:[projectId:projectInstance?.id])
    }

    def deleteFieldDefinition() {
        def projectInstance = Project.get(params.int("projectId"))
        def fieldDefinition = StagingFieldDefinition.get(params.int("fieldDefinitionId"))
        if (projectInstance && fieldDefinition) {
            fieldDefinition.delete()
        }
        redirect(action:'staging', params:[projectId:projectInstance?.id])
    }

    def loadStagedTasks() {

        def projectInstance = Project.get(params.int("projectId"))
        if (projectInstance) {
            def results = taskLoadService.loadTasksFromStaging(projectInstance)
            flash.message = results.message
            if (results.success) {
                redirect( controller:'loadProgress', action:'index')
                return
            }
        }
        redirect(action:'staging', params:[projectId:projectInstance?.id])
    }

    def exportStagedTasksCSV() {
        def projectInstance = Project.get(params.int("projectId"))

        if (projectInstance) {

            def profile = ProjectStagingProfile.findByProject(projectInstance)

            response.addHeader("Content-type", "text/plain")
            def writer = new BVPCSVWriter( (Writer) response.writer,  {
                'imageName' { it.name }
                'url' { it.url }
            })

            profile.fieldDefinitions.each { field ->
                writer.columns[field.fieldName] =  {
                    it.valueMap[field.fieldName] ?: ''
                }
            }
            writer.resetProducers()

            writer.writeHeadings = false

            def images = stagingService.buildTaskMetaDataList(projectInstance)

            images.each {
                writer << it
            }

            response.writer.flush()
        }

    }

    def loadTaskData() {

        def projectInstance = Project.get(params.int("projectId"))
        if (!projectInstance) {
            flash.errorMessage = "No project/invalid project id!"
            redirect(controller:'admin', action:'index')
            return
        }

        def hasDataFile = stagingService.projectHasDataFile(projectInstance)
        def fieldValues = [:]
        def columnNames = []
        if (hasDataFile) {
            fieldValues = stagingService.buildTaskFieldValuesFromDataFile(projectInstance)
            if (fieldValues.size() > 0) {
                columnNames = fieldValues[fieldValues.keySet().first()].keySet().collect()
            }
        }

        [projectInstance: projectInstance, hasDataFile: hasDataFile, dataFileUrl:stagingService.dataFileUrl(projectInstance), fieldValues: fieldValues, columnNames: columnNames]
    }

    def processTaskDataLoad() {

        def projectInstance = Project.get(params.int("projectId"))
        if (projectInstance) {
            def results = stagingService.loadTaskDataFromFile(projectInstance)
            flash.message = results?.message
        }

        redirect(action:'loadTaskData', params:[projectId: projectInstance?.id])
    }

    // One of task to help transition to explicit date recording against tasks
    def calculateDates() {

        taskService.calculateTaskDates()

        redirect(controller:'admin', action:'index')

    }

    def exportOptionsFragment() {
        [exportCriteria: params.exportCriteria, projectId: params.projectId]
    }

    def viewedTaskFragment() {
        def viewedTask = ViewedTask.get(params.int("viewedTaskId"));
        if (viewedTask) {
            def lastViewedDate = new Date(viewedTask?.lastView)
            def tc = TimeCategory.minus(new Date(), lastViewedDate)
            def agoString = "${tc} ago"

            [viewedTask: viewedTask, lastViewedDate: lastViewedDate, agoString: agoString]
        }
    }

}