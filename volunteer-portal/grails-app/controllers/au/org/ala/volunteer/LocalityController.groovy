package au.org.ala.volunteer

import org.springframework.web.multipart.MultipartFile
import grails.converters.JSON

class LocalityController {

    def localityService;
    def logService;

    def index = { }

    def load = {
        def collectionCodes = Project.createCriteria().list {
            isNotNull("featuredOwner")
            projections {
                distinct("featuredOwner")
            }
        }

        [collectionCodes: collectionCodes]
    }

    def loadCSV = {
        def collectionCode = params.collectionCode;
        MultipartFile f = request.getFile('csvfile')

        def results = localityService.importLocalities(collectionCode, f)

        flash.message = results.message

        render(view: 'load')
    }

    def searchFragment = {
        def taskInstance = Task.get(params.long("taskId"))
        def verbatimLocality = params["verbatimLocality"] as String
        [taskInstance: taskInstance, verbatimLocality: verbatimLocality]
    }

    def searchResultsFragment = {
        def taskInstance = Task.get(params.long("taskId"))
        if (taskInstance) {
            def q = params.searchLocality;
            def localities = localityService.findLocalities(q, taskInstance.project.featuredOwner, 500)
            return [taskInstance: taskInstance, localities: localities]
        }
    }

    def getLocalityJSON = {
        Locality locality = null;
        if (params.localityId) {
            locality = Locality.get(params.long("localityId"))
        } else if (params.externalLocalityId) {
            locality = Locality.findByExternalLocalityId(params.long('externalLocalityId'));
        }

        if (locality) {
            render(locality as JSON)
        } else {
            return(null as JSON)
        }
    }

}
