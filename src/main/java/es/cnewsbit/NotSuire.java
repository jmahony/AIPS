package es.cnewsbit;

import com.mongodb.*;
import es.cnewsbit.utilities.NewsArticleFactory;
import lombok.extern.log4j.Log4j2;

import java.net.UnknownHostException;

/**
 * Created by josh on 03/04/14.
 */
@Log4j2
public class NotSuire {
    public static void test() {

        log.info("Initialising MongoDB connection connection... ");

        try {

            MongoClient client = new MongoClient("localhost", 27017);

            DB db = client.getDB("bigc");

            DBCollection collection = db.getCollection("htmlStore");

            DBCursor cursor = collection.find();

            int i = 0;

            while(cursor.hasNext()) {

                DBObject document = cursor.next();

                log.debug("Processing " + document.get("url"));

                try {

                    NewsArticle na = NewsArticleFactory.buildNewsArticle(document);

                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Failed to process document");

                }

                if (i++ == 1000) {

                    break;

                }

            }

        } catch (UnknownHostException e) {

            log.fatal("Could not connect to MongoDB server");

            System.exit(-1);

        }

    }
}
