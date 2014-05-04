package es.cnewsbit.articles;

import es.cnewsbit.HTMLDocument;
import es.cnewsbit.Indexable;
import es.cnewsbit.exceptions.NoDateException;
import es.cnewsbit.extractors.ContentExtractor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.List;

@Log4j2
public abstract class NewsArticle implements Indexable {

    protected final @Getter HTMLDocument document;

    protected final @Getter URL url;

    protected @Getter @Setter ContentExtractor contentExtractor;

    public NewsArticle(HTMLDocument document, URL url) {

        this.document = document;

        this.url = url;

    }

    public List<String> getMetaKeywords() {

        return document.getMetaKeywords();

    }

    /**
     *
     * Attempt to get the headline from the article. It starts by trying to
     * extract the open graph title, then the HTML page title and finally,
     * if the other two don't exist it will try to return the first h1 HTML tag
     * is can find.
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
     * Runs the content extractor over the HTML document and, hopefully, returns
     * the HTML pages actual content.
     *
     * @return the content
     */
    public String getContent() {

        return contentExtractor.extract(document);

    }

    /**
     *
     * Gets a unique handle to identify the article by, this is used by the
     * index.
     *
     * @return the handle to be used by the indexer
     */
    @Override public String getHandle() {

        return DigestUtils.sha256Hex(url.toString());

    }

    /**
     *
     * Get the content that the article will be indexed by.
     *
     * @return the index base
     */
    @Override public String getIndexString() {

        return getHeadline() + " " + getMetaKeywords() + " " + getContent();

    }

    public String getSummarisation() {

        return "";

    }

    //TODO: This needs rethinking
    public DateTime getDate() throws NoDateException {

        throw new NoDateException("No date found in article");

    }

}
