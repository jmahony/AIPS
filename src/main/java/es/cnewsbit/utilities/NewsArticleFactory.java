package es.cnewsbit.utilities;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import es.cnewsbit.C;
import es.cnewsbit.HTMLDocument;
import es.cnewsbit.NewsArticle;

import java.net.URL;
import java.sql.ResultSet;
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

    public static NewsArticle build(ResultSet rs) throws Exception {

        HTMLDocument htmlDocument = new HTMLDocument(
                rs.getString("html"),
                C.SMOOTHING_KERNEL
        );

        URL url = new URL(rs.getString("url"));

        NewsArticle newsArticle = new NewsArticle(htmlDocument, url);

        return newsArticle;


    }

}
