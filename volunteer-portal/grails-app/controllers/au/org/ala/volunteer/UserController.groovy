package au.org.ala.volunteer

class UserController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def taskService
    def authService
    def userService
    def fieldSyncService
    def fieldService
    def ROLE_ADMIN = grailsApplication.config.auth.admin_role

    def index = {
        redirect(action: "list", params: params)
    }

    def logout = {
        session.invalidate()
        redirect(url:"${params.casUrl}?url=${params.appUrl}")
    }

    def myStats = {
      userService.registerCurrentUser()
      def currentUser = authService.username()
      def userInstance = User.findByUserId(currentUser)
      redirect(action: "show", id: userInstance.id, params: params )
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (!params.sort){
          // set default sort and order
          params.sort = params.sort ? params.sort : "transcribedCount"
          params.order = "desc"
        }
        def currentUser = authService.username()
        def userList = User.list(params)
        userList.each { user ->
            def count = taskService.getCountsForUserId(user.userId)?.get(0)
            log.debug(user.userId + " count: " + count)
            if (user.transcribedCount != count) {
                // Update incorrect transcribed count (from prev bug)
                user.transcribedCount = count.toInteger()
                if (!user.hasErrors() && user.save(flush:true)) {
                    log.info("Updating counts for " + user.displayName)
                } else {
                    log.error("Failed to update user: " + user.userId + " - " + user.hasErrors())
                }
            }
        }

        [userInstanceList: User.list(params), userInstanceTotal: User.count(),currentUser: currentUser]
    }

    def project = {
        def projectInstance = Project.get(params.id)
        if (projectInstance) {
            params.max = Math.min(params.max ? params.int('max') : 10, 100)
            if(!params.sort){
              params.sort = params.sort ? params.sort : "displayName"
              params.order = "asc"
            }
            //def userList = User.list(params)
            def userList = []
            def userIds = taskService.getUserIdsAndCountsForProject(projectInstance, params)
            def userCount = taskService.getUserIdsAndCountsForProject(projectInstance, new HashMap<String, Object>()).size()
            userIds.each {
                // iterate over each user and assign to a role.
                def userId = it[0]
                def count = it[1]
                def user = User.findByUserId(userId)
                if (user) {
                    user.transcribedCount = count
                    userList.add(user)
                }
            }

//            userList.each { user ->
//                def count = taskService.getCountsForProjectAndUserId(projectInstance, user.userId)?.get(0)
//                log.debug(user.userId + " count: " + count)
//                if (user.transcribedCount != count) {
//                    // get counts for current project
//                    user.transcribedCount = count.toInteger() // temp set counts for just current project
//                }
//            }

            def currentUser = authService.username()
            render(view: "list", model:[userInstanceList: userList, userInstanceTotal: userCount, currentUser: currentUser, projectInstance: projectInstance])
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])}"
            redirect(action: "list")
        }
    }

    def create = {
        def userInstance = new User()
        userInstance.properties = params
        return [userInstance: userInstance]
    }

    def save = {
        def userInstance = new User(params)
        if (userInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), userInstance.id])}"
            redirect(action: "show", id: userInstance.id)
        }
        else {
            render(view: "create", model: [userInstance: userInstance])
        }
    }

    def show = {
        def userInstance = User.get(params.id)
        def currentUser = authService.username()
        params.max = Math.min(params.max ? params.int('max') : 20, 50)
        def recentTasks
        def totalTranscribedTasks
        def savedTasks
        def totalSavedTasks
        def project = Project.get(params.projectId)

        if (project) {
            savedTasks = taskService.getRecentlySavedTasksByProject(userInstance.getUserId(), project, params)
            totalSavedTasks = taskService.getRecentlySavedTasksByProject(userInstance.getUserId(), project, new HashMap<String, Object>()).size()
            recentTasks = Task.findAllByFullyTranscribedByAndProject(userInstance.getUserId(), project, params)
            totalTranscribedTasks = Task.countByFullyTranscribedByAndProject(userInstance.getUserId(), project)
        } else {
            savedTasks = taskService.getRecentlySavedTasks(userInstance.getUserId(), params)
            totalSavedTasks = taskService.getRecentlySavedTasks(userInstance.getUserId(), new HashMap<String, Object>()).size()
            recentTasks = Task.findAllByFullyTranscribedBy(userInstance.getUserId(), params)
            totalTranscribedTasks = Task.countByFullyTranscribedBy(userInstance.getUserId())
        }

        def fieldsInTask = [:] // map of task id to saved fields for that task
        savedTasks.each {
            // get the fields as a Map (to be able to display catalogNumber
            fieldsInTask.put(it.id, fieldSyncService.retrieveFieldsForTask(it))
        }
        recentTasks.each {
            // get the fields as a Map (to be able to display catalogNumber
            fieldsInTask.put(it.id, fieldSyncService.retrieveFieldsForTask(it))
        }
        def extraFields = [:]
        def fieldNames = ["catalogNumber"];
        fieldNames.each {
            if (recentTasks) extraFields[it] = fieldService.getLatestFieldsWithTasks(it, recentTasks, params)
        }

        def numberOfTasksEdited = taskService.getRecentlyTranscribedTasks(userInstance.getUserId(), [:]).size()
        def pointsTotal =  (userInstance.transcribedCount * 100) + ((numberOfTasksEdited - userInstance.transcribedCount ) * 10)

        if (!userInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
            redirect(action: "list")
        }
        else {
            [userInstance: userInstance, taskInstanceList: recentTasks, currentUser: currentUser, numberOfTasksEdited: numberOfTasksEdited,
                    pointsTotal: pointsTotal, totalTranscribedTasks: totalTranscribedTasks, fieldsInTask: fieldsInTask,
                    extraFields: extraFields, project: project, savedTasks: savedTasks, totalSavedTasks: totalSavedTasks]
        }
    }

    def edit = {
        def currentUser = authService.username()
        def userInstance = User.get(params.id)
        if (currentUser != null && (authService.userInRole(ROLE_ADMIN) || currentUser == userInstance.userId)) {
            if (!userInstance) {
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
                redirect(action: "list")
            }
            else {
                return [userInstance: userInstance]
            }
        } else {
            flash.message = "You do not have permission to edit this user page (ROLE_ADMIN required)"
            redirect(action: "list")
        }
    }

    def update = {
        def userInstance = User.get(params.id)
        def currentUser = authService.username()
        if (userInstance && currentUser && (authService.userInRole(ROLE_ADMIN) || currentUser == userInstance.userId)) {
            if (params.version) {
                def version = params.version.toLong()
                if (userInstance.version > version) {
                    
                    userInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'user.label', default: 'User')] as Object[], "Another user has updated this User while you were editing")
                    render(view: "edit", model: [userInstance: userInstance])
                    return
                }
            }
            userInstance.properties = params
            if (!userInstance.hasErrors() && userInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'User'), userInstance.id])}"
                redirect(action: "show", id: userInstance.id)
            }
            else {
                render(view: "edit", model: [userInstance: userInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def userInstance = User.get(params.id)
        def currentUser = authService.username()
        if (userInstance && currentUser && authService.userInRole(ROLE_ADMIN)) {
            try {
                userInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
            redirect(action: "list")
        }
    }
}
