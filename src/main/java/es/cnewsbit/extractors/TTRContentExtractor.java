package es.cnewsbit.extractors;

import es.cnewsbit.HTMLDocument;
import es.cnewsbit.HTMLLine;
import es.cnewsbit.exceptions.InvalidKernelException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by joshmahony on 27/04/2014.
 */
public class TTRContentExtractor implements ContentExtractor {

    /**
     * The HTML Document
     */
    private HTMLDocument document;

    /**
     * The HTML document split up into individual HTMLLine objects
     */
    private HTMLLine[] htmlBodyLines;

    @Override
    public String extract(String s) {

        document = new HTMLDocument(s);

        htmlBodyLines = getLines(stripWhitespace(stripScripts(stripRemarks(stripStyles(getBody(document.getHtml()))))));

        return "hello";

    }

    /**
     *
     * Strip out HTML whitespace
     *
     * @param _html html to be stripped of whitespace
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
     * Strip out HTML remarks
     *
     * @param _html html to be stripped of script tags
     * @return HTML less script tags
     */
    public static String stripScripts(String _html) {

        // https://github.com/jquery/jquery/blob/1.7.2/src/ajax.js#L14
        String cleaned = _html.replaceAll("<script\\b[^<]*(?:(?!<\\/script>)<[^<]*)*<\\/script>", "");

        return cleaned.trim();

    }

    /**
     *
     * Strip out HTML remarks
     *
     * @param _html html to be stripped of XML comments
     * @return HTML less remark tags
     */
    public static String stripRemarks(String _html) {

        // http://davidwalsh.name/remove-html-comments-php
        String cleaned = _html.replaceAll("<!--(.|\\s)*?-->", "");

        return cleaned.trim();

    }

    /**
     *
     * Strip out HTML styles
     *
     * @param _html html to be stripped of style tags
     * @return HTML less style tags
     */
    public static String stripStyles(String _html) {

        String cleaned = _html.replaceAll("<style\\b[^<]*(?:(?!<\\/style>)<[^<]*)*<\\/style>", "");

        return cleaned.trim();

    }

    /**
     *
     * Returns the HTML between opening and closing body tags
     *
     * @param _html html to extract body content from
     * @return HTML between the two body tags
     */
    public static String getBody(String _html) {

        int firstPos = TTRContentExtractor.getStartBodyIndex(_html),
                lastPos  = TTRContentExtractor.getEndBodyIndex(_html);

        // Return the string between these two indices, and trim leading and trailing whitespace
        return _html.substring(firstPos, lastPos).trim();

    }

    /**
     *
     * Get the index position of the last character of the <body> tag,
     * There could be multiple opening body tags, we are looking for the first
     * character of the first occurrence.
     *
     * @param _html html to get the index from
     * @return first char index of first body tag
     */
    private static int getStartBodyIndex(String _html) {

        Pattern openingBody = Pattern.compile("<body[^>]*>");

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
     * @param _html html to get the index from
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
     * Separate out the given HTML into lines
     *
     * @param _html html to split into lines
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
     * @param lines the array of HTMLLines to smooth
     * @param kernel the kernel to smooth the lines ratio with
     * @return an array of HTMLLine objects with the smoothed ratio populated
     * @exception es.cnewsbit.exceptions.InvalidKernelException if the kernel has an even number of elements
     */
    private HTMLLine[] smooth(HTMLLine[] lines, double[] kernel) throws InvalidKernelException {

        if (kernel.length % 2 == 0)
            throw new InvalidKernelException("Kernel length must be odd");

        int kernelOverlap = (int) Math.floor(kernel.length / 2);

        for (int i = 0; i < lines.length; i++) {

            double newRatio = 0;

            for (int j = 0; j < kernel.length; j++) {

                int lineIndex = i - kernelOverlap + j;

                if (lineIndex >= 0 && lineIndex < lines.length) {

                    newRatio += lines[lineIndex].getTextTagRatio() * kernel[j];

                }

            }

            lines[i].setSmoothedTextTagRatio(newRatio);

        }

        return lines;

    }

}
