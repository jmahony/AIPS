package com.cnewsbit;

import lombok.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cnewsbit on 24/02/2014.
 */
public class HTMLLine {

    /**
     * Original HTML line
     */
    private @Getter final String line;

    /**
     * Count of text on line
     */
    private @Getter int textCount = 0;

    /**
     * Count of tags on line
     */
    private @Getter int tagsCount = 0;

    /**
     * The text to tag ratio of the line
     */
    private @Getter double textTagRatio = 0;

    /**
     * The text to tag ratio after smoothing has occurred
     *
     */
    private @Getter @Setter double smoothedTextTagRatio = 0;

    /**
     *
     * Takes the HTML line
     *
     * @param _line
     */
    public HTMLLine(String _line) {

        line = _line.trim();

        textCount = countText();

        tagsCount = countTags();

        textTagRatio = textToTagRatio();

    }

    /**
     *
     * Count text on the line
     *
     * @return amount of text
     */
    private int countText() {

        String strippedLine = line.replaceAll("\\<.*?>", "");

        if (strippedLine.equals("")) return 0;

        String[] words = strippedLine.split(" ");

        return words.length;

    }

    /**
     *
     * Count tags on the line
     *
     * @return amount of tags
     */
    private int countTags() {

        Pattern pattern = Pattern.compile("<\\b[^>]*>");

        Matcher matcher = pattern.matcher(line);

        int i = 0;

        while(matcher.find()) {

            i++;

        }

        return i;

    }


    /**
     *
     * Gets the text to tags ratio
     *
     * @return text to tag ratio
     */
    private double textToTagRatio() {

        double ratio = 0;

        if (textCount == 0) {
            ratio = 0;
        } else if (tagsCount == 0) {
            ratio = 100;
        } else {
            ratio = (double) textCount / (double) tagsCount;
        }

        return ratio;

    }

}
