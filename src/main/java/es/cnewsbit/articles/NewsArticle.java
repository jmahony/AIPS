package es.cnewsbit.articles;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleSentencesExtractor;
import es.cnewsbit.C;
import es.cnewsbit.HTMLDocument;
import es.cnewsbit.Indexable;
import es.cnewsbit.exceptions.NoDateException;
import es.cnewsbit.extractors.ContentExtractor;
import es.cnewsbit.extractors.TTRContentExtractor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.List;

/**
 * Generic base extractor class
 */
@Log4j2
public abstract class NewsArticle implements Indexable {

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
    /**
     *
     * Constructor
     *
     * @param document the document of the news article
     * @param url the URL of the article
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
     * Attempt to get the headline from the article
     *
     * @return the headline
     */
    public String getHeadline() {

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

            ContentExtractor extractor = new TTRContentExtractor();

            extractor.extract(document);

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

        return getHeadline() + " " + getMetaKeywords() + " " + getContent();

    }

    /**
     *
     * The summarisation of the news article
     *
     * @return content summarisation
     */
    public String getSummarisation() {

        return "";

    }

    /**
     *
     * Gets the date of the article
     *
     * TODO: This needs rethinking
     *
     * @return the date of the article
     * @throws NoDateException if there is no overriding method
     */
    public DateTime getDate() throws NoDateException {

        throw new NoDateException("No date found in article");

    }

}
