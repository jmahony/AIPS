package com.joshmahony;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * Created by joshmahony on 25/02/2014.
 */
public class HTMLDocumentTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public HTMLDocumentTest( String testName ) {

        super( testName );

    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {

        return new TestSuite( HTMLDocumentTest.class );

    }

    /**
     *
     * Make the right amount of lines are taken from the document, the test numbers are counted manually.
     *
     * @throws IOException
     */
    public void testGetLines() throws IOException {

        String simpleHTML = getResource("/simple.html");

        int lines1 = 64;

        assertEquals(lines1, HTMLDocument.getLines(simpleHTML).length);

        String complexHTML = getResource("/world-europe-26333587.html");

        int lines2 = 2968;

        assertEquals(lines2, HTMLDocument.getLines(complexHTML).length);

    }

    /**
     *
     * Test the simple HTML document to see if the body is correctly extracted
     *
     * @throws IOException
     */
    public void testSimpleGetBody() throws IOException {

        String simpleHTML = getResource("/simple.html");

        String simpleHTMLBody = getResource("/simpleBody.html");

        assertEquals(simpleHTMLBody.trim(), HTMLDocument.getBody(simpleHTML));

    }

    /**
     *
     * Test the complex HTML document to see if the body is correctly extracted
     *
     * @throws IOException
     */
    public void testComplexGetBody() throws IOException {

        String complexHTML = getResource("/world-europe-26333587.html");

        String complexHTMLBody = getResource("/world-europe-26333587Body.html");

        assertEquals(complexHTMLBody.trim(), HTMLDocument.getBody(complexHTML));

    }

    /**
     * Returns a resource as a string
     * @param resource
     * @return
     * @throws IOException
     */
    private String getResource(String resource) throws IOException {

        return IOUtils.toString(
            this.getClass().getResourceAsStream(resource),
            "UTF-8"
        );

    }

}
