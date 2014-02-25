package com.joshmahony;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private String getBody() {

        return HTMLDocument.getBody(html);

    }

    /**
     *
     * Returns the HTML between <body> & </body>
     *
     * @param _html
     * @return
     */
    public static String getBody(String _html) {

        // Get the index position of the last character of the <body> tag,
        // There could be multiple opening body tags, we are looking for the first
        // character of the first occurrence.
        Pattern openingBody = Pattern.compile("<body.*>");

        Matcher openingBodyMatcher = openingBody.matcher(_html);

        int firstPos = 0,
            lastPos  = _html.length();

        while(openingBodyMatcher.find()) {

            if (openingBodyMatcher.end() > firstPos) {

                firstPos = openingBodyMatcher.end();

            }

        }

        // Get the index position of the first character of the </body> tag
        Pattern closingBody = Pattern.compile("</body>");

        Matcher closingBodyMatcher = closingBody.matcher(_html);

        while(closingBodyMatcher.find()) {

            if (closingBodyMatcher.start() < lastPos) {

                lastPos = closingBodyMatcher.start();

            }

        }

        // Return the string between these two indices, and trim leading and trailing whitespace
        return _html.substring(firstPos, lastPos).trim();

    }

    public HTMLLine[] getLines() {

        return HTMLDocument.getLines(html);

    }

    public static HTMLLine[] getLines(String _html) {

        String lines[] = _html.split("\\r?\\n");

        HTMLLine[] htmlLines = new HTMLLine[lines.length];

        for(int i = 0; i < lines.length; i++) {

            htmlLines[i] = new HTMLLine(lines[i]);

        }

        return htmlLines;

    }

}
