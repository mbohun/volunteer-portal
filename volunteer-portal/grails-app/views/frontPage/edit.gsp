<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="${grailsApplication.config.ala.skin}"/>
    <title><g:message code="frontPage.label" default="Front Page Configuration"/></title>
  </head>
  <body class="sublevel sub-site volunteerportal">
    <cl:navbar selected="" />
    <div class="nav">
      <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
      <span class="menuButton">ConfigureFrontPage</span>
    </div>
    <div class="inner">
      <h1>Front Page Configuration</h1>
      <cl:messages />
      <g:hasErrors bean="${frontPage}">
      <div class="errors">
          <g:renderErrors bean="${frontPage}" as="list" />
      </div>
      </g:hasErrors>
      <g:form action="save" >
        <div class="dialog">
          <table align="center">
            <tbody>
              <tr class="prop">
                  <td valign="top" class="name">
                      <label for="projectOfTheDay"><g:message code="frontPage.projectOfTheDay.label" default="Project of the day" /></label>
                  </td>
                  <td valign="top" class="value ${hasErrors(bean: frontPage, field: 'projectOfTheDay', 'errors')}">
                      <g:select name="projectOfTheDay" from="${au.org.ala.volunteer.Project.list()}" optionKey="id" optionValue="name" value="${frontPage.projectOfTheDay?.id}"  />
                  </td>
                  <td>
                    <g:link action="edit" controller="project" id="${frontPage.projectOfTheDay.id}">edit project...</g:link>
                  </td>
              </tr>

              <tr class="prop">
                  <td valign="top" class="name">
                      <label for="featuredProject1"><g:message code="frontPage.featuredProject1.label" default="Featured Project 1" /></label>
                  </td>
                  <td valign="top" class="value ${hasErrors(bean: frontPage, field: 'featuredProject1', 'errors')}">
                      <g:select name="featuredProject1" from="${au.org.ala.volunteer.Project.list()}" optionKey="id" optionValue="name" value="${frontPage.featuredProject1?.id}"  />
                  </td>
                  <td>
                    <g:link action="edit" controller="project" id="${frontPage.featuredProject1.id}">edit project...</g:link>
                  </td>
              </tr>

              <tr class="prop">
                  <td valign="top" class="name">
                      <label for="featuredProject2"><g:message code="frontPage.featuredProject2.label" default="Featured Project 2" /></label>
                  </td>
                  <td valign="top" class="value ${hasErrors(bean: frontPage, field: 'featuredProject2', 'errors')}">
                      <g:select name="featuredProject2" from="${au.org.ala.volunteer.Project.list()}" optionKey="id" optionValue="name" value="${frontPage.featuredProject2?.id}"  />
                  </td>
                  <td>
                    <g:link action="edit" controller="project" id="${frontPage.featuredProject2.id}">edit project...</g:link>
                  </td>
              </tr>

              <tr class="prop">
                  <td valign="top" class="name">
                      <label for="featuredProject3"><g:message code="frontPage.featuredProject3.label" default="Featured Project 3" /></label>
                  </td>
                  <td valign="top" class="value ${hasErrors(bean: frontPage, field: 'featuredProject3', 'errors')}">
                      <g:select name="featuredProject3" from="${au.org.ala.volunteer.Project.list()}" optionKey="id" optionValue="name" value="${frontPage.featuredProject3?.id}"  />
                  </td>
                  <td>
                    <g:link action="edit" controller="project" id="${frontPage.featuredProject3.id}">edit project...</g:link>
                  </td>
              </tr>

              <tr class="prop">
                  <td valign="top" class="name">
                      <label for="useGlobalNewsItem"><g:message code="frontPage.useGlobalNewsItem.label" default="Use global news item" /></label>
                  </td>
                  <td valign="top" class="value ${hasErrors(bean: frontPage, field: 'useGlobalNewsItem', 'errors')}">
                      <g:checkBox name="useGlobalNewsItem" value="${frontPage.useGlobalNewsItem}"  />
                      <div style="color: #808080;">If unchecked the most recent project news item will be used instead</div>
                  </td>
              </tr>

              <tr class="prop">
                  <td valign="top" class="name">
                    <label for="newsTitle"><g:message code="frontPage.newsTitle.label" default="News title" /></label>
                  </td>
                  <td valign="top" class="value ${hasErrors(bean: frontPage, field: 'newsTitle', 'errors')}">
                      <g:textField name="newsTitle" value="${frontPage?.newsTitle}" />
                  </td>
              </tr>

              <tr class="prop">
                  <td valign="top" class="name">
                      <label for="newsBody"><g:message code="frontPage.newsBody.label" default="News text" /></label>
                  </td>
                  <td valign="top" class="value ${hasErrors(bean: frontPage, field: 'newsBody', 'errors')}">
                      <g:textArea cols="50" rows="4" name="newsBody" value="${frontPage?.newsBody}" />
                  </td>
              </tr>

              <tr class="prop">
                  <td valign="top" class="name">
                    <label for="newsCreated"><g:message code="frontPage.newsCreated.label" default="News date" /></label>
                  </td>
                  <td valign="top" class="value ${hasErrors(bean: frontPage, field: 'newsCreated', 'errors')}">
                      <g:datePicker name="newsCreated" precision="day" value="${frontPage?.newsCreated}"  />
                  </td>
              </tr>

            <tr class="prop">
                <td valign="top" class="name">
                    <label for="systemMessage"><g:message code="frontPage.systemMessage.label" default="System message" /></label>
                    <div style="color: #808080;">(Displayed on every page)</div>
                </td>
                <td valign="top" class="value ${hasErrors(bean: frontPage, field: 'systemMessage', 'errors')}">
                    <g:textArea cols="50" rows="4" name="systemMessage" value="${frontPage?.systemMessage}" />
                </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                  <label for="showAchievements"><g:message code="frontPage.showAchievements.label" default="Show achievements on User stats page" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: frontPage, field: 'showAchievements', 'errors')}">
                  <g:checkBox name="showAchievements" value="${frontPage.showAchievements}"  />
              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                  <label for="enableTaskComments"><g:message code="frontPage.enableTaskComments.label" default="Enable task commenting" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: frontPage, field: 'enableTaskComments', 'errors')}">
                  <g:checkBox name="enableTaskComments" value="${frontPage.enableTaskComments}"  />
              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                  <label for="enableForum"><g:message code="frontPage.enableForum.label" default="Enable the BVP Forum" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: frontPage, field: 'enableForum', 'errors')}">
                  <g:checkBox name="enableForum" value="${frontPage.enableForum}"  />
              </td>
            </tr>

            </tbody>
          </table>
        </div>
        <div class="buttons">
            <span class="button"><g:submitButton name="save" class="save" value="${message(code: 'default.button.save.label', default: 'Save')}" /></span>
        </div>

      </g:form>
    </div>
  </body>
</html>
