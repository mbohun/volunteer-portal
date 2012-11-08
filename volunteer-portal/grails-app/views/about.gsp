<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
  <head>
      <title>Volunteer Portal - Atlas of Living Australia</title>
      <meta name="layout" content="${grailsApplication.config.ala.skin}"/>
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
  <body class="sublevel sub-site volunteerportal">

    <cl:navbar selected="aboutbvp" />

    <header id="page-header">      
      <div class="inner">
        <cl:messages />
        <nav id="breadcrumb">
          <ol>
            <li><a href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
            <li class="last"><g:message code="default.about.label" default="About the Volunteer Portal" /></li>
          </ol>
        </nav>
        <h1>About the Volunteer Portal</h1>
      </div>
    </header>
    <div>
      <div class="inner">
        <p style="font-size: 1.2em">
        The <a href="http://www.ala.org.au">Atlas of Living Australia</a>, in collaboration with the <a class="external" href="http://www.australianmuseum.net.au">Australian Museum</a>, developed the Biodiversity Volunteer Portal to harness the power of online volunteers (also known as crowdsourcing) to digitise biodiversity data that is locked up in biodiversity collections, field notebooks and survey sheets.
        </p>

        <H2>Why capture this data?</H2>

        <span>This data has many uses, including:</span>
        <ul>
          <li>understanding the relationships between species (important in determining potential agricultural pests or potential medical applications);</li>
          <li>the distribution of species (for understanding how best to conserve individual species or ecosystems);</li>
          <li>identification of species from morphological or genetic characters (for example being able to identify birds involved in aircraft incidents).</li>
        </ul>

        <p>
        By helping us capture this information into digital form you are helping scientists and planners better understand, utilise, manage and conserve our precious biodiversity.
        </p>

        <span>
        This data, once captured, becomes available through a broad range of mechanisms that make it accessible to the scientific and broader communities.  These mechanisms include websites such as :
        </span>
        <ul>
          <li><a class="external" href="http://www.australianmuseum.net.au/research-and-collections">Individual institutions collections and associated databases</a></li>
          <li>The <a class="external" href="http://www.ala.org.au">Atlas of Living Australia</a>
          <li>The <a class="external" href="http://www.gbif.org/">Global Biodiversity Information Facility</a></li>
        </ul>

        <H2>Some useful references:</H2>
          <ul>
            <li><a class="external" href="http://www.ncbi.nlm.nih.gov/pmc/articles/PMC1693343/pdf/15253354.pdf">Biodiversity informatics: managing and applying primary biodiversity data</a></li>
            <li><a class="external" href="http://www.youtube.com/watch?v=x9404is3RJ8">Video showing how data is shared and what it is used for</a></li>
          </ul>
      </div>
    </div>
  </body>
</html>
