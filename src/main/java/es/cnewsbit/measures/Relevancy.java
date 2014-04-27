package es.cnewsbit.measures;

import com.google.common.collect.Sets;

import java.security.InvalidParameterException;
import java.util.Set;

/**
 * Created by josh on 02/03/14.
 */
public class Relevancy {

    /**
     *
     * Calculates F Measure / Score of the given precision and recall
     *
     * @param precision 0.00 to 100.00
     * @param recall 0.00 to 100.00
     * @throws InvalidParameterException precision or recall less than 0.00
     * @throws InvalidParameterException precision or recall greater than 100.00
     * @return 0.00 to 100.00
     */
    public static double fMeasure(double precision, double recall) throws InvalidParameterException {

        if (precision < 0.00d || recall < 0.00d)
            throw new InvalidParameterException("Neither recall or precision can be less negative");

        if (precision > 100.00d || recall > 100.00d)
            throw new InvalidParameterException("Precision cant be over 100");

        if (precision == 0.00d && recall == 0.00d)
            return 0.00d;

        return 2 * ((precision * recall) / (precision + recall));

    }

    /**
     *
     * Returns the recall of the two sets. This simply intersects a result set and a known relevant set and returns the
     * fraction of the number of relevant articles in the return set.
     *
     * rr  = the relevant elements returned
     * nrr = the elements that were not relevant but were returned
     *
     * precision = (rr / (rr + nrr)) * 100
     *
     * @param relevant The set of correct results
     * @param returned The set of returned results
     * @param <T> The type of the set
     * @return The percentage precision of the two sets (0.00 to 100.00)
     */
    public static <T> double precision(Set<T> relevant, Set<T> returned) {

        double precision = 0.00d;

        if (!relevant.isEmpty() && !returned.isEmpty()) {

            Set<T> returnedRelevant = Sets.intersection(relevant, returned);

            double returnedRelevantCount    = returnedRelevant.size();
            double notRelevantReturnedCount = returned.size() - returnedRelevant.size();

            precision = (returnedRelevantCount / (returnedRelevantCount + notRelevantReturnedCount)) * 100;

        }

        return precision;

    }

    /**
     *
     * Returns the recall of the two sets. This simply intersects a result set and a known relevant set and returns the
     * fraction of the number of relevant articles in the known relevant set.
     *
     * rr  = the relevant elements returned
     * rnr = the elements that are relevant but not returned
     *
     * precision = (rr / (rr + rnr)) * 100
     *
     * @param relevant The set of correct results
     * @param returned The set of returned results
     * @param <T> The type of the set
     * @return The percentage recall of the two sets (0.00 to 100.00)
     */
    public static <T> double recall(Set<T> relevant, Set<T> returned) {

        double recall = 0.00d;

        if (!relevant.isEmpty() && !returned.isEmpty()) {
            Set<T> returnedRelevant = Sets.intersection(relevant, returned);

            double returnedRelevantCount = returnedRelevant.size();
            double relevantNotReturned   = relevant.size() - returnedRelevant.size();

            recall = (returnedRelevantCount / (returnedRelevantCount + relevantNotReturned)) * 100;

        }

        return recall;

    }

}
