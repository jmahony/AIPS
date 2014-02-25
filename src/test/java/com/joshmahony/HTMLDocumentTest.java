package com.joshmahony;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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

        return new TestSuite( HTMLDocument.class );

    }

    /**
     * Test line count
     */
    public void testLineCount() {



    }

}
