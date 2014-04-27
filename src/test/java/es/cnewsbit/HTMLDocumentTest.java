package es.cnewsbit;

import es.cnewsbit.exceptions.InvalidKernelException;
import es.cnewsbit.utilities.CSV;
import es.cnewsbit.utilities.ResourceLoader;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by cnewsbit on 25/02/2014.
 */
public class HTMLDocumentTest {

    /**
     *
     * Make the right amount of lines are taken from the document, the test numbers are counted manually.
     *
     */
    @Test public void testGetLines() throws IOException {

        String simpleHTML = ResourceLoader.asString(this, "/simple.html");

        int lines1 = 83;

        assertEquals(lines1, HTMLDocument.getLines(simpleHTML).length);

        String complexHTML = ResourceLoader.asString(this, "/world-europe-26333587.html");

        int lines2 = 2968;

        assertEquals(lines2, HTMLDocument.getLines(complexHTML).length);

    }

    /**
     *
     * Test the simple HTML document to see if the body is correctly extracted
     *
     */
    @Test public void testSimpleGetBody() throws IOException {

        String simpleHTML = ResourceLoader.asString(this, "/simple.html");

        String simpleHTMLBody = ResourceLoader.asString(this, "/simpleBody.html");

        assertEquals(simpleHTMLBody.trim(), HTMLDocument.getBody(simpleHTML));

    }

    /**
     *
     * Test the complex HTML document to see if the body is correctly extracted
     * TODO: This isn't passing on jenkins, works locally though, need to investigate
     */
    /*Test public void testComplexGetBody() throws IOException {

        String complexHTML = ResourceLoader.asString(this, "/world-europe-26333587.html");

        String complexHTMLBody = ResourceLoader.asString(this, "/world-europe-26333587Body.html");

        assertEquals(complexHTMLBody.trim(), HTMLDocument.getBody(complexHTML));

    }*/

    /**
     *
     */
    @Test public void testSimpleStripRemarks() throws IOException {

        String simpleHTML = ResourceLoader.asString(this, "/simple.html");

        String simpleHTMLRemark = ResourceLoader.asString(this, "/simpleRemarks.html");

        assertEquals(simpleHTMLRemark.trim(), HTMLDocument.stripRemarks(simpleHTML));

    }

    /**
     *
     */
    @Test public void testComplexStripRemarks() throws IOException {

        String complexHTML = ResourceLoader.asString(this, "/world-europe-26333587.html");

        String complexHTMLRemark = ResourceLoader.asString(this, "/world-europe-26333587Remarks.html");

        assertEquals(complexHTMLRemark.trim(), HTMLDocument.stripRemarks(complexHTML));

    }

    /**
     *
     */
    @Test public void testSimpleStripScripts() throws IOException {

        String simpleHTML = ResourceLoader.asString(this, "/simple.html");

        String simpleHTMLScript = ResourceLoader.asString(this, "/simpleScripts.html");

        assertEquals(simpleHTMLScript.trim(), HTMLDocument.stripScripts(simpleHTML));

    }

    /**
     *
     */
    @Test public void testComplexStripScripts() throws IOException {

        String complexHTML = ResourceLoader.asString(this, "/world-europe-26333587.html");

        String complexHTMLScript = ResourceLoader.asString(this, "/world-europe-26333587Scripts.html");

        assertEquals(complexHTMLScript.trim(), HTMLDocument.stripScripts(complexHTML));

    }

    /**
     *
     */
    @Test public void testSimpleStripStyles() throws IOException {

        String simpleHTML = ResourceLoader.asString(this, "/simple.html");

        String simpleHTMLStyles = ResourceLoader.asString(this, "/simpleStyles.html");

        assertEquals(simpleHTMLStyles.trim(), HTMLDocument.stripStyles(simpleHTML));

    }

    /**
     *
     */
    @Test public void testComplexStripStyles() throws IOException {

        String complexHTML = ResourceLoader.asString(this, "/world-europe-26333587.html");

        String complexHTMLStyles = ResourceLoader.asString(this, "/world-europe-26333587Styles.html");

        assertEquals(complexHTMLStyles.trim(), HTMLDocument.stripStyles(complexHTML));

    }

    /**
     *
     */
    @Test public void testSimpleStripWhitespace() throws IOException {

        String simpleHTML = ResourceLoader.asString(this, "/simple.html");

        String simpleHTMLWhitespace = ResourceLoader.asString(this, "/simpleWhitespace.html");

        assertEquals(simpleHTMLWhitespace.trim(), HTMLDocument.stripWhitespace(simpleHTML));

    }

    /**
     *
     */
    @Test public void testComplexStripWhitespace() throws IOException {

        String complexHTML = ResourceLoader.asString(this, "/world-europe-26333587.html");

        String complexHTMLWhitespace = ResourceLoader.asString(this, "/world-europe-26333587Whitespace.html");

        assertEquals(complexHTMLWhitespace.trim(), HTMLDocument.stripWhitespace(complexHTML));

    }

    /**
     *
     */
    @Test public void testSmoothingKernelThree() throws IOException {

        String html = ResourceLoader.asString(this, "/simple.html");

        String csv = ResourceLoader.asString(this, "/simpleSmoothedKernelThree_0-25_0-5_0-25.csv");

        LinkedHashMap<Integer, String>[] knownValues = CSV.toTable(csv);

        HTMLLine[] lines = HTMLDocument.getLines(html);

        HTMLLine[] linesSmoothed = HTMLDocument.smooth(lines, new double[] {0.25, 0.5, 0.25});

        for (int i = 0; i < linesSmoothed.length; i++) {

            assertEquals(Double.parseDouble(knownValues[i].get(2)), linesSmoothed[i].getSmoothedTextTagRatio(), 0);

        }

    }

    /**
     *
     */
    @Test(expected = InvalidKernelException.class) public void testSmoothingEvenKernel() throws IOException {

        String html = ResourceLoader.asString(this, "/simple.html");

        HTMLLine[] lines = HTMLDocument.getLines(html);

        HTMLLine[] linesSmoothed = HTMLDocument.smooth(lines, new double[]{0.25, 0.5});

    }

    /**
     *
     * @throws InvalidKernelException
     */
    @Test public void testSmoothingEmptyArray() throws InvalidKernelException {

        HTMLLine[] lines = HTMLDocument.getLines("");

        HTMLLine[] linesSmoothed = HTMLDocument.smooth(lines, new double[]{0.25, 0.5, 0.25});

        assertArrayEquals(lines, linesSmoothed);

    }

}
