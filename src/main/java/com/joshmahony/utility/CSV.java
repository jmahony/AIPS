package com.joshmahony.utility;

import au.com.bytecode.opencsv.CSVWriter;
import com.joshmahony.HTMLDocument;
import com.joshmahony.HTMLLine;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by josh on 02/03/14.
 */
public class CSV {

    /**
     *
     * Returns a csv file as a hashmap
     *
     * @param csvRaw
     * @return a hasmap of 
     */
    public static LinkedHashMap[] toTable(String csvRaw) {

        String csv = csvRaw.replaceAll("(\r|\n|\r\n)", "\n");

        String[] rows = csv.split("\n");

        LinkedHashMap<Integer, String>[] table = new LinkedHashMap[rows.length];

        int rowIndex = 0;

        for (String row : rows) {

            String[] columns = row.split(",");

            LinkedHashMap<Integer, String> newRow = new LinkedHashMap<Integer, String>();

            for (int i = 0; i < columns.length; i++) {

                newRow.put(i, columns[i].trim());

            }

            table[rowIndex++] = newRow;

        }

        return table;

    }

    /**
     * 
     * @param csv
     * @param lineColumn
     * @param relevancyColumn
     * @return a set of integer that relate to lines that have been marked as relevant
     */
    public static Set<Integer> toRelevancySet(String csv, int lineColumn, int relevancyColumn) {
        
        Map<Integer, String>[] table = toTable(csv);
        
        Set<Integer> relevancySet = new HashSet<>();
        
        for (Map<Integer, String> row : table) {
            
            if (row.get(relevancyColumn).equals("1")) {
                
                relevancySet.add(Integer.parseInt(row.get(lineColumn), 10));
                
            }
            
        }
        
        return relevancySet;
        
    }

    public static void docToCSV(String html) {

        try {

            HTMLDocument doc = new HTMLDocument(html, new double[] {0.25, 0.5, 0.25});

            int i = 0;

            for (HTMLLine line : doc.htmlBodyLines) {

                System.out.println(i++ + ", " + line.line);

            }

        } catch (IOException e) {

            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
    
}
