package com.joshmahony.measures;

import com.google.common.collect.Sets;
import org.omg.CORBA.DynAnyPackage.Invalid;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by josh on 02/03/14.
 */
public class Relevancy {

    /**
     * 
     * Calculates F Measure / Score of the given precision and recall
     * 
     * @param precision a percentage
     * @param recall a percentage
     * @return the f measure of the arguments
     */
    public static double fMeasure(double precision, double recall) {
        
        if (precision < 0 || recall < 0)
            throw new InvalidParameterException("Neither recall or precision can be less negative");
        
        if (precision == 0 && recall == 0)
            return 0.00d;
        
        if (precision > 100.00d) 
            throw new InvalidParameterException("Precision cant be over 100");

        if (recall > 100.00d)
            throw new InvalidParameterException("Recall cant be over 100");
        
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
     * @param <T>
     * @return The percentage precision of the two sets
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
     * @param <T>
     * @return The percentage recall of the two sets
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
