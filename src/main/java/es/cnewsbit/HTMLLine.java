package es.cnewsbit;

import es.cnewsbit.extractors.Smoothable;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLLine implements Smoothable {

    private @Getter final String LINE;

    private @Getter int words = 0;

    /**
     * Count of tags on line
     */
    private @Getter int tags = 0;

    /**
     * The text to tag ratio of the line
     */
    private @Getter Double valueToSmooth = 0d;

    /**
     * The text to tag ratio after smoothing has occurred
     *
     */
    private @Getter @Setter Double smoothedValue = 0d;

    /**
     *
     * Constructor
     *
     * @param line the HTML line
     */
    public HTMLLine(String line) {

        this.LINE = line.trim();

        words = countWords();

        tags = countTags();

        valueToSmooth = textToTagRatio();

    }

    /**
     *
     * Gets the text of the line with no HTML tags
     *
     */
    public String getText() {

        return LINE.replaceAll("\\<.*?>", "");

    }

    private int countWords() {

        String strippedLine = LINE.replaceAll("\\<.*?>", "");

        if (strippedLine.equals("")) return 0;

        String[] words = strippedLine.split(" ");

        return words.length;

    }

    private int countTags() {

        Pattern pattern = Pattern.compile("<\\b[^>]*>");

        Matcher matcher = pattern.matcher(LINE);

        int i = 0;

        while(matcher.find()) {

            i++;

        }

        return i;

    }

    private double textToTagRatio() {

        double ratio = 0;

        if (words == 0) {

            ratio = 0;

        } else if (tags == 0) {

            ratio = 100;

        } else {

            ratio = (double) words / (double) tags;

        }

        return ratio;

    }

}
