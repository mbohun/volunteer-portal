<!DOCTYPE html>
<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="${grailsApplication.config.ala.skin}"/>
        <g:set var="entityName" value="${message(code: 'institution.label', default: 'Institution')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
        <cl:headerContent title="${message(code:'default.create.label', args:[entityName])}">
            <%
                pageScope.crumbs = [
                        [link:createLink(controller:'admin'),label:message(code:'default.admin.label', default:'Admin')],
                        [link:createLink(controller:'institutionAdmin'), label:message(code:'default.institutions.label', default:'Manage Institutions')]
                ]
            %>
        </cl:headerContent>
		<div id="create-institution" class="content scaffold-create" role="main">
			<g:hasErrors bean="${institutionInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${institutionInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
            <div class="container-fluid">
                <div class="row-fluid">
                        <g:form url="[controller:'institutionAdmin', action:'save']" >
                            <fieldset class="form">
                                <g:render template="form"/>
                            </fieldset>
                            <fieldset class="buttons">
                                <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                            </fieldset>
                        </g:form>
                </div>
            </div>
		</div>
	</body>
</html>
