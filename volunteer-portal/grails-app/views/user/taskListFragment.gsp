<%@ page contentType="text/html;charset=UTF-8" %>
<div>
  <div class="list">
      <table class="bvp-expeditions">
          <thead>
              <tr>
                <th colspan="4">
                  <h2>
                  <g:if test="${!totalMatchingTasks}">
                  <span>(No tasks found)</span>
                  </g:if>
                  <g:else>
                    ${totalMatchingTasks} tasks found
                    <g:if test="${projectInstance}">
                        for ${projectInstance.featuredLabel}
                    </g:if>
                  </g:else>
                  </h2>
                </th>
                <th colspan="3" style="text-align: right">
                  <span>
                    <a style="vertical-align: middle;" href="#" class="fieldHelp" title="Enter search text here to show only tasks matching values in the ImageID, CatalogNumber, Project and Transcribed columns"><span class="help-container">&nbsp;</span></a>
                  </span>
                  <g:textField id="searchbox" value="${params.q}" name="searchbox" onkeypress=""/>
                  <button onclick="doSearch()">Search</button>
                </th>
              </tr>
              <tr>

                  <g:set var="pageParams" value="${params}" />

                  <g:sortableColumn style="text-align: left" property="id" title="${message(code: 'task.id.label', default: 'Id')}" params="${pageParams}" action="show" controller="user" />

                  <g:sortableColumn style="text-align: left" property="externalIdentifier" title="${message(code: 'task.externalIdentifier.label', default: 'Image ID')}" params="${pageParams}" action="show" controller="user"}"/>

                  <g:sortableColumn style="text-align: left" property="catalogNumber" title="${message(code: 'task.catalogNumber.label', default: 'Catalog&nbsp;Number')}" params="${pageParams}" action="show" controller="user"/>

                  <g:sortableColumn style="text-align: left" property="projectName" title="${message(code: 'task.project.name', default: 'Project')}" params="${pageParams}" action="show" controller="user"/>

                  <g:sortableColumn property="lastEdit" title="${message(code: 'task.transcribed.label', default: 'Transcribed')}" params="${pageParams}" action="show" controller="user" style="text-align: left;"/>

                  <g:sortableColumn property="status" title="${message(code: 'task.isValid.label', default: 'Status')}" params="${pageParams}" action="show" controller="user" style="text-align: center;"/>

                  <th style="text-align: center;">Action</th>

              </tr>
          </thead>
          <tbody>
          <g:each in="${viewList}" status="i" var="taskInstance">
              <tr>

                  <td><g:link class="listLink" controller="task" action="show" id="${taskInstance.id}">${taskInstance.id}</g:link></td>

                  <td>${taskInstance.externalIdentifier}</td>

                  <td>${taskInstance.catalogNumber}</td>

                  <td><g:link class="listLink" controller="project" action="index" id="${taskInstance.projectId}">${taskInstance.project}</g:link></td>

                  <td>
                    <g:formatDate date="${taskInstance.lastEdit}" format="dd MMM, yyyy HH:mm:ss" />

                  </td>

                  <td style="text-align: center;">
                      ${taskInstance.status}
                  </td>

                  <td style="text-align: center;">
                      <span>
                      <g:if test="${taskInstance.fullyTranscribedBy}">
                        <button onclick="location.href='${createLink(controller:'task', action:'show', id:taskInstance.id)}'">View</button>
                        <cl:ifValidator project="${taskInstance.project}">
                          <g:if test="${taskInstance.status?.equalsIgnoreCase('validated')}">
                            <button onclick="location.href='${createLink(controller:'validate', action:'task', id:taskInstance.id)}'">Review</button>
                          </g:if>
                          <g:else>
                            <button onclick="location.href='${createLink(controller:'validate', action:'task', id:taskInstance.id)}'">Validate</button>
                          </g:else>
                        </cl:ifValidator>
                      </g:if>
                      <g:else>
                          <button onclick="location.href='${createLink(controller:'transcribe', action:'task', id:taskInstance.id)}'">Transcribe</button>
                      </g:else>
                      </span>
                  </td>

              </tr>
          </g:each>
          </tbody>
      </table>
  </div>

  <div class="paginateButtons">
    <g:paginate total="${totalMatchingTasks}" id="${userInstance?.id}" params="${params + [selectedTab: selectedTab]}" action="show" controller="user"/>
  </div>

</div>

<script type="text/javascript">
  $("th > a").addClass("button")
  $("th.sorted > a").addClass("current")

  $('#searchbox').bind('keypress', function(e) {
      var code = (e.keyCode ? e.keyCode : e.which);
      if(code == 13) {
        doSearch();
      }
  });

  doSearch = function() {
    var searchTerm = $('#searchbox').val()
    var link = "${createLink(controller: 'user', action: 'show', id: userInstance?.id)}?q=" + searchTerm + "&selectedTab=${selectedTab ?: 0}&projectId=${projectInstance?.id ?: ''}"
    window.location.href = link;
  }

</script>



