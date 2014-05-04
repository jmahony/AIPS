package es.cnewsbit.utilities;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import es.cnewsbit.C;
import es.cnewsbit.HTMLDocument;
import es.cnewsbit.articles.NewsArticle;
import es.cnewsbit.exceptions.NotNewsArticleException;
import es.cnewsbit.extractors.BoilerpipeContentExtractor;
import es.cnewsbit.extractors.EXTRACTOR_TYPES;
import es.cnewsbit.extractors.TTRContentExtractor;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@Log4j2
public class NewsArticleFactory {

    /**
     *
     * If a URL pattern exists here, it will be indexed, otherwise ignored
     *
     */
    private static String[] whitelist = new String[] {
            "http://uk.reuters.com/article/\\d{4}/\\d+/\\d+/.+",
            "http://www.bbc.co.uk/news/.+",
            "http://news.sky.com/story/.+",
            "http://www.nbcnews.com/.+"
    };

    /**
     *
     * Takes a MongoDB object and generates the correct object for the domain
     * based on the url field. It then sets the content extractor to use
     * TODO: This needs to be refactored, see todo in method
     *
     * @param dbObject the MongoDB object
     * @return the news article
     * @throws NotNewsArticleException if the document is not white listed
     * @throws MalformedURLException if the URL is malformed
     * @throws ClassNotFoundException if a news article parser class does not exist
     */
    @SuppressWarnings("unchecked")
    public static NewsArticle build(DBObject dbObject) throws
            NotNewsArticleException, MalformedURLException,
            ClassNotFoundException {

        URL url = new URL(dbObject.get("url").toString());

        if (!isURLWhitelisted(url))
            throw new NotNewsArticleException("Articles URL is not white listed");

        // Create a HTML document representation
        HTMLDocument htmlDocument = new HTMLDocument(getHTML(dbObject));

        NewsArticle newsArticle = null;

        try {

            // Attempt to dynamically instantiate the correct class
            Class instanceClass = getInstanceClass(url);

            Constructor<NewsArticle> con = instanceClass.getDeclaredConstructor(
                    HTMLDocument.class,
                    URL.class
            );

            newsArticle = con.newInstance(htmlDocument, url);

            // TODO: See which extractor works best on which site and switch
            // TODO: based on the best.
            EXTRACTOR_TYPES type = EXTRACTOR_TYPES.BOILER_PIPE;

            switch(type) {
                case BOILER_PIPE:
                    newsArticle.setContentExtractor(new BoilerpipeContentExtractor());
                    break;
                case TEXT_TAG_RATIO:
                    newsArticle.setContentExtractor(new TTRContentExtractor());
                    break;
                default:
                    newsArticle.setContentExtractor(new TTRContentExtractor());
            }

        } catch (InvocationTargetException
                | NoSuchMethodException
                | InstantiationException
                | IllegalAccessException e) {

            log.debug(e.getMessage());

        }

        return newsArticle;

    }

    /**
     *
     * Extracts the newest HTML document from the crawled object.
     *     *
     * @param dbObject the MongoDB object
     * @return the HTML string
     */
    private static String getHTML(DBObject dbObject) {

        BasicDBList docs = (BasicDBList) dbObject.get("html");

        String html = null;

        if(!docs.isEmpty()) {

            BasicDBObject object = (BasicDBObject) docs.get(0);


            for (Map.Entry<String, Object> entry : object.entrySet()) {

                html = entry.getValue().toString();

                break;

            }

        }

        return html;

    }

    /**
     *
     * Gets the class to instantiate for based on the URLs domain
     *
     * @param url the url
     * @return the class
     * @throws ClassNotFoundException
     */
    private static Class getInstanceClass(URL url) throws ClassNotFoundException {

        // Get the class name we are going to instantiate
        String className ="es.cnewsbit.articles." +
                C.ARTICLE_CLASS_MAP.get(url.getHost());

        Class c = Class.forName(className);

        return c;

    }

    private static boolean isURLWhitelisted(URL url) {

        boolean whitelisted = false;

        for (String regex : whitelist) {

            if (url.toString().matches(regex)) {

                whitelisted = true;

                break;
            }

        }

        return whitelisted;

    }

}
