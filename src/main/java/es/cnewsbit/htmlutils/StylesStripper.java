package es.cnewsbit.htmlutils;

/**
 * Created by joshmahony on 27/04/2014.
 */
public class StylesStripper {

    /**
     *
     * Strip out HTML styles
     *
     * @param html html to be stripped of style tags
     * @return HTML less style tags
     */
    public static String strip(String html) {

        String cleaned = html.replaceAll("<style\\b[^<]*(?:(?!<\\/style>)<[^<]*)*<\\/style>", "");

        return cleaned.trim();

    }

}
