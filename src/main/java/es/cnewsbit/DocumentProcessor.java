package es.cnewsbit;

import com.mongodb.DBObject;
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
    private final AtomicInteger noProcessedInBatch;


    /**
     * The database
     */
    private final Database database;

    /**
     * The html to process
     */
    private final HTMLStore store;

    /**
     *
     */
    private final Indexer indexer;

    private @Getter
    final long processStartTimeMillis;

    public DocumentProcessor() throws IOException, SQLException {

        this.noProcessedInBatch = new AtomicInteger(0);

        this.database = new Database(C.DB_USER, C.DB_PASSWORD, C.DB_NAME, 4);

        this.store = HTMLStore.getInstance();

        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);

        this.indexer = new LuceneIndexer(C.PATH_TO_INDEX, analyzer);

        this.processStartTimeMillis = System.currentTimeMillis();

    }

    /**
     * This will clear the article database
     */
    public void rebuild() throws SQLException {

        Connection c = database.getDataSource().getConnection();

        Statement s = c.createStatement();

        s.execute("TRUNCATE article");

        c.close();

        process();

    }

    public void process() {

        int noWorkers = 4;

        for(int i = 0; i < noWorkers; i++) {

            new Thread(new DocumentProcessorWorker(
                this,
                store,
                indexer,
                database,
                noProcessedInBatch,
                1000,
                250
             )).start();

        }

    }

    public void workersDone() {

        log.info("Shutting down indexed");

        try {
            indexer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("Processing batch complete");

    }

}

@Log4j2
class DocumentProcessorWorker implements Runnable {

    private static HTMLStore store;
    private static Indexer indexer;
    private static Database database;
    private static AtomicInteger noProcessed;
    private static int batchSize;
    private static int iterations;
    private static AtomicInteger noOfWorkers = new AtomicInteger(0);
    private static AtomicInteger noOfWorkersFinished = new AtomicInteger(0);
    private static DocumentProcessor dp;

    public DocumentProcessorWorker(DocumentProcessor dp, HTMLStore store, Indexer indexer,
                                   Database database, AtomicInteger noProcessed,
                                   int noToProcess,
                                   int batchSize) {

        if (DocumentProcessorWorker.store == null) {

            DocumentProcessorWorker.store = store;
            DocumentProcessorWorker.indexer = indexer;
            DocumentProcessorWorker.database = database;
            DocumentProcessorWorker.noProcessed = noProcessed;

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

                if (list == null) throw new CollectionEmptyExeception("Collection is empty");

                for (DBObject object : list) {

                    try {

                        log.debug("Processing " + object.get("url"));

                        NewsArticle na = NewsArticleFactory.build(object);

                        indexer.addToIndex(na);

                        database.insert(na);

                        na = null;

                        noProcessed.getAndIncrement();

                    } catch (StackOverflowError e) {


                        log.error("Stackoverflow: " + object.get("url"));

                    }

                }

                long rate = (noProcessed.get() / TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - dp.getProcessStartTimeMillis())));

                log.info("Finished Processing Articles, current rate is: " + rate + " per second");

            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());

            }

        }

        try {
            database.getS().executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        noOfWorkersFinished.incrementAndGet();

        DocumentProcessorWorker.done();

    }

    public static void done() {

        log.info("Done");

        if(noOfWorkers.get() == noOfWorkersFinished.get()) {

            DocumentProcessorWorker.dp.workersDone();

        }

    }

}
