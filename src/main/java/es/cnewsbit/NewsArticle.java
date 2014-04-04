package es.cnewsbit;

import lombok.Getter;

import java.util.List;
import java.net.URL;

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

        List<String> headings = document.getHeadingOnes();

        if (!headings.isEmpty()) {

            return headings.get(0);

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

        return Integer.toString(0);

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
}
