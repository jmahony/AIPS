package com.joshmahony.measures;

import com.joshmahony.HTMLDocument;
import com.joshmahony.HTMLLine;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by josh on 02/03/14.
 */
public class RelevancyGenerator {

    /**
     * 
     */
    private final Set<Integer> relevantLines;
    
    /**
     * 
     */
    private final HTMLDocument document;

    /**
     * 
     */
    private Map<String, Double>[] results;

    /**
     * 
     * @param _relevantLines
     * @param _document
     */
    public RelevancyGenerator(Set<Integer> _relevantLines, HTMLDocument _document) {
        
        relevantLines = _relevantLines;
        
        document = _document;

    }

    /**
     * 
     * @param start
     * @param end
     */
    public void generate(int start, int end) {
        
        HTMLLine[] lines = document.htmlBodyLines;

        results = new LinkedHashMap[end - start + 1];
        
        for (int i = start; i < end; i++) {
            
            Set<Integer> returnedLines = new HashSet<>();     

            for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {

                if (lines[lineNumber].smoothedtTextTagRatio >= i) {

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
            
            results[i] = row;
            
        }

    }
   
}
