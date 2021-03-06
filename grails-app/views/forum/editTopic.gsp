<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title><g:message code="default.application.name" /> - Atlas of Living Australia</title>
        <meta name="layout" content="${grailsApplication.config.ala.skin}"/>
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'vp.css')}"/>
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'forum.css')}"/>

        <style type="text/css">

        #title {
            width: 400px;
        }

        </style>

    </head>

    <body class="">

        <r:script type="text/javascript">

            $(document).ready(function () {
            });

        </r:script>

        <cl:navbar selected=""/>

        <header id="page-header">
            <div class="inner">
                <cl:messages/>
                <vpf:forumNavItems topic="${topic}" lastLabel="${message(code: 'forum.editTopic.label', default: "Edit Topic")}"/>
                %{--<h1><g:message code="forum.editProjectTopicHeading.label" default="{0} Forum - Edit Topic" args="${[topic.project.featuredLabel]}"/></h1>--}%
            </div>
        </header>

        <div>
            <div class="inner">
                <g:form controller="forum" action="updateTopic" params="${[topicId: topic.id]}">
                    <div class="newTopicFields">
                        <h2><g:message code="forum.projectTopicTitle.label" default="Topic title"/></h2>
                        <g:textField id="title" name="title" maxlength="200" value="${topic.title}"/>
                    %{--<h2><g:message code="forum.newProjectTopicMessage.label" default="New topic message" /></h2>--}%
                    %{--<g:textArea name="text" rows="6" cols="80" value="${params.text}" />--}%
                        <vpf:ifModerator>
                            <div class="moderatorOptions">
                                <h2><g:message code="forum.moderatorOptions.label" default="Moderator Options"/></h2>
                                <label for="sticky"><g:message code="forum.sticky.label" default="Sticky"/></label>
                                <g:checkBox name="sticky" checked="${topic.sticky}"/>
                                <br/>
                                <label for="locked"><g:message code="forum.locked.label" default="Locked"/></label>
                                <g:checkBox name="locked" checked="${topic.locked}"/>
                                <br/>
                                <label for="priority"><g:message code="forum.priority.label" default="Priority"/></label>
                                <g:select from="${au.org.ala.volunteer.ForumTopicPriority.values()}" name="priority" value="${topic.priority}"/>
                                <br/>
                                <label for="featured"><g:message code="forum.featured.label" default="Featured topic"/></label>
                                <g:checkBox name="featured" checked="${topic.featured}"/>
                                <span>Will be displayed on the Forum entry page if ticked</span>

                            </div>
                        </vpf:ifModerator>
                        <button type="submit">Update</button>
                    </div>
                </g:form>
            </div>
        </div>
    </body>
</html>