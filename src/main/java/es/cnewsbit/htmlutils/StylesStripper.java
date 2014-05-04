package es.cnewsbit.htmlutils;

public class StylesStripper {

    public static String strip(String html) {

        String cleaned = html.replaceAll("<style\\b[^<]*(?:(?!<\\/style>)<[^<]*)*<\\/style>", "");

        return cleaned.trim();

    }

}
