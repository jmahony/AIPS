package com.joshmahony;

import com.joshmahony.exceptions.InvalidKernelException;
import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
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

        String simpleHTML = getResource("/simple.html");

        int lines1 = 83;

        assertEquals(lines1, HTMLDocument.getLines(simpleHTML).length);

        String complexHTML = getResource("/world-europe-26333587.html");

        int lines2 = 2968;

        assertEquals(lines2, HTMLDocument.getLines(complexHTML).length);

    }

    /**
     *
     * Test the simple HTML document to see if the body is correctly extracted
     *
     */
    @Test public void testSimpleGetBody() {

        String simpleHTML = getResource("/simple.html");

        String simpleHTMLBody = getResource("/simpleBody.html");

        assertEquals(simpleHTMLBody.trim(), HTMLDocument.getBody(simpleHTML));

    }

    /**
     *
     * Test the complex HTML document to see if the body is correctly extracted
     *
     */
    @Test public void testComplexGetBody() {

        String complexHTML = getResource("/world-europe-26333587.html");

        String complexHTMLBody = getResource("/world-europe-26333587Body.html");

        assertEquals(complexHTMLBody.trim(), HTMLDocument.getBody(complexHTML));

    }

    /**
     *
     */
    @Test public void testSimpleStripRemarks() {

        String simpleHTML = getResource("/simple.html");

        String simpleHTMLRemark = getResource("/simpleRemarks.html");

        assertEquals(simpleHTMLRemark.trim(), HTMLDocument.stripRemarks(simpleHTML));

    }

    /**
     *
     */
    @Test public void testComplexStripRemarks() {

        String complexHTML = getResource("/world-europe-26333587.html");

        String complexHTMLRemark = getResource("/world-europe-26333587Remarks.html");

        assertEquals(complexHTMLRemark.trim(), HTMLDocument.stripRemarks(complexHTML));

    }

    /**
     *
     */
    @Test public void testSimpleStripScripts() {

        String simpleHTML = getResource("/simple.html");

        String simpleHTMLScript = getResource("/simpleScripts.html");

        assertEquals(simpleHTMLScript.trim(), HTMLDocument.stripScripts(simpleHTML));

    }

    /**
     *
     */
    @Test public void testComplexStripScripts() {

        String complexHTML = getResource("/world-europe-26333587.html");
        
        String complexHTMLScript = getResource("/world-europe-26333587Scripts.html");

        assertEquals(complexHTMLScript.trim(), HTMLDocument.stripScripts(complexHTML));

    }

    /**
     *
     */
    @Test public void testSimpleStripStyles() {

        String simpleHTML = getResource("/simple.html");

        String simpleHTMLStyles = getResource("/simpleStyles.html");

        assertEquals(simpleHTMLStyles.trim(), HTMLDocument.stripStyles(simpleHTML));

    }

    /**
     *
     */
    @Test public void testComplexStripStyles() {

        String complexHTML = getResource("/world-europe-26333587.html");

        String complexHTMLStyles = getResource("/world-europe-26333587Styles.html");

        assertEquals(complexHTMLStyles.trim(), HTMLDocument.stripStyles(complexHTML));

    }

    /**
     *
     */
    @Test public void testSimpleStripWhitespace() {

        String simpleHTML = getResource("/simple.html");

        String simpleHTMLWhitespace = getResource("/simpleWhitespace.html");

        assertEquals(simpleHTMLWhitespace.trim(), HTMLDocument.stripWhitespace(simpleHTML));

    }

    /**
     *
     */
    @Test public void testComplexStripWhitespace() {

        String complexHTML = getResource("/world-europe-26333587.html");

        String complexHTMLWhitespace = getResource("/world-europe-26333587Whitespace.html");

        assertEquals(complexHTMLWhitespace.trim(), HTMLDocument.stripWhitespace(complexHTML));

    }

    /**
     *
     * @throws Exception
     */
    @Test public void testSmoothingKernelThree() throws Exception {
        
        String html = getResource("/simple.html");

        String csv = getResource("/simpleSmoothedKernelThree_0-25_0-5_0-25.csv");

        LinkedHashMap<Integer, String>[] knownValues = csvToTable(csv);
        
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

        String html = getResource("/simple.html");

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

    /**
     * 
     * Returns a csv file as a hashmap
     * 
     * @param csv
     * @return a hasmap of 
     * @throws java.io.IOException
     */
    private LinkedHashMap[] csvToTable(String csv) {
        
        String[] rows = csv.split("\n");

        LinkedHashMap<Integer, String>[] table = new LinkedHashMap[rows.length];
        
        int rowIndex = 0;
        
        for (String row : rows) {

            String[] columns = row.split(",");
            
            LinkedHashMap<Integer, String> newRow = new LinkedHashMap<Integer, String>();
                        
            for (int i = 0; i < columns.length; i++) {
                
                newRow.put(i, columns[i]);
                
            }
            
            table[rowIndex++] = newRow;
            
        }

        return table;
        
    }

    /**
     * 
     * Returns a resource as a string
     * 
     * @param resource
     * @return
     * @throws IOException
     */
    private String getResource(String resource) {

        try {

            return IOUtils.toString(
                    this.getClass().getResourceAsStream(resource),
                    "UTF-8"
            );

        } catch (Exception e) {

            return null;

        }

    }

}
