package es.cnewsbit.utilities;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import es.cnewsbit.C;
import es.cnewsbit.HTMLDocument;
import es.cnewsbit.NewsArticle;
import es.cnewsbit.exceptions.NotNewsArticleException;

import java.net.URL;
import java.util.Map;

/**
 * Created by josh on 04/04/14.
 */
public class NewsArticleFactory {

    /**
     *
     * Turns a DBObject into a NewsArticle
     *
     * @param dbObject the database object
     * @return the news article
     * @throws Exception
     */
    public static NewsArticle build(DBObject dbObject) throws Exception {

        if(!isNewsArticle(dbObject))
            throw new NotNewsArticleException("Document is not a whitelisted");

        // Get the HTML list
        BasicDBList docs = (BasicDBList) dbObject.get("html");

        if(!docs.isEmpty()) {

            BasicDBObject object = (BasicDBObject) docs.get(0);

            String html = null;

            for (Map.Entry<String, Object> entry : object.entrySet()) {

                html = entry.getValue().toString();

                break;

            }

            HTMLDocument htmlDocument = new HTMLDocument(
                    html,
                    C.SMOOTHING_KERNEL
            );

            URL url = new URL(dbObject.get("url").toString());

            NewsArticle newsArticle = new NewsArticle(htmlDocument, url);

            return newsArticle;

        }

        return null;

    }

    private static String[] whitelist = new String[] {
        "http://uk.reuters.com/article/\\d{4}/\\d+/\\d+/.+"
    };

    public static boolean isNewsArticle(DBObject dbObject) {

        String url = dbObject.get("url").toString();

        for (String regex : whitelist) {

            if (url.matches(regex)) return true;

        }

        return false;

    }

}
