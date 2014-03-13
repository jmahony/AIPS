package com.joshmahony;

import com.joshmahony.measures.RelevancyGenerator;
import com.joshmahony.utility.CSV;
import com.joshmahony.utility.ResourceLoader;
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

            //CSV.docToCSV(ResourceLoader.asString(this, "/external/documents/uk-scotland-independence-devolution-idUKBREA1H1JM20140218"));

            LinkedHashMap<Integer, String>[] relevanceTable = CSV.toTable(relevanceCSV);

            LinkedHashMap<Integer, Integer> relevance = new LinkedHashMap<>();

            // Normalise CSV
            for (int i = 0; i < relevanceTable.length; i++) {

                if (relevanceTable[i].get(1).equals("1")) {

                    relevance.put(i, Integer.parseInt(relevanceTable[i].get(1), 10));

                }

            }

            Map diff = new HashMap(relevance);

            diff.keySet().retainAll(results.keySet());

            int rr           = diff.size();
            int nrr          = results.size() - diff.size();
            int rnr          = relevance.size() - diff.size();
            double precision = rr / ((double) rr + (double) nrr) * 100;
            double recall    = rr / ((double) rr + (double) rnr) * 100;

            double fm        = 2 * (precision * recall) / (precision + recall);

            System.out.println("Relevant Returned     : " + rr);
            System.out.println("Not Relevant Returned : " + nrr);
            System.out.println("Relevant Not Returned : " + rnr);
            System.out.println("Precision             : " + precision);
            System.out.println("Recall                : " + recall);
            System.out.println("F Measure             : " + fm);

        } catch (Exception e) {
            
            e.printStackTrace();
            
        }



    }


}
