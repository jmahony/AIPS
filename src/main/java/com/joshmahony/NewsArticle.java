package com.joshmahony;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by josh on 24/02/14.
 */
public class NewsArticle {

    /**
     *
     */
    private final String heading;

    /**
     *
     */
    private final HashSet<String> keywords;

    /**
     *
     */
    private final String author;

    /**
     *
     */
    private final int date;

    /**
     *
     */
    private final String html;

    /**
     *
     */
    private Document document;

    /**
     *
     */
    private String content;

    /**
     *
     * @param _html
     */
    public NewsArticle(String _html) {

        // Store a copy of the raw HTML
        html = _html;

        // Create Jsoup version
        document = Jsoup.parse(html);

        // Populate the heading
        heading = parseHeading(html);

        // Populate the keywords, array list because we may need to add some more
        keywords = parseKeywords(html);

        // Populate the author
        author = NewsArticle.parseAuthor(html);

        // Populate the date
        date = NewsArticle.parseDate(html);

        // Content
        content = extractContent(html);

    }

    private String extractContent(String _html) {

        Pattern pattern = Pattern.compile("<body.*>");

        Matcher matcher = pattern.matcher(_html);

        int firstPos = 0,
                lastPos  = _html.length();

        while(matcher.find()) {

            if (matcher.end() > firstPos) {

                firstPos = matcher.end();

            }

        }

        Pattern pattern2 = Pattern.compile("</body>");

        Matcher matcher2 = pattern2.matcher(_html);

        while(matcher2.find()) {

            if (matcher2.start() < lastPos) {
                System.out.println(matcher2.group());
                lastPos = matcher2.start();

            }

        }

        String content = _html.substring(firstPos, lastPos).replaceAll("\"(?m)^[ \\t]*\\r?\\n\", \"\"", "");

        String lines[] = content.trim().split("\\r?\\n");

        System.out.println(content);

        return "";

    }

    /**
     *
     * @param _html
     * @return
     */
    private String parseHeading(String _html) {

        String heading = document.select("h1").html();

        return heading;

    }

    /**
     *
     * @param _html
     * @return
     */
    private HashSet<String> parseKeywords(String _html) {

        Element elem = document.select("meta[name=keywords], META[name=keywords]").first();

        if (elem != null) {

            String[] words = elem.attr("content").split(", *");

            HashSet<String> keywords = new HashSet<String>();

            keywords.addAll(Arrays.asList(words));

            return keywords;

        }

        return new HashSet<String>();

    }

    /**
     *
     * @param document
     * @return
     */
    private static int parseDate(String document) {

        int date = 0;

        return date;

    }

    /**
     *
     * @param document
     * @return
     */
    public static String parseAuthor(String document) {

        String author = null;

        return author;

    }

}
