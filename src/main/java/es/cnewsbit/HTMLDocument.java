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

public class HTMLDocument implements Extractable {

    private @Getter final String HTML;

    private @Getter final Document DOM;

    private @Getter List<String> metaKeywords;

    private @Getter List<String> headingOnes;

    public HTMLDocument(String html) {

        this.HTML = html.trim();

        this.DOM = Jsoup.parse(this.HTML);

        this.headingOnes = populateH1List();

        this.metaKeywords = parseMetaKeywords();

        // TODO: Clean me!!

    }

    public ArrayList<String> populateH1List() {

        ArrayList<String> headings = new ArrayList<String>();

        Elements elements = DOM.select("h1");

        for(Element element : elements) {

            headings.add(element.html());

        }

        return headings;

    }

    //TODO: This can be moved out of this class
    private ArrayList<String> parseMetaKeywords() {

        Element elem = DOM.select("meta[name=keywords], META[name=keywords]").first();

        ArrayList<String> keywords = new ArrayList<String>();

        if (elem != null) {

            String[] words = elem.attr("content").split(", *");

            keywords.addAll(Arrays.asList(words));

        }

        return keywords;

    }

}
