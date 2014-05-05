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
     * The kernel used for smoothing
     */
    private @Getter @Setter double[] kernel;

    /**
     * Ignore any ratios above the upper bound
     */
    private @Getter @Setter double upperThreshold = C.UPPER_BOUND_EXTRACTION_THRESHOLD;

    /**
     * Ignore any ratios below the lower bound
     */
    private @Getter @Setter double lowerThreshold = C.LOWER_BOUND_EXTRACTION_THRESHOLD;

    private HTMLLine[] htmlBodyLines;

    private @Getter @Setter Smoother smoother = new GaussianSmoother();

    /**
     *
     * Applies the Text-to-Tag-Ratio algorithms as described by Tim Weninger &
     * William H. Hsu in their paper Text Extraction from the Web via
     * Text-to-Tag Ratio (http://www.cse.nd.edu/~tweninge/pubs/WH_TIR08.pdf).
     *
     * This works by stripping out various clutter from a HTML page, calculating
     * a ratio of text to tags for each line, smoothing the ratio histogram and
     * then everything in between the upper and lower bound will be considered
     * content and return as a string.
     *
     * @param extractable extractable object
     * @return the content
     */
    @Override
    public String extract(Extractable extractable) {

        String html = extractable.getHTML();

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
