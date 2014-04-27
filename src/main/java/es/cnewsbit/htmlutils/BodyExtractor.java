package es.cnewsbit.htmlutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by joshmahony on 27/04/2014.
 */
public class BodyExtractor {

    /**
     *
     * Returns the HTML between opening and closing body tags
     *
     * @param html html to extract body content from
     * @return HTML between the two body tags
     */
    public static String extract(String html) {

        int firstPos = BodyExtractor.getStartBodyIndex(html),
                lastPos  = BodyExtractor.getEndBodyIndex(html);

        // Return the string between these two indices, and trim leading and trailing whitespace
        return html.substring(firstPos, lastPos).trim();

    }

    /**
     *
     * Get the index position of the last character of the opening body tag,
     * There could be multiple opening body tags, we are looking for the first
     * character of the first occurrence.
     *
     * @param html html to get the index from
     * @return first char index of first body tag
     */
    private static int getStartBodyIndex(String html) {

        Pattern openingBody = Pattern.compile("<body[^>]*>");

        Matcher openingBodyMatcher = openingBody.matcher(html);

        int firstPos = 0;

        while(openingBodyMatcher.find()) {

            if (openingBodyMatcher.end() > firstPos) {

                firstPos = openingBodyMatcher.end();

            }

        }

        return firstPos;

    }

    /**
     * Get the index position of the first character of the closing body tag
     *
     * @param html html to get the index from
     * @return last char index of last body tag
     */
    private static int getEndBodyIndex(String html) {

        int lastPos  = html.length();

        Pattern closingBody = Pattern.compile("</body>");

        Matcher closingBodyMatcher = closingBody.matcher(html);

        while(closingBodyMatcher.find()) {

            if (closingBodyMatcher.start() < lastPos) {

                lastPos = closingBodyMatcher.start();

            }

        }

        return lastPos;

    }

}
