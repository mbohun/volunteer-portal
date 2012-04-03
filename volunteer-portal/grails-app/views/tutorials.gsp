<%@ page contentType="text/html;charset=UTF-8" import="org.codehaus.groovy.grails.commons.ConfigurationHolder" %>
<!DOCTYPE html>
<html>
  <head>
      <title>Volunteer Portal - Atlas of Living Australia</title>
      <meta name="layout" content="${ConfigurationHolder.config.ala.skin}"/>
      <link rel="stylesheet" href="${resource(dir:'css',file:'vp.css')}" />
      %{--<link rel="icon" type="image/x-icon" href="${ConfigurationHolder.config.ala.baseURL}/wp-content/themes/ala/images/favicon.ico"/>--}%
      %{--<g:javascript library="jquery-1.5.1.min"/>--}%
      <g:javascript library="jquery.tools.min"/>
      <style type="text/css">

        div#wrapper > div#content {
            background-color: transparent !important;
        }

        .volunteerportal #page-header {
        	background:#f0f0e8 url(${resource(dir:'images/vp',file:'bg_volunteerportal.jpg')}) center top no-repeat;
        	padding-bottom:12px;
        	border:1px solid #d1d1d1;
        }

      </style>

  </head>
  <body class="page-parent page-child parent-pageid-27568 page-template page-template-vp-expeditions-php sublevel sub-site volunteerportal">

    <nav id="nav-site">
      <ul class="sf sf-js-enabled">
        <li class="nav-bvp"><a href="${createLink(uri:'/')}">Biodiversity Volunteer Portal</a></li>
        <li class="nav-expeditions"><g:link controller="project" action="list">Expeditions</g:link></li>
        <li class="nav-tutorials selected"><a href="${createLink(uri:'/tutorials.gsp')}">Tutorials</a></li>
        <li class="nav-aboutbvp"><a href="${createLink(uri:'/about.gsp')}">About the Portal</a></li>
      </ul>
    </nav>

    <header id="page-header">      
      <div class="inner">
        <g:if test="${flash.message}">
          <div class="message">${flash.message}</div>
        </g:if>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:message code="default.tutorials.label" default="Tutorials" /></span>
        </div>

        <h1>Tutorials</h1>
      </div>
    </header>
    <A href="${org.codehaus.groovy.grails.commons.ConfigurationHolder.config.server.url}/video/Introduction.swf"><g:message code="default.tutorial.introduction.label" default="Introduction"/></a>
    <A href="${org.codehaus.groovy.grails.commons.ConfigurationHolder.config.server.url}/video/Mapping_Tool2.swf"><g:message code="default.tutorial.introduction.label" default="Mapping tool"/></a>
    <A href="${org.codehaus.groovy.grails.commons.ConfigurationHolder.config.server.url}/video/Transcribing.swf"><g:message code="default.tutorial.introduction.label" default="Transcribing"/></a>
  </body>
</html>
