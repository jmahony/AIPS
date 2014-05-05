package es.cnewsbit.articles;

import es.cnewsbit.HTMLDocument;
import es.cnewsbit.exceptions.NoDateException;
import es.cnewsbit.exceptions.NotNewsArticleException;
import lombok.extern.log4j.Log4j2;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.nodes.Element;

import java.net.URL;

/**
 * News article parser for articles on the news.sky.co.uk domain
 */
@Log4j2
public class SkyNewsArticle extends NewsArticle {

    /**
     * The format used for the published date
     */
    private static final String dateFormat = "yyyy-MM-dd'T'HH:mm:SSZ";

    /**
     *
     * Constructor
     *
     * @throws NotNewsArticleException if URL is blacklisted
     * @throws NotNewsArticleException if the title is blacklisted
     */
    public SkyNewsArticle(HTMLDocument document, URL url) throws NotNewsArticleException {

        super(document, url);

        // Get open graph meta tag type
        Element elem = document.getDOM().select("meta[property=og:type]").first();

        // If the OG type meta tag is not found, add the article anyway because
        // older articles may not have them.
        if (elem != null) {

            String OGType = elem.attr("content").toString();

            if (!OGType.equals("article"))
                throw new NotNewsArticleException("OG type is not article");

        }

    }

    /**
     *
     * Attempts to return the date of the news article by looking at meta tags.
     *
     * @return the date of the article
     * @throws NoDateException if a date cant be found
     */
    @Override
    public DateTime getDate() throws NoDateException {

        Element elem = document.getDOM().select("meta[property=article:modified_time]").first();

        if (elem != null) {

            String timestamp = elem.attr("content").toString();

            DateTimeFormatter dtf = DateTimeFormat.forPattern(dateFormat);

            DateTime dt = DateTime.parse(timestamp, dtf);

            return dt;

        }

        throw new NoDateException("No date found in article");

    }

}
