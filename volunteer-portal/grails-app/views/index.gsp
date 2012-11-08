<!DOCTYPE html>
<html>
  <head>
      <title>Volunteer Portal - Atlas of Living Australia</title>
      <meta name="layout" content="${grailsApplication.config.ala.skin}"/>
      <link rel="stylesheet" href="${resource(dir:'css',file:'vp.css')}" />
      <style type="text/css">

        div#wrapper > div#content {
            background-color: transparent !important;
        }

      </style>
  </head>
  <body class="sublevel sub-site volunteerportal">

    <cl:navbar selected="bvp" />

    <header id="page-header">      
      <div class="inner">
        <cl:messages />

        <hgroup>
              <h1>Biodiversity Volunteer Portal</h1>
              <h2>Helping to understand, manage and conserve Australia's biodiversity<br>through community based capture of biodiversity data</h2>
        </hgroup>
        <nav id="nav-1-2-3">
          <ol>
            <li>
              <cl:isNotLoggedIn>
                <span class="numbered">1</span> <a href="https://auth.ala.org.au/emmet/selfRegister.html" class="button orange">Register</a> <p>Already registered with the Atlas?<br/><cl:loginLink /></p>
              </cl:isNotLoggedIn>
              <cl:isLoggedIn>
                <span class="numbered">1</span> <h2>Hi !</h2><p>You're registered with the Atlas, so <a href="${createLink(controller: 'transcribe', id: frontPage.projectOfTheDay.id, action: 'index')}">start transcribing</a> or <a href="${createLink(controller: 'user', action:'myStats')}">view your tasks</a>.</p>
              </cl:isLoggedIn>
            </li>
            <li class="double"><div style="float:left;postition:relative;">
              <span class="numbered">2</span> <a href="${createLink(controller: 'project', action: 'list')}" class="button orange">Join a virtual expedition</a> <p><a href="${createLink(controller: 'project', action: 'list')}">Find a virtual expedition</a> that suits you.</p></div><span class="grey" style="float:left;postition:relative;">or</span>
              <div style="float:left;postition:relative;"><a href="${createLink(controller: 'transcribe', id: frontPage.projectOfTheDay.id, action: 'index')}" class="button orange">Start transcribing <img src="http://www.ala.org.au/wp-content/themes/ala2011/images/button_transcribe-orange.png" width="37" height="18" alt=""></a> <p>Join the <a href="${createLink(controller: 'project', id: frontPage.projectOfTheDay.id, action: 'index')}">virtual expedition of the day</a>.</p></div>
            </li>

            <li class="last">
              <span class="numbered">3</span><a href="${createLink(controller: 'user', action:'list')}" class="button orange">Become a leader</a> <p>Are you ready to become an <a href="${createLink(controller: 'user', action:'list')}">expedition leader</a>?</p>
            </li>
          </ol>
        </nav>
      </div><!--inner-->
    </header>
  

    <div class="inner">
      <div class="col-wide">
        <section>
          <h1 class="orange">Help us capture Australia's biodiversity</h1>
          <p>Help capture the wealth of information hidden in our natural history collections, field notebooks and survey sheets. This information will be used for better understanding, managing and conserving our precious biodiversity. <a href="${createLink(controller: 'about', action:'index')}" class="button">Learn more</a></p>

          <h2 class="orange">Virtual expedition of the day</h2>
          <div class="button-nav"><a href="${grailsApplication.config.grails.serverURL}/project/index/${frontPage.projectOfTheDay.id}" style="background-image:url(${frontPage.projectOfTheDay.featuredImage});"><h2>${frontPage.projectOfTheDay.featuredLabel}</h2></a></div>
          <div>
            <span class="eyebrow">${frontPage.projectOfTheDay.featuredOwner}</span>
            <h2 class="grey"><a href="${grailsApplication.config.grails.serverURL}/project/index/${frontPage.projectOfTheDay.id}">${frontPage.projectOfTheDay.name}</a></h2>
            <p>${frontPage.projectOfTheDay.shortDescription} <a href="${grailsApplication.config.grails.serverURL}/transcribe/index/${frontPage.projectOfTheDay.id}" class="button">Start transcribing <img src="http://www.ala.org.au/wp-content/themes/ala2011/images/button_transcribe.png" width="37" height="18" alt=""></a></p>
          </div>
        </section>
        <section>
          <hgroup><h2 class="alignleft">More expeditions</h2><a href="${createLink(controller: 'project', action: 'list')}" class="button alignright">View all</a></hgroup>
          <nav>
            <ol>
              <li><a href="${createLink(controller: 'project', id: frontPage.featuredProject1.id, action: 'index')}" style="background-image:url(${frontPage.featuredProject1.featuredImage});"><h2>${frontPage.featuredProject1.featuredLabel}</h2></a></li>
              <li><a href="${createLink(controller: 'project', id: frontPage.featuredProject2.id, action: 'index')}" style="background-image:url(${frontPage.featuredProject2.featuredImage});"><h2>${frontPage.featuredProject2.featuredLabel}</h2></a></li>
              <li class="last"><a href="${createLink(controller: 'project', id: frontPage.featuredProject3.id, action: 'index')}" style="background-image:url(${frontPage.featuredProject3.featuredImage});"><h2>${frontPage.featuredProject3.featuredLabel}</h2></a></li>
            </ol>
          </nav>
        </section>
      </div> <!-- col-wide -->

      <div class="col-narrow last">
        <section id="leaderBoardSection">
          <table border="0" class="borders">
            <thead>
              <tr>
                <th colspan="2"><h2>Leader board</h2> <a class="button alignright" href="${createLink(controller:'user', action:'list')}">View all</a></th>
              </tr>
            </thead>
            <tbody>
              <g:each in="${1..grailsApplication.config.leaderBoard.count}" var="i">
                <tr>
                  <td><img src="${resource(dir:'images', file:'spinner.gif')}"/></td>
                </tr>
              </g:each>
            </tbody>
          </table>
        </section>

        <section id="expedition-stats">
          <h2>Expedition stats</h2>
          Calculating statistics...
        </section>
        <section>
          <g:if test="${newsItem}">
            <h2>News</h2>
            <article>
              <g:if test="${newsItem?.created}">
                <time datetime="${formatDate(format: "yyyy-MM-dd", date: newsItem.created)}"><g:formatDate format="dd MMM yyyy" date="${newsItem.created}" /></time>
              </g:if>
              <h3>
                <g:if test="${frontPage.useGlobalNewsItem == false}">
                  <g:link action="show" controller="newsItem" id="${newsItem.id}">${newsItem.title}</g:link>
                </g:if>
                <g:else>
                  ${newsItem.title}
                </g:else>
              </h3>
              <p>
                ${newsItem.shortDescription}
                <g:if test="${frontPage.useGlobalNewsItem == false}">
                  <g:link controller="newsItem" action="show" id="${newsItem.id}">Read more...</g:link>
                </g:if>
              </p>
            </article>
          </g:if>
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

        $(document).ready(function(e) {
          $.ajax("${createLink(controller: 'index', action:'leaderBoardFragment')}").done(function(content) {
            $("#leaderBoardSection").html(content);
          });

          $.ajax("${createLink(controller: 'index', action:'statsFragment')}").done(function(content) {
            $("#expedition-stats").html(content);
          });

        });


    </script>
  </body>
</html>