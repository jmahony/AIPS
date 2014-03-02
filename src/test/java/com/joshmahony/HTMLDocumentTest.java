package com.joshmahony;

import com.joshmahony.exceptions.InvalidKernelException;
import com.joshmahony.utility.CSV;
import com.joshmahony.utility.ResourceLoader;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.LinkedHashMap;

/**
 * Created by joshmahony on 25/02/2014.
 */
public class HTMLDocumentTest {

    /**
     *
     * Make the right amount of lines are taken from the document, the test numbers are counted manually.
     *
     */
    @Test public void testGetLines() {

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
    @Test public void testSimpleGetBody() {

        String simpleHTML = ResourceLoader.asString(this, "/simple.html");

        String simpleHTMLBody = ResourceLoader.asString(this, "/simpleBody.html");

        assertEquals(simpleHTMLBody.trim(), HTMLDocument.getBody(simpleHTML));

    }

    /**
     *
     * Test the complex HTML document to see if the body is correctly extracted
     *
     */
    @Test public void testComplexGetBody() {

        String complexHTML = ResourceLoader.asString(this, "/world-europe-26333587.html");

        String complexHTMLBody = ResourceLoader.asString(this, "/world-europe-26333587Body.html");

        assertEquals(complexHTMLBody.trim(), HTMLDocument.getBody(complexHTML));

    }

    /**
     *
     */
    @Test public void testSimpleStripRemarks() {

        String simpleHTML = ResourceLoader.asString(this, "/simple.html");

        String simpleHTMLRemark = ResourceLoader.asString(this, "/simpleRemarks.html");

        assertEquals(simpleHTMLRemark.trim(), HTMLDocument.stripRemarks(simpleHTML));

    }

    /**
     *
     */
    @Test public void testComplexStripRemarks() {

        String complexHTML = ResourceLoader.asString(this, "/world-europe-26333587.html");

        String complexHTMLRemark = ResourceLoader.asString(this, "/world-europe-26333587Remarks.html");

        assertEquals(complexHTMLRemark.trim(), HTMLDocument.stripRemarks(complexHTML));

    }

    /**
     *
     */
    @Test public void testSimpleStripScripts() {

        String simpleHTML = ResourceLoader.asString(this, "/simple.html");

        String simpleHTMLScript = ResourceLoader.asString(this, "/simpleScripts.html");

        assertEquals(simpleHTMLScript.trim(), HTMLDocument.stripScripts(simpleHTML));

    }

    /**
     *
     */
    @Test public void testComplexStripScripts() {

        String complexHTML = ResourceLoader.asString(this, "/world-europe-26333587.html");
        
        String complexHTMLScript = ResourceLoader.asString(this, "/world-europe-26333587Scripts.html");

        assertEquals(complexHTMLScript.trim(), HTMLDocument.stripScripts(complexHTML));

    }

    /**
     *
     */
    @Test public void testSimpleStripStyles() {

        String simpleHTML = ResourceLoader.asString(this, "/simple.html");

        String simpleHTMLStyles = ResourceLoader.asString(this, "/simpleStyles.html");

        assertEquals(simpleHTMLStyles.trim(), HTMLDocument.stripStyles(simpleHTML));

    }

    /**
     *
     */
    @Test public void testComplexStripStyles() {

        String complexHTML = ResourceLoader.asString(this, "/world-europe-26333587.html");

        String complexHTMLStyles = ResourceLoader.asString(this, "/world-europe-26333587Styles.html");

        assertEquals(complexHTMLStyles.trim(), HTMLDocument.stripStyles(complexHTML));

    }

    /**
     *
     */
    @Test public void testSimpleStripWhitespace() {

        String simpleHTML = ResourceLoader.asString(this, "/simple.html");

        String simpleHTMLWhitespace = ResourceLoader.asString(this, "/simpleWhitespace.html");

        assertEquals(simpleHTMLWhitespace.trim(), HTMLDocument.stripWhitespace(simpleHTML));

    }

    /**
     *
     */
    @Test public void testComplexStripWhitespace() {

        String complexHTML = ResourceLoader.asString(this, "/world-europe-26333587.html");

        String complexHTMLWhitespace = ResourceLoader.asString(this, "/world-europe-26333587Whitespace.html");

        assertEquals(complexHTMLWhitespace.trim(), HTMLDocument.stripWhitespace(complexHTML));

    }

    /**
     *
     * @throws Exception
     */
    @Test public void testSmoothingKernelThree() throws Exception {
        
        String html = ResourceLoader.asString(this, "/simple.html");

        String csv = ResourceLoader.asString(this, "/simpleSmoothedKernelThree_0-25_0-5_0-25.csv");

        LinkedHashMap<Integer, String>[] knownValues = CSV.toTable(csv);
        
        HTMLLine[] lines = HTMLDocument.getLines(html);
        
        HTMLLine[] linesSmoothed = HTMLDocument.smooth(lines, new double[] {0.25, 0.5, 0.25});

        for (int i = 0; i < linesSmoothed.length; i++) {
                        
            assertEquals(Double.parseDouble(knownValues[i].get(2)), linesSmoothed[i].smoothedtTextTagRatio, 0);

        }

    }

    /**
     *
     */
    @Test(expected = InvalidKernelException.class) public void testSmoothingEvenKernel() {

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
