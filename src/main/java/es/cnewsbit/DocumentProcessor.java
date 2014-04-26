package es.cnewsbit;

import com.mongodb.DBObject;
import es.cnewsbit.exceptions.CollectionEmptyException;
import es.cnewsbit.articles.NewsArticle;
import es.cnewsbit.indexers.Indexer;
import es.cnewsbit.indexers.LuceneIndexer;
import es.cnewsbit.utilities.NewsArticleFactory;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by josh on 10/04/14.
 */
@Log4j2
public class DocumentProcessor {

    /**
     * How many documents have been processed in this instance
     */
    private final AtomicInteger NO_PROCESSED;

    /**
     * The database
     */
    private final Database DATABASE;

    /**
     * The html to process
     */
    private final HTMLStore STORE;

    /**
     * Used to build the index
     */
    private final Indexer INDEXER;

    /**
     * When we started processing, so we can calculate how many articles
     * we are processing per second
     */
    private @Getter final long START_TIME;

    public DocumentProcessor(Database database) throws IOException, SQLException {

        this.NO_PROCESSED = new AtomicInteger(0);

        this.DATABASE = database;

        this.STORE = HTMLStore.getInstance();

        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);

        this.INDEXER = new LuceneIndexer(C.PATH_TO_INDEX, analyzer);

        this.START_TIME = System.currentTimeMillis();

    }


    /**
     *
     * We are rebuilding the index, so clear the DB table before
     * starting processing
     *
     * @throws SQLException if the sql is invalid
     */
    public void rebuild() throws SQLException {

        Connection c = DATABASE.getDataSource().getConnection();

        Statement s = c.createStatement();

        s.execute("TRUNCATE article");

        c.close();

        process();

    }

    /**
     *
     * Spawn a number of workers
     *
     */
    public void process() {

        for(int i = 0; i < C.NO_OF_PROCESSORS; i++) {

            new Thread(new DocumentProcessorWorker(
                this,
                INDEXER,
                DATABASE,
                    NO_PROCESSED,
                20000,
                250
             )).start();

        }

    }

    /**
     *
     * Closes down the indexer when all document processor workers are finished
     *
     */
    public void workersDone() {

        log.info("Shutting down indexer");

        try {

            INDEXER.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

        log.info("Processing batch complete");

    }

}

/**
 * Carries out the actual processing of news articles
 */
@Log4j2
class DocumentProcessorWorker implements Runnable {

    /**
     * Reference to the HTML store
     */
    private static HTMLStore store;

    /**
     * Reference to the indexer
     */
    private static Indexer indexer;

    /**
     * Reference to the database
     */
    private static Database database;

    /**
     * How many the class has processed
     */
    private static AtomicInteger noProcessed;

    /**
     * How many articles the thread should retreive from the store at a time
     */
    private static int batchSize;

    /**
     * How many iterations are needed to process everything
     */
    private static int iterations;

    /**
     * How many instances of this class
     */
    private static AtomicInteger noOfWorkers = new AtomicInteger(0);

    /**
     * How many instances of this class have finished their work
     */
    private static AtomicInteger noOfWorkersFinished = new AtomicInteger(0);

    /**
     * Reference to the document processor
     */
    private static DocumentProcessor dp;

    /**
     *
     * Constructor
     *
     * @param dp the calling document processor
     * @param indexer the indexer to add terms to
     * @param database the database to store the articles to
     * @param noProcessed to keep track of how many have been processed
     * @param noToProcess total number to process by this instance
     * @param batchSize how many batches to process in
     */
    public DocumentProcessorWorker(DocumentProcessor dp,
                                   Indexer indexer,
                                   Database database,
                                   AtomicInteger noProcessed,
                                   int noToProcess,
                                   int batchSize) {

        if (DocumentProcessorWorker.store == null) {

            DocumentProcessorWorker.store = HTMLStore.getInstance();
            DocumentProcessorWorker.indexer = indexer;
            DocumentProcessorWorker.database = database;
            DocumentProcessorWorker.noProcessed = noProcessed;

            DocumentProcessorWorker.batchSize = batchSize;

            DocumentProcessorWorker.iterations = noToProcess / batchSize;

            DocumentProcessorWorker.dp = dp;

        }

        noOfWorkers.incrementAndGet();

    }

    /**
     * Starts the thread
     */
    @Override
    public void run() {

        for (int i = 0; i < iterations; i++) {

            try {

                // Get a batch of document to process
                List<DBObject> list = store.nextBatch(batchSize);

                log.info("Started Processing Articles");

                // If the batch is empty, throw an exception
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
                        // We still need to keep track of how many have been
                        // taken from the collection
                        noProcessed.getAndIncrement();

                    } catch (StackOverflowError e) {

                        log.error("Stackoverflow: " + object.get("url"));

                    } catch (Exception e) {

                        log.debug(e.getMessage());

                    }

                }

                // Calculate the rate we are processing articles
                long rate = (noProcessed.get() / TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - dp.getSTART_TIME())));

                log.info("Finished Processing Articles, current rate is: " + rate + " per second");

            } catch (CollectionEmptyException e) {

                log.debug(e);

            }

        }

        try {

            // If this is the last thread finishing, there may be articles left over
            // in the database batch, so send these to the database
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
     * so it can close down properly
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
