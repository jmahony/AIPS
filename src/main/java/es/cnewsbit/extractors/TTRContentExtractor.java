package es.cnewsbit.extractors;

import es.cnewsbit.C;
import es.cnewsbit.HTMLLine;
import es.cnewsbit.htmlutils.*;
import es.cnewsbit.utilities.HTMLLineFactory;
import lombok.Getter;
import lombok.Setter;

/**
 * Uses the text to tag ratio to extract content from a HTML document
 */
public class TTRContentExtractor implements ContentExtractor {

    /**
     * The HTML Document
     */
    private Extractable extractable;

    /**
     * The kernel used for smoothing
     */
    private @Getter @Setter double[] kernel;

    /**
     * Upper bound ratio
     */
    private @Getter @Setter double upperThreshold = C.UPPER_BOUND_EXTRACTION_THRESHOLD;

    /**
     * Lower bound ratio
     */
    private @Getter @Setter double lowerThreshold = C.LOWER_BOUND_EXTRACTION_THRESHOLD;

    /**
     * The HTML document split up into individual HTMLLine objects
     */
    private HTMLLine[] htmlBodyLines;

    /**
     * Smoother to smooth the smoothables
     */
    private @Getter @Setter Smoother smoother = new GaussianSmoother();

    /**
     *
     * Attempts to remove clutter from around the actual body content of a HTML
     * Document
     *
     * @param extractable extractable object
     * @return the content
     */
    @Override
    public String extract(Extractable extractable) {

        this.extractable = extractable;

        String html = this.extractable.getHtml();

        html = WhitespaceStripper.strip(html);

        html = ScriptsStripper.strip(html);

        html = RemarkStripper.strip(html);

        html = StylesStripper.strip(html);

        html = BodyExtractor.extract(html);

        htmlBodyLines = HTMLLineFactory.build(html);

        smoother.smooth(htmlBodyLines);

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < htmlBodyLines.length; i++) {

            double ratio = htmlBodyLines[i].getSmoothedValue();

            if (ratio >= getLowerThreshold() &&
                    ratio <= getUpperThreshold()) {

                sb.append(htmlBodyLines[i].getText());

            }

        }

        return sb.toString();

    }

}
