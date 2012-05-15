<%@ page import="org.codehaus.groovy.grails.commons.ConfigurationHolder; au.org.ala.volunteer.Task" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="${ConfigurationHolder.config.ala.skin}"/>
    <g:set var="entityName" value="${message(code: 'task.label', default: 'Task')}"/>
    <title>Thank you - we are done for now!</title>
  </head>
  <body class="sublevel sub-site volunteerportal">
    <cl:navbar selected="expeditions" />

    <header id="page-header">
      <div class="inner">
        <cl:messages />
        <nav id="breadcrumb">
          <ol>
            <li><a href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
            <li class="last">Thanks - we're done!</li>
          </ol>
        </nav>
        <hgroup>
          <h1>Thank you - we are done for now !</h1>
        </hgroup>
      </div>
    </header>

    <div class="inner">
      <p>There are currently no new tasks ready to be validated.</p>
      <p>Please check back later for more validation tasks.</p>
    </div>
  </body>
</html>
