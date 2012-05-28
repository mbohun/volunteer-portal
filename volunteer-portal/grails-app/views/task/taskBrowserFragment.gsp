<%@ page import="org.codehaus.groovy.grails.commons.ConfigurationHolder" %>
<style type="text/css">

  #task_browser_controls, #current_task_header {
    background: #3D464C;
  }

  #task_browser_controls h3, #current_task_header h3 {
    color: white;
    padding-bottom: 6px;
  }

  #task_browser_controls hr, #current_task_header hr {
    clear: both;
    height: 1px;
    width: auto;
    background-color: #D1D1D1;
    margin-bottom: 6px;
    border:none;
  }

  #task_location {
    color: white;
  }

</style>

<div>
  <g:if test="${taskInstance}">
    <div id="current_task_header">
      <h3>Image from current task</h3>
      <hr/>
    </div>
    <div class="dialog" id="imagePane" >
      <g:each in="${taskInstance.multimedia}" var="m" status="i">
        <g:if test="${!m.mimeType || m.mimeType.startsWith('image/')}">
          <g:set var="imageUrl" value="${ConfigurationHolder.config.server.url}${m.filePath}"/>
          <div class="pageViewer" id="journalPageImg" style="height:200px">
              <div><img id="image_${i}" src="${imageUrl}" style="width:100%;"/></div>
          </div>
        </g:if>
      </g:each>
    </div>
    <div style="height: 6px"></div>
  </g:if>
  <div id="task_browser_controls">
    <h3>Your previously transcribed tasks in ${projectInstance.featuredLabel}</h3>

    <g:if test="${taskList?.size}">
      <div id="task_list" style="display: none" currentTaskIndex="0" taskCount="${taskList.size}">
        <g:each in="${taskList}" var="task" status="index">
          <div id="task_${index}" task_id="${task.id}" transcribed="${task.lastEdit}">${task.id}</div>
        </g:each>
      </div>
      <div>
        <span style="padding: 5px; float: left">
          <button id="show_prev_task"><img src="${resource(dir:'images',file:'left_arrow.png')}">&nbsp;Previous</button>
          <button id="show_next_task">Next&nbsp;<img src="${resource(dir:'images',file:'right_arrow.png')}"></button>
          <span id="task_location"></span>
        </span>
        <span style="padding: 5px;float:right">
          <button id="copy_task_data">Copy</button>
          <button id="cancel_button">Cancel</button>
        </span>

      </div>
      <hr />
     </div>
    <div id="task_content" />


    </div>
    <script type="text/javascript">

      function updateLocation() {
        var currentTaskIndex = $("#task_list").attr("currentTaskIndex");
        var taskListSize = parseInt($("#task_list").attr("taskCount"));
        $("#task_location").text((parseInt(currentTaskIndex) + 1) + " of " + taskListSize);

        if (currentTaskIndex == 0) {
          $("#show_prev_task").attr("disabled", "true")
        } else {
          $("#show_prev_task").removeAttr("disabled")
        }

        if (currentTaskIndex >= taskListSize - 1) {
          $("#show_next_task").attr("disabled", "true")
        } else {
          $("#show_next_task").removeAttr("disabled")
        }
      }

      $("#show_next_task").click(function(e) {
        e.preventDefault();
        var currentTaskIndex = $("#task_list").attr("currentTaskIndex");
        var nextTaskIndex = parseInt(currentTaskIndex) + 1;
        var taskListSize = parseInt($("#task_list").attr("taskCount"));
        if (nextTaskIndex < taskListSize) {
          loadTask(nextTaskIndex);
        }
      });

      $("#copy_task_data").click(function(e) {
        var currentTaskIndex = $("#task_list").attr("currentTaskIndex");
        var taskId = $("#task_" + currentTaskIndex).attr("task_id")
        copyDataFromTask(taskId)
        $.fancybox.close();
      });


      $("#cancel_button").click(function(e) {
        $.fancybox.close();
      });

      $("#show_prev_task").click(function(e) {
        e.preventDefault();
        var currentTaskIndex = $("#task_list").attr("currentTaskIndex");
        var prevTaskIndex = parseInt(currentTaskIndex) - 1;
        if (prevTaskIndex >= 0) {
          loadTask(prevTaskIndex);
        }
      });


      loadTask(${taskList.size() - 1})

      function loadTask(taskIndex) {
        $("#task_list").attr("currentTaskIndex", taskIndex)
        var taskId = $("#task_" + taskIndex).attr("task_id")
        var taskUrl = "${createLink(controller: 'task', action:'taskDetailsFragment')}?taskId=" + taskId + "&taskIndex=" + taskIndex;
        $.ajax({url:taskUrl, success: function(data) {
          $("#task_content").html(data);
          updateLocation();
        }})
      }

      function clearTaskData() {

        $('[id*="recordValues\\."]').each(function(index) {
          // Don't clear the hidden fields
          if ($(this).attr('type') != 'hidden') {
            $(this).val('')
          }
        });
      }

      function copyDataFromTask(taskId) {

        // First we need to clear old data...
        clearTaskData();

        var taskDataUrl = "${createLink(controller: 'task', action:'ajaxTaskData')}?taskId=" + taskId;
        $.ajax({url:taskDataUrl, success: function(data) {
          // the data is the form of a dictionary with the keys being the form element id for each data element
          // being copied, and the value being the field value
          for (key in data) {
            // copy over the data
            var selector = "#" + key;
            $(selector).val(data[key])
          }
        }});

      }


    </script>
  </g:if>
  <g:else>
    You have no previously transcribed tasks for this project!
  </g:else>

</div>