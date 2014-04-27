package es.cnewsbit.measures;

import es.cnewsbit.HTMLLine;
import es.cnewsbit.extractors.Smoothable;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Used to calculate how relevant a result set is, used to compare
 * different algorithms
 */
public class RelevancyGenerator {

    /**
     * A set of the lines that are relevant
     */
    private final Set<Integer> relevantLines;

    /**
     * The HTML lines to perform extraction on
     */
    private final Smoothable[] lines;

    /**
     * The results actually generated by the content extractor
     */
    private Map<Double, Map<String, Double>> results;

    /**
     *
     * Constructor
     *
     * @param relevantLines The rellevant set of results
     * @param lines The HTML lines
     */
    public RelevancyGenerator(Set<Integer> relevantLines, HTMLLine[] lines) {

        this.relevantLines = relevantLines;

        this.lines = lines;

    }

    /**
     *
     * Calculates the precision, recall and f measure when using the upper and
     * lower threshold on the given set of results.
     *
     * @param lower the lower threshold
     * @param upper the upper threshold
     */
    public void generate(double lower, double upper) {

        Smoothable[] smoothable = this.lines;

        results = new LinkedHashMap<>();

        for (double i = lower; i < upper; i++) {

            Set<Integer> returnedLines = new HashSet<>();

            for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {

                if (lines[lineNumber].getSmoothedValue() >= i) {

                    returnedLines.add(lineNumber);

                }

            }

            double precision = Relevancy.precision(relevantLines, returnedLines);
            double recall    = Relevancy.recall(relevantLines, returnedLines);
            double fm        = Relevancy.fMeasure(precision, recall);

            Map<String, Double> row = new LinkedHashMap<>();

            row.put("precision", precision);
            row.put("recall",    recall);
            row.put("fm",        fm);

            results.put(i, row);

        }

    }

    /**
     *
     * Prints the results to the console.
     *
     */
    public void printResults() {

        for (Map.Entry<Double, Map<String, Double>> entry : results.entrySet()) {

            System.out.print(entry.getKey() + ", ");

            for (Map.Entry<String, Double> entry2 : entry.getValue().entrySet()) {

                System.out.print(entry2.getValue() + ", ");

            }

            System.out.println();

        }

    }

}
