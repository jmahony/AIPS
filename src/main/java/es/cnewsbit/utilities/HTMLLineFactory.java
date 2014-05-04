package es.cnewsbit.utilities;

import es.cnewsbit.HTMLLine;

public class HTMLLineFactory {

    /**
     *
     * Turns HTML into an array of HTMLLine objects
     *
     * @param html the html
     * @return array of HTMLLines
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
