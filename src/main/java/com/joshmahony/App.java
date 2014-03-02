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

            String html = ResourceLoader.asString(this, "/external/documents/blogs-ouch-26193704");
            String relevanceCSV = ResourceLoader.asString(this, "/local/relevance/blogs-ouch-26193704.csv");
            
            HTMLDocument doc = new HTMLDocument(html, new double[] {0.25, 0.5, 0.25});

            Set<Integer> relevancySet = CSV.toRelevancySet(relevanceCSV, 0, 1);

            RelevancyGenerator rg = new RelevancyGenerator(relevancySet, doc);
            
            rg.generate(1, 50);

        } catch (Exception e) {
            
            e.printStackTrace();
            
        }



    }


}
