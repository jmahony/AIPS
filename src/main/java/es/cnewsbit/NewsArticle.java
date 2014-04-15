package es.cnewsbit;

import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.List;

/**
 * Created by josh on 03/04/14.
 */
public class NewsArticle implements Indexable {

    /**
     * The HTML of the news article
     */
    private final @Getter HTMLDocument document;

    /**
     * The original URL of the article
     */
    private final @Getter URL url;

    /**
     *
     * Constructor
     *
     * @param document the document of the news article
     */
    public NewsArticle(HTMLDocument document, URL url) {

        this.document = document;

        this.url = url;

    }

    /**
     *
     * Returns the meta keywords of the article
     *
     * @return a list of keywords
     */
    public List<String> getMetaKeywords() {

        return document.getMetaKeywords();

    }

    /**
     *
     * Get the content of the first h1 tag
     *
     * @return content of first h1
     */
    public String getHeading() {

        Element elem = document.getDom().select("meta[property=og:title], META[property=og:title]").first();

        if (elem != null) {

            return elem.attr("content");

        }

        elem = document.getDom().getElementsByTag("title").first();

        if (elem != null) {

            return elem.html();

        }

        elem = document.getDom().select("h1").first();

        if (elem != null) {

            return elem.html();

        }

        return null;

    }

    /**
     *
     * Returns the extracted content of the HTML
     *
     * @return the content string
     */
    public String getContent() {

        HTMLLine[] lines = document.getHtmlBodyLines();

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < lines.length; i++) {

            double ratio = lines[i].getSmoothedTextTagRatio();

            if (ratio >= C.LOWER_BOUND_EXTRACTION_THRESHOLD &&
                ratio <= C.UPPER_BOUND_EXTRACTION_THRESHOLD) {

                sb.append(lines[i].getText());

            }

        }

        return sb.toString();

    }

    /**
     *
     * Gets a unique handle to identify the article by
     *
     * @return the handle to be used by the indexer
     */
    @Override public String getHandle() {

        return DigestUtils.sha256Hex(url.toString());

    }

    /**
     *
     * Get the actual content to base the index entry on
     *
     * @return the index base
     */
    @Override public String getIndexString() {

        return getHeading() + " " + getMetaKeywords() + " " + getContent();

    }

    public String getSummarisation() {

        return "";

    }



    public DateTime getDate() {

        if (url.getHost().toString().equals("www.bbc.co.uk")) {

            Element elem = document.getDom().select("meta[property=rnews:datePublished]").first();

            if (elem != null) {

                DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:SS");

                DateTime dt = DateTime.parse(elem.attr("content").toString(), dtf);

                return dt;

            }


        }

        return new DateTime();

    }
}
