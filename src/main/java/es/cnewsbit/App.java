package es.cnewsbit;

import com.googlecode.flyway.core.Flyway;
import com.mongodb.DBObject;
import es.cnewsbit.indexers.Indexer;
import es.cnewsbit.indexers.LuceneIndexer;
import es.cnewsbit.queriers.LuceneQuerier;
import es.cnewsbit.utilities.NewsArticleFactory;
import lombok.extern.log4j.Log4j2;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 *
 */
@Log4j2
public class App {

    private static Analyzer ANALYSER;

    private static AtomicInteger count = new AtomicInteger(0);

    private static long startTime = System.currentTimeMillis();

    public static void main(String[] args) {

        Flyway flyway = new Flyway();
        flyway.setDataSource(C.DB_NAME, C.DB_USER, C.DB_PASSWORD);
        flyway.migrate();

        ANALYSER = new StandardAnalyzer(Version.LUCENE_47);

        createIndex();




        //queryIndex();

    }

    public static void queryIndex() {

        try {

            LuceneQuerier lq = new LuceneQuerier(C.PATH_TO_INDEX, ANALYSER);

            lq.query("rugby", 100);

        } catch (IOException e) {

            e.printStackTrace();

        } catch (ParseException e) {

            e.printStackTrace();

        }


    }

    public static void createIndex() {

        ExecutorService pool = Executors.newFixedThreadPool(4);

        HTMLStore store = HTMLStore.getInstance();

        try {

            final Indexer indexer = new LuceneIndexer(C.PATH_TO_INDEX, ANALYSER);

            Database db = new Database();

            while(store.hasNext()) {

                pool.execute(()-> {

                    try {

                        List<DBObject> list = store.nextBatch(250);

                        log.info("Started Processing Articles");

                        if (list == null) throw new CollectionEmptyExeception("Collection is empty");

                        for (DBObject object : list) {

                            try {

                                log.debug("Processing " + object.get("url"));

                                NewsArticle na = NewsArticleFactory.build(object);

                                indexer.addToIndex(na);

                                db.insert(na);

                                na = null;

                                count.getAndIncrement();

                            } catch (StackOverflowError e) {

                                log.error("Stackoverflow");

                            }

                        }

                        log.info("Finished Processing Articles, current rate is: " + (count.get() / TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - App.startTime))));

                    } catch (Exception e) {

                        log.error(e.getMessage());

                    }

                });

            }

            Thread.sleep(60000);

            indexer.close();

            System.exit(-1);

        } catch (Exception e) {

            log.fatal(e.getMessage());

            System.exit(-1);

        }

    }

    public App() {





        /**
        try {

            String html = ResourceLoader.asString(this, "/external/documents/uk-scotland-independence-devolution-idUKBREA1H1JM20140218");
            String relevanceCSV = ResourceLoader.asString(this, "/local/relevance/uk-scotland-independence-devolution-idUKBREA1H1JM20140218.csv");

            HTMLDocument doc = new HTMLDocument(html, new double[] {0.25, 0.5, 0.25});

            Set<Integer> relevancySet = CSV.toRelevancySet(relevanceCSV, 0, 1);

            RelevancyGenerator rg = new RelevancyGenerator(relevancySet, doc);

            rg.generate(1.00d, 50.00d);

            rg.printResults();

        } catch (Exception e) {

            e.printStackTrace();

        }
*/


    }


}
