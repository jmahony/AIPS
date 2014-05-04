package es.cnewsbit.htmlutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhitespaceStripper {

    /**
     *
     * Strip out HTML extra whitespace, for example multiple newlines or spaces.
     *
     */
    public static String strip(String html) {

        Pattern whitespace = Pattern.compile("^\\s+", Pattern.MULTILINE);

        Matcher matcher = whitespace.matcher(html);

        String cleaned = matcher.replaceAll("");

        return cleaned.trim();

    }

}
