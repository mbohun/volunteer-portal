<%@ page import="au.org.ala.volunteer.Project" %>
<%@ page import="org.codehaus.groovy.grails.commons.ConfigurationHolder" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="${ConfigurationHolder.config.ala.skin}"/>
  <title><g:message code="admin.label" default="Administration"/></title>
</head>
<body class="sublevel sub-site volunteerportal">
    <div class="nav">
      <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
      <span class="menuButton">Admin</span>
    </div>
    <div class="body">
      <h2>Administration</h2>
      <div class="inner">
        <table class="bvp-expeditions">
          <thead>
            <tr>
              <th style="text-align: left">Tool</th>
              <th style="text-align: left">Description</th>
            </tr>
          </thead>
          <tr>
            <td><button style="margin: 6px" onclick="location.href='${createLink(controller:'admin', action:'mailingList')}'">Global mailing List</button></td>
            <td>Display a list of email address for all volunteers</td>
          </tr>
          <tr>
            <td><button style="margin: 6px" onclick="location.href='${createLink(controller:'picklist', action:'manage')}'">Bulk manage picklists</button></td>
            <td>Allows modification to the values held in various picklists</td>
          </tr>
          <tr>
            <td><button style="margin: 6px" onclick="location.href='${createLink(controller:'frontPage', action:'edit')}'">Configure front page</button></td>
            <td>Configure the appearance of the front page</td>
          </tr>
          <tr>
            <td><button style="margin: 6px" onclick="location.href='${createLink(controller:'project', action:'create')}'">Create project</button></td>
            <td>Create a new volunteer project</td>
          </tr>
        </table>

      </div>
    </div>
    <br />
</body>
</html>
