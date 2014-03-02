package com.joshmahony.utility;

import au.com.bytecode.opencsv.CSVWriter;
import com.joshmahony.HTMLDocument;

import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Created by josh on 02/03/14.
 */
public class CSV {

    /**
     *
     * Returns a csv file as a hashmap
     *
     * @param csv
     * @return a hasmap of 
     */
    public static LinkedHashMap[] toTable(String csv) {

        String[] rows = csv.split("\n");

        LinkedHashMap<Integer, String>[] table = new LinkedHashMap[rows.length];

        int rowIndex = 0;

        for (String row : rows) {

            String[] columns = row.split(",");

            LinkedHashMap<Integer, String> newRow = new LinkedHashMap<Integer, String>();

            for (int i = 0; i < columns.length; i++) {

                newRow.put(i, columns[i]);

            }

            table[rowIndex++] = newRow;

        }

        return table;

    }

    public void docToCSV(String path) {

        String html = ResourceLoader.asString(this, path);

        CSVWriter writer = null;

        try {

            HTMLDocument doc = new HTMLDocument(html, new double[] {0.25, 0.5, 0.25});

            System.out.println(doc.htmlBodyLines.length);

        } catch (IOException e) {

            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
    
}
