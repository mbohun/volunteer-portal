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
  <body class="sub-site volunteerportal">

    <nav id="nav-site">
      <ul class="sf sf-js-enabled"><li class="nav-bvp selected"><a href="http://test.ala.org.au/biodiversity-volunteer-portal/">Biodiversity Volunteer Portal</a></li><li class="nav-expeditions"><a href="http://test.ala.org.au/biodiversity-volunteer-portal/virtual-expeditions/">Expeditions</a></li><li class="nav-tutorials"><a href="http://test.ala.org.au/biodiversity-volunteer-portal/tutorials/">Tutorials</a></li><li class="nav-aboutbvp"><a href="http://test.ala.org.au/biodiversity-volunteer-portal/about-the-biodiversity-volunteer-portal/">About the Portal</a></li></ul>
    </nav>

    <cl:ifTest>
      <div class="message">Test Environment</div>
    </cl:ifTest>

    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>

    <header id="page-header">      
      <div class="inner">
        <hgroup>
              <h1>Biodiversity Volunteer Portal</h1>
              <h2>Helping to understand, manage and conserve Australia's biodiversity<br>through community based capture of biodiversity data</h2>
        </hgroup>
        <nav id="nav-1-2-3">
          <ol>
            <li><span class="numbered">1</span> <a href="https://auth.ala.org.au/emmet/selfRegister.html" class="button orange">Register</a> <p>Already registered with the Atlas?<br><a href="https://auth.ala.org.au/cas/login?service=http://test.ala.org.au/wp-login.php?redirect_to=http://test.ala.org.au/biodiversity-volunteer-portal/">Log in</a>.</p></li>
            <li class="double"><div style="float:left;postition:relative;"><span class="numbered">2</span> <a href="" class="button orange">Join a virtual expedition</a> <p><a href="/virtual-expeditions/">Find a virtual expedition</a> that suits you.</p></div><span class="grey" style="float:left;postition:relative;">or</span> <div style="float:left;postition:relative;"><a href="" class="button orange">Start transcribing</a> <p>Join the <a href="">virutal expedition of the day</a>.</p></div></li>
            <li class="last"><span class="numbered">3</span> <a href="" class="button orange">Become a leader</a> <p>Are you ready to become an <a href="">expedition leader</a>?</p></li>
          </ol>
        </nav>
      </div><!--inner-->
    </header>
  

    <div class="inner">
      <div class="col-wide">
        <section>
          <h1 class="orange">Help us capture Australia's biodiversity</h1>
          <p>Help capture the wealth of information hidden in our natural history collections, field notebooks and survey sheets. This information will be used for better understanding, managing and conserving our precious biodiversity. <a href="/about-the-biodiversity-volunteer-portal/" class="button">Learn more</a></p>

          <h2 class="orange">Virtual expedition of the day</h2>
          <div class="button-nav"><a href="/volunteer-portal/project/index/122476" style="background-image:url(${featuredProject.featuredImage});"><h2>${featuredProject.featuredLabel}</h2></a></div>
          <div>
            <span class="eyebrow">${featuredProject.featuredOwner}</span>
            <h2 class="grey"><a href="/volunteer-portal/project/index/${featuredProject.id}">${featuredProject.name}</a></h2>
            <p>${featuredProject.shortDescription} <a href="/volunteer-portal/project/index/${featuredProject.id}" class="button">Start transcribing</a></p>
          </div>

          <hgroup><h2 class="alignleft">More expeditions</h2><a href="/biodiversity-volunteer-portal/virtual-expeditions/" class="button alignright">View all</a></hgroup>
          <nav>
            <ol>
              <li><a href="" style="background-image:url(http://test.ala.org.au/wp-content/themes/ala2011/images/expedition-am-cicadas.jpg);"><h2>Cicadas</h2></a></li>
              <li><a href="" style="background-image:url(http://test.ala.org.au/wp-content/themes/ala2011/images/expedition-am-leafhoppers.jpg);"><h2>Leafhoppers</h2></a></li>
              <li class="last"><a href="" style="background-image:url(http://test.ala.org.au/wp-content/themes/ala2011/images/expedition-am-planthoppers.jpg);"><h2>Planthoppers</h2></a></li>
            </ol>
          </nav>
        </section>
      </div> <!-- col-wide -->

      <div class="col-narrow last">
        <section>
          <table border="0" class="borders">
            <thead>
              <tr>
                <th colspan="2"><h2>Leader board</h2> <a class="button alignright" href="/leader-board/">View all</a></th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>Deborah Cox</td>
                <td>2138</td>
              </tr>
              <tr>
                <td>Jim Richardson</td>
                <td>2134</td>
              </tr>
              <tr>
                <td>Rhiannon Stephens</td>
                <td>1234</td>
              </tr>
              <tr>
                <td>Donald Hobern</td>
                <td>765</td>
              </tr>
              <tr>
                <td>Dave Martin</td>
                <td>3</td>
              </tr>
            </tbody>
          </table>
        </section>

        <section id="expedition-stats">
          <h2>Expedition stats</h2>
          <ul>
            <li><strong>5175</strong> tasks of <strong>14320</strong> completed</li>
            <li><strong>95</strong> volunteer transcribers</li>
          </ul>
        </section>
        <section>
          <h2>News</h2>
          <article>
            <time datetime="2012-03-07">7 March 2012</time>
            <h3><a href="">Froghopper expedition complete</a></h3>
            <p>Well done Jim Richardson for leading the <a href="/virtual-expedtions/">Froghopper Expedition</a> to completion!</p>
          </article>
        </section>
      </div>

      <cl:isLoggedIn>
        <g:link controller="admin" action="index" style="color:#DDDDDD;">Admin</g:link>
      </cl:isLoggedIn>

    </div>

    <script type="text/javascript">

        $(function() {
          $("#rollovers").tabs("#description-panes > div", {event:'mouseover', effect: 'fade', fadeOutSpeed: 400});
        });
        $('#description-panes img.active').click(function() {
            document.location.href = $(this).next('a').attr('href');
        });
        $('#rollovers img.active').css("cursor","pointer").click(function() {
            document.location.href = "${resource(dir:'project/index/')}" + $(this).attr('id');
        });
    </script>
  </body>
</html>