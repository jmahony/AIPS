package com.joshmahony;

import au.com.bytecode.opencsv.CSVWriter;
import com.joshmahony.utility.CSV;
import com.joshmahony.utility.ResourceLoader;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

            String html = ResourceLoader.asString(this, "/documents/blogs-ouch-26193704");

            LinkedHashMap<Integer, Integer> results = new LinkedHashMap<>();

            HTMLDocument doc = new HTMLDocument(html, new double[] {0.25, 0.5, 0.25});

            for (int i = 0; i < doc.htmlBodyLines.length; i++) {

                if (doc.htmlBodyLines[i].smoothedtTextTagRatio > 2) {

                    results.put(i, 1);

                }

            }

            String relevanceCSV = ResourceLoader.asString(this, "/relevance/blogs-ouch-26193704.csv");

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
