package es.cnewsbit;

import com.mongodb.*;
import lombok.extern.log4j.Log4j2;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Used to retrieve documents from the HTML store
 */
@Log4j2
public class HTMLStore {

    /**
     * MongoDB connection pool
     */
    private static MongoClient client;

    /**
     * Reference to the database
     */
    private static DB db;

    /**
     * This is a singleton, so keep one instance
     */
    private static HTMLStore instance = null;

    /**
     * How many document we have retrieved from monogodb
     */
    private static AtomicInteger count = new AtomicInteger(0);

    /**
     * Reference to the collection
     */
    private static DBCollection collection;

    /**
     * Whether the cursor has reached the end of the collection
     */
    public static boolean isEmpty = false;

    /**
     *
     * Constructor
     *
     */
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

    /**
     *
     * This is a singleton, so return an instance
     *
     * @return singleton instance
     */
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

        int c = count.getAndIncrement();

        int skip = c * batchSize;

        log.info("Fetching documents from " + (skip + 1) + " to " + (skip + batchSize));

        DBCursor cursor = collection.find().skip(skip);

        cursor.batchSize(batchSize);

        if (!cursor.hasNext()) {

            isEmpty = true;

            return null;

        }

        ArrayList<DBObject> list = new ArrayList<>();

        int i = 0;

        while(cursor.hasNext() && i++ < batchSize) {

            list.add(cursor.next());

        }

        log.info("Finished fetching documents from " + (skip + 1) + " to " + (skip + batchSize));

        cursor = null;

        return list;

    }

}
