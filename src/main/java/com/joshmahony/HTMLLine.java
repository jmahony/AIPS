package com.joshmahony;

/**
 * Created by joshmahony on 24/02/2014.
 */
public class HTMLLine {

    /**
     * Original HTML line
     */
    public final String line;

    /**
     * Count of text on line
     */
    public int textCount = 0;

    /**
     * Count of tags on line
     */
    public int tagsCount = 0;

    /**
     * The text to tag ratio of the line
     */
    public double textTagRatio = 0;

    /**
     *
     * Takes the HTML line
     *
     * @param _line
     */
    public HTMLLine(String _line) {

        line = _line;

    }

    /**
     *
     * Count text on the line
     *
     * @return amount of text
     */
    private int countText() {

        return 0;

    }

    /**
     *
     * Count tags on the line
     *
     * @return amount of tags
     */
    private int countTags() {

        return 0;

    }


    /**
     *
     * Gets the text to tags ratio
     *
     * @return
     */
    private double textToTagRatio() {

        return 0d;

    }

}
