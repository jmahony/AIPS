package es.cnewsbit.utilities;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import es.cnewsbit.C;
import es.cnewsbit.HTMLDocument;
import es.cnewsbit.articles.NewsArticle;
import es.cnewsbit.exceptions.NotNewsArticleException;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by josh on 04/04/14.
 */
@Log4j2
public class NewsArticleFactory {

    private static String[] whitelist = new String[] {
            "http://uk.reuters.com/article/\\d{4}/\\d+/\\d+/.+",
            "http://www.bbc.co.uk/news/.+",
            "http://news.sky.com/story/.+"
    };

    /**
     *
     * Turns a DBObject into a NewsArticle
     *
     * @param dbObject the database object
     * @return the news article
     * @throws Exception
     */
    public static NewsArticle build(DBObject dbObject) throws NotNewsArticleException, MalformedURLException {

        URL url = new URL(dbObject.get("url").toString());

        if (!isInWhitelist(url))
            throw new NotNewsArticleException("Articles URL is not white listed");

        // Create a HTML document representation
        HTMLDocument htmlDocument = new HTMLDocument(
                getHTML(dbObject),
                C.SMOOTHING_KERNEL
        );

        NewsArticle newsArticle;

        try {

            // Attempt to dynamically instantiate the correct class
            Class instanceClass = getInstanceClass(url);

            Constructor<NewsArticle> con = instanceClass.getDeclaredConstructor(
                    HTMLDocument.class, URL.class);

            newsArticle = con.newInstance(htmlDocument, url);

        } catch (Exception e) {

            log.debug("Could not find class");

            // If we cant dynamically instantiate, just create the base article
            newsArticle = new NewsArticle(htmlDocument, url);

        }

        return newsArticle;

    }

    /**
     *
     * Extracts the newest HTML document from the crawled object.
     *
     * @param dbObject
     * @return
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
     * Gets the class to instantiate for a URL
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

    /**
     *
     * Returns true if the given URL is white listed, false if not.
     *
     * @param url the url
     * @return whether the url is white listed or not
     */
    private static boolean isInWhitelist(URL url) {

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
