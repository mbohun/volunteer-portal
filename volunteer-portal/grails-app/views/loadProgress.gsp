<%@ page contentType="text/html;charset=UTF-8" import="org.codehaus.groovy.grails.commons.ConfigurationHolder" %>
<!DOCTYPE html>
<html>
  <head>
      <title>Volunteer Portal - Atlas of Living Australia</title>
      <meta name="layout" content="${ConfigurationHolder.config.ala.skin}"/>
      <link rel="stylesheet" href="${resource(dir:'css',file:'vp.css')}" />
      <g:javascript library="jquery.tools.min"/>

      <script type="text/javascript">
          (function poll() {
             setTimeout(function() {

                 $.ajax({ url: "${createLink(controller: 'ajax', action:'loadProgress')}", success: function(data) {

                   if (data.queueLength > 0) {
                     $("#progress").css("display", "block")
                     $("#notasks").css("display", "none")

                     $("#total_records").text("" + data.totalTasks);
                     $("#start_time").text("" + data.startTime);
                     $("#start_by").text("" + data.startedBy);
                     $("#completed_tasks").text("" + data.tasksLoaded);
                     var percent =  Math.round((data.tasksLoaded / data.totalTasks) * 100);
                     $("#progressBar").css("width", "" + percent + "%")
                     $("#completed_percent").text("" + percent);
                     $("#current_item").text("" + data.currentItem);
                     $("#time_remaining").text("" + data.timeRemaining);
                     $("#error_count").text("" + data.errorCount);

                   } else {
                     $("#progress").css("display", "none")
                     $("#notasks").css("display", "block")
                   }

                   if (data.errorCount > 0) {
                     $("#error-link").css("display", "block")
                   } else {
                     $("#error-link").css("display", "none")
                   }

                 }, dataType: "json", complete: poll });

              }, 1000);
          })();
      </script>

  </head>
  <body class="sublevel sub-site volunteerportal">

    <cl:navbar selected="contact" />

    <header id="page-header">
      <div class="inner">
        <cl:messages />
        <nav id="breadcrumb">
          <ol>
            <li><a href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
            <li class="last"><g:message code="default.progress.label" default="Task Loading Progress" /></li>
          </ol>
        </nav>
        <h1>Task Load Progress</h1>
      </div>
    </header>
    <div class="inner">

      <div id="progress" style="display: none;">
        <h2>Task load progress</h2>
        <table>
          <tr>
            <td>Import started:</td>
            <td><b><span id="start_time"></b></td>
          </tr>
          <tr>
            <td>Started by:</td>
            <td><b><span id="start_by"></span></b></td>
          </tr>
          <tr>
            <td>Total number of tasks</td>
            <td><b><span id="total_records"></span></b></td>
          </tr>
          <tr>
            <td>Errors</td>
            <td><b><span id="error_count"></span></b></td>
          </tr>
        </table>

        Tasks loaded: <b><span id="completed_tasks"></span></b> (<b><span id="completed_percent"></span>%</b>), <span id="time_remaining"></span> remaining<br/>
        <div class="ui-progressbar ui-widget ui-widget-content ui-corner-all" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0">
          <div id="progressBar" class="ui-progressbar-value ui-widget-header ui-corner-left ui-corner-right" style="width: 0%; "></div>
        </div>
        Currently loading: <b><span id="current_item"></span></b>
        <br />
        <button style="margin:5px;" onclick="location.href='${createLink(controller:'task', action:'cancelLoad')}'">Cancel load</button>
      </div>

      <div id="notasks" style="display: none">
        There are currently no tasks being loaded.
      </div>


      <div id="error-link" style="display: none">
        <br/>
        <b>
          Errors or warnings have occurred during the last (or current) load. Click <g:link controller="ajax" action="taskLoadReport">here</g:link> for more information.
        </b>
      </div>

    </div>
  </body>
</html>
