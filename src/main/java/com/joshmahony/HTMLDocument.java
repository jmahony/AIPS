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

    public HTMLLine[] htmlBodyLines;


    /**
     * Takes the HTML line
     *
     * @param _html
     */
    public HTMLDocument(String _html) {

        html = _html.trim();

        htmlBodyLines = getLines(getBody());

    }

    /**
     *
     * Shortcut to strip the instances remarks
     *
     * @return
     */
    private String stripScripts() {

        return HTMLDocument.stripScripts(html);

    }

    /**
     *
     * Strip out HTML remarks
     *
     * @param _html
     * @return
     */
    public static String stripScripts(String _html) {

        // https://github.com/jquery/jquery/blob/1.7.2/src/ajax.js#L14
        String cleaned = _html.replaceAll("<script\\b[^<]*(?:(?!<\\/script>)<[^<]*)*<\\/script>", "");

        return cleaned.trim();

    }
    
    private String stripRemarks() {
        
        return HTMLDocument.stripRemarks(html);

    }
    
    /**
     * 
     * Strip out HTML remarks
     * 
     * @param _html
     * @return
     */
    public static String stripRemarks(String _html) {

        // http://davidwalsh.name/remove-html-comments-php
        String cleaned = _html.replaceAll("<!--(.|\\s)*?-->", "");

        return cleaned.trim();

    }

    /**
     *
     * Shortcut to get the instances body
     *
     * @return
     */
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

        int firstPos = HTMLDocument.getStartBodyIndex(_html),
            lastPos  = HTMLDocument.getEndBodyIndex(_html);

        // Return the string between these two indices, and trim leading and trailing whitespace
        return _html.substring(firstPos, lastPos).trim();

    }

    /**
     *
     * Get the index position of the last character of the <body> tag,
     * There could be multiple opening body tags, we are looking for the first
     * character of the first occurrence.
     *
     * @param _html
     * @return
     */
    private static int getStartBodyIndex(String _html) {

        Pattern openingBody = Pattern.compile("<body.*>");

        Matcher openingBodyMatcher = openingBody.matcher(_html);

        int firstPos = 0;

        while(openingBodyMatcher.find()) {

            if (openingBodyMatcher.end() > firstPos) {

                firstPos = openingBodyMatcher.end();

            }

        }

        return firstPos;

    }

    /**
     * Get the index position of the first character of the </body> tag
     * @param _html
     * @return
     */
    private static int getEndBodyIndex(String _html) {

        int lastPos  = _html.length();

        Pattern closingBody = Pattern.compile("</body>");

        Matcher closingBodyMatcher = closingBody.matcher(_html);

        while(closingBodyMatcher.find()) {

            if (closingBodyMatcher.start() < lastPos) {

                lastPos = closingBodyMatcher.start();

            }

        }

        return lastPos;

    }

    /**
     *
     * Shortcut to get the instances lines
     *
     * @return
     */
    public HTMLLine[] getLines() {

        return HTMLDocument.getLines(html);

    }

    /**
     *
     * Separate out the given HTML into lines
     *
     * @param _html
     * @return
     */
    public static HTMLLine[] getLines(String _html) {

        String lines[] = _html.split("\\r?\\n");

        HTMLLine[] htmlLines = new HTMLLine[lines.length];

        for(int i = 0; i < lines.length; i++) {

            htmlLines[i] = new HTMLLine(lines[i]);

        }

        return htmlLines;

    }

}
