package es.cnewsbit.articles;

import es.cnewsbit.HTMLDocument;
import es.cnewsbit.exceptions.NotNewsArticleException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.nodes.Element;

import java.net.URL;

/**
 * Created by josh on 15/04/14.
 */
public class BBCNewsArticle extends NewsArticle {

    private static final String dateFormat = "yyyy/MM/dd HH:mm:SS";

    private static String[] headlineBlacklist = new String[] {
            "INDEX"
    };

    /**
     * Constructor
     *
     * @param document the document of the news article
     * @param url
     */
    public BBCNewsArticle(HTMLDocument document, URL url) throws NotNewsArticleException {

        super(document, url);

        for (String regex : headlineBlacklist) {

            if (getHeading().contains(regex))
                throw new NotNewsArticleException("Document title is blacklisted");

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

    public DateTime getDate() {

        Element elem = document.getDom().select("meta[property=rnews:datePublished]").first();

        if (elem != null) {

            DateTimeFormatter dtf = DateTimeFormat.forPattern(dateFormat);

            DateTime dt = DateTime.parse(elem.attr("content").toString(), dtf);

            return dt;

        }

        elem = document.getDom().select("meta[name=OriginalPublicationDate]").first();

        if (elem != null) {

            DateTimeFormatter dtf = DateTimeFormat.forPattern(dateFormat);

            DateTime dt = DateTime.parse(elem.attr("content").toString(), dtf);

            return dt;

        }

        return new DateTime();

    }

}

