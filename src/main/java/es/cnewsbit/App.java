package es.cnewsbit;

import com.googlecode.flyway.core.Flyway;
import es.cnewsbit.queriers.LuceneQuerier;
import lombok.extern.log4j.Log4j2;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.sql.SQLException;
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

        // Perform database migrations
        Flyway flyway = new Flyway();
        flyway.setDataSource(C.DB_NAME, C.DB_USER, C.DB_PASSWORD);
        flyway.migrate();


        try {

            DocumentProcessor dp = new DocumentProcessor();

            dp.rebuild();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (SQLException e) {

            e.printStackTrace();

        }

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
