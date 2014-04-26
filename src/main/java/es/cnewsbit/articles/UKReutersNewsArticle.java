package es.cnewsbit.articles;

import es.cnewsbit.HTMLDocument;
import es.cnewsbit.exceptions.NoDateException;
import lombok.extern.log4j.Log4j2;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.nodes.Element;

import java.net.URL;

/**
 * News article parser for articles on the bbc.co.uk domain
 */
@Log4j2
public class UKReutersNewsArticle extends NewsArticle {

    /**
     * The format used for the published date
     */
    private static final String dateFormat = "EE MMM d H:m:s z y";

    /**
     *
     * Constructor
     *
     * @param document the document of the article
     * @param url the url of the article
     */
    public UKReutersNewsArticle(HTMLDocument document, URL url) {

        super(document, url);

    }

    /**
     *
     * Attempt to get a title from the article
     *
     * @return content of first h1
     */
    @Override
    public String getHeadline() {

        Element elem;

        // Attempt to get title from the first h1 tag on the page
        elem = document.getDom().select("h1").first();

        if (elem != null) return elem.html();

        return "COULD NOT FIND TITLE";

    }

    /**
     *
     * Extracts the date of the article
     *
     * @return the date of the article
     */
    @Override
    public DateTime getDate() throws NoDateException {

        Element elem = document.getDom().select("META[name=REVISION_DATE]").first();

        if (elem != null) {

            DateTimeFormatter dtf = DateTimeFormat.forPattern(dateFormat);

            DateTime dt = DateTime.parse(elem.attr("content").toString(), dtf);

            return dt;

        }

        throw new NoDateException("No date found in article");

    }

}
