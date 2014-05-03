package es.cnewsbit.utilities;

import es.cnewsbit.HTMLLine;

/**
 * Produces HTMLLine objects from HTML lines
 */
public class HTMLLineFactory {

    /**
     *
     * Separate out the given HTML into lines
     *
     * @param html the html to turn into an array of lines
     * @return array of HTML lines
     */
    public static HTMLLine[] build(String html) {

        String lines[] = html.split("\\r?\\n");

        HTMLLine[] htmlLines = new HTMLLine[lines.length];

        for(int i = 0; i < lines.length; i++) {

            htmlLines[i] = new HTMLLine(lines[i]);

        }

        return htmlLines;

    }

}
