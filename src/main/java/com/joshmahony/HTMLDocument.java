package com.joshmahony;

import java.util.ArrayList;

/**
 * Created by josh on 25/02/14.
 */
public class HTMLDocument {

    /**
     * Original HTML line
     */
    public final String html;

    public HTMLLine[] htmlLines;

    /**
     * Takes the HTML line
     *
     * @param _html
     */
    public HTMLDocument(String _html) {

        html = _html.trim();

        htmlLines = getLines();

    }

    public HTMLLine[] getLines() {

        return getLines(html);

    }

    public HTMLLine[] getLines(String _html) {

        String lines[] = _html.split("\\r?\\n");

        HTMLLine[] htmlLines = new HTMLLine[lines.length];

        for(int i = 0; i < lines.length; i++) {

            htmlLines[i] = new HTMLLine(lines[i]);

        }

        return htmlLines;

    }

}
