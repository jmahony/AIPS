package com.joshmahony;

import com.joshmahony.exceptions.InvalidKernelException;

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
    public HTMLDocument(String _html, double[] kernel) throws Exception {

        html = _html.trim();

        // TODO: Clean me!!
        htmlBodyLines = smooth(getLines(stripWhitespace(stripScripts(stripRemarks(stripStyles(getBody()))))), kernel);

    }

    /**
     *
     * Shortcut to strip whitespace
     *
     * @return HTML less any leading and newline whitespace
     */
    private String stripWhitespace() {

        return HTMLDocument.stripWhitespace(html);

    }

    /**
     *
     * Strip out HTML whitespace
     *
     * @param _html
     * @return HTML less any leading and newline whitespace
     */
    public static String stripWhitespace(String _html) {

        Pattern whitespace = Pattern.compile("^\\s+", Pattern.MULTILINE);

        Matcher matcher = whitespace.matcher(_html);

        String cleaned = matcher.replaceAll("");

        return cleaned.trim();

    }

    /**
     *
     * Shortcut to strip the instances styles
     *
     * @return HTML less style tags
     */
    private String stripStyles() {

        return HTMLDocument.stripStyles(html);

    }

    /**
     *
     * Strip out HTML styles
     *
     * @param _html
     * @return HTML less style tags
     */
    public static String stripStyles(String _html) {

        String cleaned = _html.replaceAll("<style\\b[^<]*(?:(?!<\\/style>)<[^<]*)*<\\/style>", "");

        return cleaned.trim();

    }

    /**
     *
     * Shortcut to strip the instances remarks
     *
     * @return HTML less script tags
     */
    private String stripScripts() {

        return HTMLDocument.stripScripts(html);

    }

    /**
     *
     * Strip out HTML remarks
     *
     * @param _html
     * @return HTML less script tags
     */
    public static String stripScripts(String _html) {

        // https://github.com/jquery/jquery/blob/1.7.2/src/ajax.js#L14
        String cleaned = _html.replaceAll("<script\\b[^<]*(?:(?!<\\/script>)<[^<]*)*<\\/script>", "");

        return cleaned.trim();

    }

    /**
     *
     * Shortcut to strip the instances remarks
     *
     * @return HTML less remark tags
     */
    private String stripRemarks() {

        return HTMLDocument.stripRemarks(html);

    }

    /**
     *
     * Strip out HTML remarks
     *
     * @param _html
     * @return HTML less remark tags
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
     * @return return body of HTML document
     */
    private String getBody() {

        return HTMLDocument.getBody(html);

    }

    /**
     *
     * Returns the HTML between <body> & </body>
     *
     * @param _html
     * @return HTML between the two body tags
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
     * @return first char index of first body tag
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
     *
     * @param _html
     * @return last char index of last body tag
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
     * @return array of HTML lines
     */
    public HTMLLine[] getLines() {

        return HTMLDocument.getLines(html);

    }

    /**
     *
     * Separate out the given HTML into lines
     *
     * @param _html
     * @return array of HTML lines
     */
    public static HTMLLine[] getLines(String _html) {

        String lines[] = _html.split("\\r?\\n");

        HTMLLine[] htmlLines = new HTMLLine[lines.length];

        for(int i = 0; i < lines.length; i++) {

            htmlLines[i] = new HTMLLine(lines[i]);

        }

        return htmlLines;

    }

    /**
     *
     * Smooths an array of HTMLLine objects, needs a kernel for the smoothing process.
     *
     * @param lines
     * @param kernel
     * @return an array of HTMLLine objects with the smoothed ratio populated
     * @throws Exception
     */
    public static HTMLLine[] smooth(HTMLLine[] lines, double[] kernel) throws InvalidKernelException {

        if (kernel.length % 2 == 0) 
            throw new InvalidKernelException("Kernel length must be odd");

        int kernelOverlap = (int) Math.floor(kernel.length / 2);

        for (int i = 0; i < lines.length; i++) {

            double newRatio = 0;

            for (int j = 0; j < kernel.length; j++) {

                int lineIndex = i - kernelOverlap + j;

                if (lineIndex >= 0 && lineIndex < lines.length) {

                    newRatio += lines[lineIndex].textTagRatio * kernel[j];

                }

            }

            lines[i].smoothedtTextTagRatio = newRatio;

        }

        return lines;

    }

}
