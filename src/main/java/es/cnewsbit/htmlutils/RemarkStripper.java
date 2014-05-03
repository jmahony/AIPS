package es.cnewsbit.htmlutils;

/**
 * Strips all comment/remark tags from a HTML document
 */
public class RemarkStripper {

    /**
     *
     * Strip out HTML remarks
     *
     * @param html html to be stripped of XML comments
     * @return HTML less remark tags
     */
    public static String strip(String html) {

        // http://davidwalsh.name/remove-html-comments-php
        String cleaned = html.replaceAll("<!--(.|\\s)*?-->", "");

        return cleaned.trim();

    }

}
