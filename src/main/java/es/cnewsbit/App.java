package es.cnewsbit;

import es.cnewsbit.measures.RelevancyGenerator;
import es.cnewsbit.utility.CSV;
import es.cnewsbit.utility.ResourceLoader;

import java.util.Set;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {

        new App();

    }

    public App() {

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



    }


}
