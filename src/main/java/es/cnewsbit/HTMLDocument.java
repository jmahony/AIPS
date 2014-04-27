package es.cnewsbit;

import es.cnewsbit.extractors.Extractable;
import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a HTML document
 */
public class HTMLDocument implements Extractable {

    /**
     * Original HTML line
     */
    private @Getter final String html;

    /**
     * A Jsoup version of the HTML
     */
    private @Getter final Document dom;

    /**
     * The keywords contained in the meta tag of the HTML
     */
    private @Getter List<String> metaKeywords;

    /**
     * All of the H1 tags content on the page
     */
    private @Getter List<String> headingOnes;

    /**
     * Takes the HTML
     *
     * @param html the HTML
     */
    public HTMLDocument(String html) {

        this.html = html.trim();

        this.dom  = Jsoup.parse(this.html);

        this.headingOnes = parseHOnes();

        this.metaKeywords = parseKeywords();

        // TODO: Clean me!!

    }

    /**
     *
     * Pareses all H1 tags
     *
     * @return a list of h1 tags HTML
     */
    public ArrayList<String> parseHOnes() {

        ArrayList<String> headings = new ArrayList<>();

        Elements elements = dom.select("h1");

        for(Element element : elements) {

            headings.add(element.html());

        }

        return headings;

    }

    /**
     *
     * Parses the meta keywords
     *
     * @return a set of keywords
     */
    private ArrayList<String> parseKeywords() {

        Element elem = dom.select("meta[name=keywords], META[name=keywords]").first();

        ArrayList<String> keywords = new ArrayList<>();

        if (elem != null) {

            String[] words = elem.attr("content").split(", *");

            keywords.addAll(Arrays.asList(words));

        }

        return keywords;

    }

}
