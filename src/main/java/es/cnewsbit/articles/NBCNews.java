package es.cnewsbit.articles;

import es.cnewsbit.HTMLDocument;
import es.cnewsbit.exceptions.NoDateExeception;
import es.cnewsbit.exceptions.NotNewsArticleException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.nodes.Element;

import java.net.URL;

/**
 * Created by josh on 16/04/14.
 */
public class NBCNews extends NewsArticle {

    private static final String dateFormat = "yyyy-MM-dd'T'HH:mm:SS";
    private static final String dateFormatAlt = "yyyy-MM-dd'T'HH:mm:SSZ";

    private static String[] urlBlacklist = new String[] {
            "http://www.nbcnews.com/.*/tag/.*",
            "http://www.nbcnews.com/html/msnbc/.*",
            "http://www.nbcnews.com/id/3032123/ns/travel/"
    };

    private static String[] headlineBlacklist = new String[] {
            "Meet the Press",
            "The Ed Show",
            "Ed Show Transcripts",
            "Hardball Transcripts",
            "Dateline NBC: News stories about crime, celebrity and health",
            "NBC News Social Directory"
    };


    /**
     * Constructor
     *
     * @param document the document of the news article
     * @param url
     */
    public NBCNews(HTMLDocument document, URL url) throws NotNewsArticleException {

        super(document, url);

        for (String regex : urlBlacklist) {

            if (url.toString().matches(regex))
                throw new NotNewsArticleException("URL is black listed");

        }

        for (String regex : headlineBlacklist) {

            if (getHeading().contains(regex))
                throw new NotNewsArticleException("Document title is black listed");

        }

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
     * Attempt to get a title from the article
     *
     * @return content of first h1
     */
    public String getHeading() {

        String headline = null;
        Element elem;

        // Attempt getting the title from open graph tags
        elem = document.getDom().select("meta[property=og:title], META[property=og:title]").first();

        if (elem != null) headline = elem.attr("content");

        // Attempt to get title from title tag
        elem = document.getDom().getElementsByTag("title").first();

        if (elem != null && headline == null) return elem.html();

        // Atempt to get title from the first h1 tag on the page
        elem = document.getDom().select("h1").first();

        if (elem != null && headline == null) return elem.html();

        if (headline != null) {

            String cleanHeadline = headline.replace("- NBC News.com", "");

            return cleanHeadline;

        }

        return "COULD NOT FIND TITLE";

    }


    public DateTime getDate() throws NoDateExeception {

        Element elem = document.getDom().select("meta[property=article:published_time]").first();

        if (elem != null) {

            DateTimeFormatter dtf = DateTimeFormat.forPattern(dateFormat);

            DateTime dt = DateTime.parse(elem.attr("content").toString(), dtf);

            return dt;

        }

        elem = document.getDom().select("meta[name=DC.date.issued]").first();

        if (elem != null) {

            DateTimeFormatter dtf = DateTimeFormat.forPattern(dateFormatAlt);

            DateTime dt = DateTime.parse(elem.attr("content").toString(), dtf);

            return dt;

        }

        throw new NoDateExeception("No date found in article");

    }

}
