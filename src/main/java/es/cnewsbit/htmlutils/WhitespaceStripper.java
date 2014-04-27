package es.cnewsbit.htmlutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by joshmahony on 27/04/2014.
 */
public class WhitespaceStripper {

    /**
     *
     * Strip out HTML whitespace
     *
     * @param html html to be stripped of whitespace
     * @return HTML less any leading and newline whitespace
     */
    public static String strip(String html) {

        Pattern whitespace = Pattern.compile("^\\s+", Pattern.MULTILINE);

        Matcher matcher = whitespace.matcher(html);

        String cleaned = matcher.replaceAll("");

        return cleaned.trim();

    }

}
