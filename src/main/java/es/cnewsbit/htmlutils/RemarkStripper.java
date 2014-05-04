package es.cnewsbit.htmlutils;

public class RemarkStripper {

    /**
     *
     * Strip out HTML remarks (comments)
     *
     */
    public static String strip(String html) {

        // http://davidwalsh.name/remove-html-comments-php
        String cleaned = html.replaceAll("<!--(.|\\s)*?-->", "");

        return cleaned.trim();

    }

}
