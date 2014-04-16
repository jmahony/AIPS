package es.cnewsbit.utilities;

import au.com.bytecode.opencsv.CSVWriter;
import es.cnewsbit.HTMLDocument;
import es.cnewsbit.HTMLLine;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Bunch of utility functions for handling CSV files
 */
public class CSV {

    /**
     *
     * Turns a CSV string into a hashmap table
     *
     * @param csvRaw the csv string
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
     * Formats a CSV string so we can perform relevant measure on it
     *
     * @param csv the CSV string
     * @param lineColumn the column the line number is in
     * @param relevancyColumn the column the relevancy bit is in
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

        CSVWriter writer = null;

        try {

            HTMLDocument doc = new HTMLDocument(html, new double[] {0.25, 0.5, 0.25});

            int i = 0;

            for (HTMLLine line : doc.getHtmlBodyLines()) {

                System.out.println(i++ + ", " + line.getLine());

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
