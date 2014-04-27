package es.cnewsbit.extractors;

import es.cnewsbit.C;
import es.cnewsbit.HTMLDocument;
import es.cnewsbit.HTMLLine;
import es.cnewsbit.exceptions.InvalidKernelException;
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
    private HTMLDocument document;

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
     *
     * Attempts to remove clutter from around the actual body content of a HTML
     * Document
     *
     * @param document the string to attempt content extraction
     * @return the content
     */
    @Override
    public String extract(HTMLDocument document) {

        this.document = document;

        String html = document.getHtml();

        html = WhitespaceStripper.strip(html);

        html = ScriptsStripper.strip(html);

        html = RemarkStripper.strip(html);

        html = StylesStripper.strip(html);

        html = BodyExtractor.extract(html);

        populateHTMLLinesArray(html);

        smooth();

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < htmlBodyLines.length; i++) {

            double ratio = htmlBodyLines[i].getSmoothedTextTagRatio();

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

    /**
     *
     * Smooths an array of HTMLLine objects, needs a kernel for the smoothing process.
     *
     * @return an array of HTMLLine objects with the smoothed ratio populated
     * @exception es.cnewsbit.exceptions.InvalidKernelException if the kernel has an even number of elements
     */
    private void smooth() throws InvalidKernelException {

        if (kernel.length % 2 == 0)
            throw new InvalidKernelException("Kernel length must be odd");

        int kernelOverlap = (int) Math.floor(kernel.length / 2);

        for (int i = 0; i < htmlBodyLines.length; i++) {

            double newRatio = 0;

            for (int j = 0; j < kernel.length; j++) {

                int lineIndex = i - kernelOverlap + j;

                if (lineIndex >= 0 && lineIndex < htmlBodyLines.length) {

                    newRatio += htmlBodyLines[lineIndex].getTextTagRatio() * kernel[j];

                }

            }

            htmlBodyLines[i].setSmoothedTextTagRatio(newRatio);

        }

    }

}
