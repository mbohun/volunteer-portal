package au.org.ala.volunteer

class Project {

    String name
    String description
    String bannerImage
    String tutorialLinks
    Boolean showMap = true
    Date created
    String shortDescription
    String featuredLabel
    String featuredOwner
    Boolean disableNewsItems = false
    Integer leaderIconIndex = 0
    String featuredImageCopyright = null
    Boolean inactive = false
    String collectionEventLookupCollectionCode
    String localityLookupCollectionCode

    def grailsApplication

    static belongsTo = [template: Template]
    static hasMany = [tasks: Task, projectAssociations: ProjectAssociation, newsItems: NewsItem]

    static mapping = {
        version false
        tasks cascade: 'all,delete-orphan'
        projectAssociations cascade: 'all,delete-orphan'
        template lazy: false
        newsItems sort: 'created', order: 'desc', cascade: 'all,delete-orphan'
    }

    static constraints = {
        name maxSize: 200
        description nullable: true, maxSize: 3000, widget: 'textarea'
        template nullable: true
        created nullable: true
        bannerImage nullable: true
        showMap nullable: true
        tutorialLinks nullable: true, maxSize: 2000, widget: 'textarea'
        featuredImage nullable: true
        featuredLabel nullable: true
        featuredOwner nullable: true
        shortDescription nullable: true, maxSize: 500
        disableNewsItems nullable: true
        leaderIconIndex nullable: true
        featuredImageCopyright nullable: true
        inactive nullable: true
        collectionEventLookupCollectionCode nullable: true
        localityLookupCollectionCode nullable: true
    }

    public String toString() {
        return name
    }

    public String getFeaturedImage() {
        return "${grailsApplication.config.server.url}/${grailsApplication.config.images.urlPrefix}project/${id}/expedition-image.jpg"
    }

    public void setFeaturedImage(String image) {
        // do nothing
    }
}
