package es.cnewsbit.extractors;

import es.cnewsbit.C;
import es.cnewsbit.HTMLLine;
import es.cnewsbit.htmlutils.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by joshmahony on 27/04/2014.
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

        populateHTMLLinesArray(html);

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

    /**
     *
     * Separate out the given HTML into lines
     *
     * @param html html to split into lines
     * @return array of HTML lines
     */
    private void populateHTMLLinesArray(String html) {

        String lines[] = html.split("\\r?\\n");

        HTMLLine[] htmlLines = new HTMLLine[lines.length];

        for(int i = 0; i < lines.length; i++) {

            htmlBodyLines[i] = new HTMLLine(lines[i]);

        }

    }

}
