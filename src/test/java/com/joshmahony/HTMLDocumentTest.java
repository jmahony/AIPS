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
     * Test line count
     */
    public void testGetLines() throws IOException {

        String simpleHTML = IOUtils.toString(
            this.getClass().getResourceAsStream("/simple.html"),
            "UTF-8"
        );

        int lines1 = 64;

        assertEquals(lines1, HTMLDocument.getLines(simpleHTML).length);

        String complexHTML = IOUtils.toString(
            this.getClass().getResourceAsStream("/world-europe-26333587.html"),
            "UTF-8"
        );

        int lines2 = 2968;

        assertEquals(lines2, HTMLDocument.getLines(complexHTML).length);

    }



}
