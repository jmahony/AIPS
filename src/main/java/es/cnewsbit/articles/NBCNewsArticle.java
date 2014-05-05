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
 * News article parser for articles on the nbcnews.com domain
 */
@Log4j2
public class NBCNewsArticle extends NewsArticle {

    /**
     * The format used for the published date
     */
    private static final String dateFormat = "yyyy-MM-dd'T'HH:mm:SS";

    /**
     * The alternative format used for the published date
     */
    private static final String dateFormatAlt = "yyyy-MM-dd'T'HH:mm:SSZ";

    /**
     * Any pages with these titles will be ignored
     */
    private static String[] headlineBlacklist = new String[] {
            "Meet the Press",
            "The Ed Show",
            "Ed Show Transcripts",
            "Hardball Transcripts",
            "Dateline NBC: News stories about crime, celebrity and health",
            "NBC News Social Directory"
    };

    /**
     * Any pages with these urls will be ignored
     */
    private static String[] urlBlacklist = new String[] {
            "http://www.nbcnews.com/.*/tag/.*",
            "http://www.nbcnews.com/html/msnbc/.*",
            "http://www.nbcnews.com/id/3032123/ns/travel/"
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
    public NBCNewsArticle(HTMLDocument document, URL url) throws NotNewsArticleException {

        super(document, url);

        // Check the URL isn't in the black list
        for (String regex : urlBlacklist) {

            if (url.toString().matches(regex))
                throw new NotNewsArticleException("URL is black listed");

        }

        // Check the headline isn't in the black list
        for (String regex : headlineBlacklist) {

            if (getHeadline().contains(regex))
                throw new NotNewsArticleException("Document title is black listed");

        }

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
     * Attempt to get a title from the article
     *
     * @return the title of the article
     */
    @Override
    public String getHeadline() {

        String headline = null;
        Element elem;

        // Attempt getting the title from open graph tags
        elem = document.getDOM().select("meta[property=og:title], META[property=og:title]").first();

        if (elem != null) headline = elem.attr("content");

        // Attempt to get title from title tag
        elem = document.getDOM().getElementsByTag("title").first();

        if (elem != null && headline == null) return elem.html();

        // Attempt to get title from the first h1 tag on the page
        elem = document.getDOM().select("h1").first();

        if (elem != null && headline == null) return elem.html();

        if (headline != null) {

            return headline.replace("- NBC News.com", "");

        }

        return "COULD NOT FIND TITLE";

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

        Element elem = document.getDOM().select("meta[property=article:published_time]").first();

        if (elem != null) {

            DateTimeFormatter dtf = DateTimeFormat.forPattern(dateFormat);

            DateTime dt = DateTime.parse(elem.attr("content"), dtf);

            return dt;

        }

        elem = document.getDOM().select("meta[name=DC.date.issued]").first();

        if (elem != null) {

            DateTimeFormatter dtf = DateTimeFormat.forPattern(dateFormatAlt);

            DateTime dt = DateTime.parse(elem.attr("content"), dtf);

            return dt;

        }

        throw new NoDateException("No date found in article");

    }

}
