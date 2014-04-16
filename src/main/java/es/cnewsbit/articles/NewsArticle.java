package es.cnewsbit.articles;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleSentencesExtractor;
import es.cnewsbit.C;
import es.cnewsbit.HTMLDocument;
import es.cnewsbit.HTMLLine;
import es.cnewsbit.Indexable;
import es.cnewsbit.exceptions.NoDateExeception;
import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
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
    protected final @Getter HTMLDocument document;

    /**
     * The original URL of the article
     */
    protected final @Getter URL url;

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
     * Attempt to get a title from the article
     *
     * @return content of first h1
     */
    public String getHeading() {

        Element elem;

        // Attempt getting the title from open graph tags
        elem = document.getDom().select("meta[property=og:title], META[property=og:title]").first();

        if (elem != null) return elem.attr("content");

        // Attempt to get title from title tag
        elem = document.getDom().getElementsByTag("title").first();

        if (elem != null) return elem.html();

        // Atempt to get title from the first h1 tag on the page
        elem = document.getDom().select("h1").first();

        if (elem != null) return elem.html();

        return "COULD NOT FIND TITLE";

    }

    /**
     *
     * Returns the extracted content of the HTML
     *
     * @return the content string
     */
    public String getContent() {

        String content = null;

        if (C.BOILERPIPE) {

            try {

                content = ArticleSentencesExtractor.INSTANCE.getText(document.getHtml());

            } catch (BoilerpipeProcessingException e) {

                e.printStackTrace();

            }

        } else {

            HTMLLine[] lines = document.getHtmlBodyLines();

            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < lines.length; i++) {

                double ratio = lines[i].getSmoothedTextTagRatio();

                if (ratio >= C.LOWER_BOUND_EXTRACTION_THRESHOLD &&
                        ratio <= C.UPPER_BOUND_EXTRACTION_THRESHOLD) {

                    sb.append(lines[i].getText());

                }

            }

            content = sb.toString();

        }

        return content;

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

    /**
     *
     * Gets the date of the news article
     *
     * @return
     */
    public  DateTime getDate() throws NoDateExeception {

        throw new NoDateExeception("No date found in article");

    }

}
