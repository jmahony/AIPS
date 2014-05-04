package es.cnewsbit;

import com.mongodb.DBObject;
import es.cnewsbit.articles.NewsArticle;
import es.cnewsbit.exceptions.CollectionEmptyException;
import es.cnewsbit.indexers.Indexer;
import es.cnewsbit.utilities.NewsArticleFactory;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Carries out the actual processing of news articles
 */
@Log4j2
public class DocumentProcessorWorker implements Runnable {

    private static HTMLStore store;

    private static Indexer indexer;

    private static Database database;

    /**
     * How many articles the class has processed
     */
    private final @Getter static AtomicInteger NO_PROCESSED = new AtomicInteger(0);;

    /**
     * How many articles the thread should retrieve from the store at a time
     */
    private static int batchSize;

    /**
     * How many iterations are needed to process everything
     */
    private static int iterations;

    /**
     * How many instances of this class exist, we need to know this so we can
     * tell the DocumentProcessor when all workers as finished
     */
    private static AtomicInteger noOfWorkers = new AtomicInteger(0);

    private static AtomicInteger noOfWorkersFinished = new AtomicInteger(0);

    private static DocumentProcessor dp;

    /**
     *
     * Constructor
     *
     * @param dp the calling document processor
     * @param indexer the indexer to add terms to
     * @param database the database to store the articles to
     * @param noToProcess total number to process by this instance
     * @param batchSize how many batches to process in
     */
    public DocumentProcessorWorker(DocumentProcessor dp,
                                   Indexer indexer,
                                   Database database,
                                   HTMLStore store,
                                   int noToProcess,
                                   int batchSize) {

        if (DocumentProcessorWorker.store == null) {

            DocumentProcessorWorker.store = store;
            DocumentProcessorWorker.indexer = indexer;
            DocumentProcessorWorker.database = database;

            DocumentProcessorWorker.batchSize = batchSize;

            DocumentProcessorWorker.iterations = noToProcess / batchSize;

            DocumentProcessorWorker.dp = dp;

        }

        noOfWorkers.incrementAndGet();

    }

    @Override
    public void run() {

        for (int i = 0; i < iterations; i++) {

            try {

                List<DBObject> list = store.nextBatch(batchSize);

                log.info("Started Processing Articles");

                if (list == null)
                    throw new CollectionEmptyException("Collection is empty");

                for (DBObject object : list) {

                    try {

                        log.debug("Processing " + object.get("url"));

                        // Build a news article
                        NewsArticle na = NewsArticleFactory.build(object);

                        if (na != null) {

                            na.getDate();

                            indexer.addToIndex(na);

                            database.insert(na);

                            na = null;

                        }

                        // Even if no article is returned by the builder,
                        // We still need to keep track of how many articles have
                        // been taken from the collection
                        NO_PROCESSED.getAndIncrement();

                    } catch (StackOverflowError e) {

                        log.error("Stackoverflow: " + object.get("url"));

                    } catch (Exception e) {

                        log.debug(e.getMessage());

                    }

                }

                long articlesProcessedPerSecond = (NO_PROCESSED.get() /
                        TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis()
                                - dp.getSTART_TIME())));

                log.info("Finished Processing Articles, current rate is: " +
                        articlesProcessedPerSecond + " per second");

            } catch (CollectionEmptyException e) {

                log.debug(e);

            }

        }

        try {

            // If this is the last thread finishing, there may be articles left
            // over in the database batch, so send these to the database.
            database.getS().executeBatch();

        } catch (SQLException e) {

            log.info(e.getMessage());

        }

        // Register as done
        DocumentProcessorWorker.done();

    }

    /**
     *
     * Each thread calls this when it has finished processing.
     * When all threads have finished, the document processor will be notified
     * so it can close down properly.
     *
     */
    public static void done() {

        log.info("Done");

        noOfWorkersFinished.incrementAndGet();

        if(noOfWorkers.get() == noOfWorkersFinished.get()) {

            DocumentProcessorWorker.dp.workersDone();

        }

    }

}
