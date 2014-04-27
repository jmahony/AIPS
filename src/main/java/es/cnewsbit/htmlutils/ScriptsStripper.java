package es.cnewsbit.htmlutils;

/**
 * Created by joshmahony on 27/04/2014.
 */
public class ScriptsStripper {

    /**
     *
     * Strip out HTML remarks
     *
     * @param html html to be stripped of script tags
     * @return HTML less script tags
     */
    public static String strip(String html) {

        // https://github.com/jquery/jquery/blob/1.7.2/src/ajax.js#L14
        String cleaned = html.replaceAll("<script\\b[^<]*(?:(?!<\\/script>)<[^<]*)*<\\/script>", "");

        return cleaned.trim();

    }

}
