package au.org.ala.volunteer

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class IndexController {

    def config = ConfigurationHolder.config

    def index = {
        def frontPage = FrontPage.instance()

        // News item
        NewsItem newsItem = null;

        if (frontPage.useGlobalNewsItem) {
            newsItem = new NewsItem(shortDescription: frontPage.newsBody, title: frontPage.newsTitle, created: frontPage.newsCreated);
        } else {
            newsItem = NewsItem.find("from NewsItem order by created desc")
        }

        // Stats
        def totalTasks = Task.count()
        def completedTasks = Task.findAll("from Task where length(fullyTranscribedBy) > 0").size()
        def transcriberCount = User.findAll("from User where transcribedCount > 0").size()
        def leaderBoard = User.findAll("from User order by transcribedCount desc", [],[max:config.leaderBoard.count]);
        while (leaderBoard.size() < config.leaderBoard.count) {
            leaderBoard.add(new User(displayName: "_", transcribedCount: 0))
        }
        render(view: "/index", model: ['newsItem' : newsItem, 'frontPage': FrontPage.instance(), 'totalTasks':totalTasks, 'completedTasks':completedTasks, 'transcriberCount':transcriberCount, 'leaderBoard':leaderBoard] )    }
}
