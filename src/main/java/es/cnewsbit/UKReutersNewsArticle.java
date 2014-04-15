package es.cnewsbit;

import lombok.extern.log4j.Log4j2;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.nodes.Element;

import java.net.URL;

/**
 * Created by josh on 15/04/14.
 */
@Log4j2
public class UKReutersNewsArticle extends NewsArticle {

    private static final String dateFormat = "EE MMM d H:m:s z y";

    /**
     * Constructor
     *
     * @param document the document of the news article
     * @param url
     */
    public UKReutersNewsArticle(HTMLDocument document, URL url) {

        super(document, url);

    }

    public DateTime getDate() {

        Element elem = document.getDom().select("META[name=REVISION_DATE]").first();

        if (elem != null) {

            DateTimeFormatter dtf = DateTimeFormat.forPattern(dateFormat);

            DateTime dt = DateTime.parse(elem.attr("content").toString(), dtf);

            return dt;

        }

        return new DateTime();

    }
}
