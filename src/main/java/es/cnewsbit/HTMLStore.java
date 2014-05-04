package es.cnewsbit;

import com.mongodb.*;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class HTMLStore {

    private static MongoClient client;

    private static DB db;

    private static HTMLStore instance = null;

    private static AtomicInteger retrievedDocumentCount = new AtomicInteger(0);

    private static DBCollection collection;

    private static @Getter boolean cursorHasReachedEnd = false;

    protected HTMLStore() {

        log.info("Initialising MongoDB connection... ");

        try {

            HTMLStore.client = new MongoClient("localhost", 27017);

        } catch (UnknownHostException e) {

            log.fatal(e.getMessage());

            System.exit(-1);

        }

        log.info("Obtaining MongoDB connection... ");

        db = client.getDB("bigc");

        collection = db.getCollection("htmlStore");


    }

    public static HTMLStore getInstance() {

        if (instance == null) {

            instance = new HTMLStore();

        }

        return instance;

    }

    /**
     *
     * Returns a set of db objects from the database
     *
     * @param batchSize the amount of db objects to fetch
     * @return a list of db objects
     */
    public synchronized List<DBObject> nextBatch(int batchSize) {

        int c = retrievedDocumentCount.getAndIncrement();

        int skip = c * batchSize;

        log.info("Fetching documents from " + (skip + 1) + " to " + (skip + batchSize));

        DBCursor cursor = collection.find().skip(skip);

        cursor.batchSize(batchSize);

        if (!cursor.hasNext()) {

            cursorHasReachedEnd = true;

            return null;

        }

        ArrayList<DBObject> list = new ArrayList<DBObject>();

        int i = 0;

        while(cursor.hasNext() && i++ < batchSize) {

            list.add(cursor.next());

        }

        log.info("Finished fetching documents from " + (skip + 1) + " to " + (skip + batchSize));

        cursor = null;

        return list;

    }

}
