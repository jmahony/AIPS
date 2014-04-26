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
 * News article parser for articles on the bbc.co.uk domain
 */
@Log4j2
public class BBCNewsArticle extends NewsArticle {

    /**
     * The format used for the published date
     */
    private static final String dateFormat = "yyyy/MM/dd HH:mm:SS";

    /**
     * Any pages with these titles will be ignored
     */
    private static String[] headlineBlacklist = new String[] {
            "INDEX",
            "BBC NEWS | Marketwatch"
    };

    /**
     * Any pages with these urls will be ignored
     */
    private static String[] urlBlacklist = new String[] {
            "print=trueprint=true",
            "/news/correspondents/"
    };

    /**
     *
     * Constructor
     *
     * @param document the document of the article
     * @param url the url of the article
     * @throws NotNewsArticleException if URL is blacklisted
     * @throws NotNewsArticleException if the title is blacklisted
     */
    public BBCNewsArticle(HTMLDocument document, URL url) throws NotNewsArticleException {

        super(document, url);

        // Check the URL isn't in the black list
        for (String regex : urlBlacklist) {

            if (url.toString().contains(regex))
                throw new NotNewsArticleException("URL is black listed");

        }

        // Check the headline isn't in the black list
        for (String regex : headlineBlacklist) {

            if (getHeadline().contains(regex))
                throw new NotNewsArticleException("Document title is black listed");

        }

        // Get open graph meta tag type
        Element elem = document.getDom().select("meta[property=og:type]").first();

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
     * Attempts to return the date of the news article
     *
     * @return the date of the article
     * @throws NoDateException if a date cant be found
     */
    @Override
    public DateTime getDate() throws NoDateException {

        Element elem = document.getDom().select("meta[property=rnews:datePublished]").first();

        if (elem != null) {

            DateTimeFormatter dtf = DateTimeFormat.forPattern(dateFormat);

            DateTime dt = DateTime.parse(elem.attr("content"), dtf);

            return dt;

        }

        elem = document.getDom().select("meta[name=OriginalPublicationDate]").first();

        if (elem != null) {

            DateTimeFormatter dtf = DateTimeFormat.forPattern(dateFormat);

            DateTime dt = DateTime.parse(elem.attr("content"), dtf);

            return dt;

        }

        throw new NoDateException("No date found in article");

    }

}

