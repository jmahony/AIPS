package es.cnewsbit.utilities;

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

            if (row.length() <= 0) continue;

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

        Set<Integer> relevancySet = new HashSet<Integer>();

        for (Map<Integer, String> row : table) {

            if (row.get(relevancyColumn).equals("1")) {

                relevancySet.add(Integer.parseInt(row.get(lineColumn), 10));

            }

        }

        return relevancySet;

    }

}
