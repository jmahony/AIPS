package es.cnewsbit;

import es.cnewsbit.indexers.Indexer;
import es.cnewsbit.indexers.LuceneIndexer;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by josh on 10/04/14.
 */
@Log4j2
public class DocumentProcessor {

    private final Database DATABASE;

    private final Indexer INDEXER;

    /**
     * When we started processing, so we can calculate how many articles
     * we are processing per second
     */
    private @Getter final long START_TIME;

    public DocumentProcessor(Database database) throws IOException, SQLException {

        this.DATABASE = database;

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
    public void rebuild(HTMLStore store) throws SQLException {

        Connection c = DATABASE.getDataSource().getConnection();

        Statement s = c.createStatement();

        s.execute("TRUNCATE article");

        c.close();

        process(store);

    }

    /**
     *
     * Spawn a number of workers
     *
     */
    public void process(HTMLStore store) {

        for(int i = 0; i < C.NO_OF_PROCESSORS; i++) {

            new Thread(new DocumentProcessorWorker(
                this,
                INDEXER,
                DATABASE,
                store,
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
