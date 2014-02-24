package com.joshmahony;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by joshmahony on 24/02/2014.
 */
public class HTMLLineTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public HTMLLineTest( String testName ) {

        super( testName );

    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {

        return new TestSuite( HTMLLineTest.class );

    }

    /**
     * Test line count
     */
    public void testLineCount() {

        String s1 = "<p>The <a href=\"http://imberbus.wordpress.com/\" >23A service takes in other remote locations on the plain</a>, " +
                "including New Zealand Farm Camp and Brazen Bottom and &quot;is a normal bus service and registered with the traffic commissioners&quot;, says Meilton.</p>";

        int s1tc = 2, s1wc = 33;
        double s1ttr = (double) s1wc / (double) s1tc;

        HTMLLine htmlLine1 = new HTMLLine(s1);

        assertEquals(htmlLine1.tagsCount, s1tc);
        assertEquals(htmlLine1.textCount, s1wc);
        assertEquals(htmlLine1.textTagRatio, s1ttr, 0);

        String s2 = "<div class=\"livestats-web-bug\"><img alt=\"\" id=\"livestats\" src=\"http://stats.bbc.co.uk/o.gif?~RS~s~R" +
                "S~News~RS~t~RS~HighWeb_Story~RS~i~RS~26222202~RS~p~RS~99277~RS~a~RS~Domestic~RS~u~RS~/news/entertainment-arts-26222202~RS~q~RS~~RS~z~RS~32~RS~\"/></div>";

        int s2tc = 2, s2wc = 0;
        double s2ttr = (double) s2wc / (double) s2tc;

        HTMLLine htmlLine2 = new HTMLLine(s2);

        assertEquals(htmlLine2.tagsCount, s2tc);
        assertEquals(htmlLine2.textCount, s2wc);
        assertEquals(htmlLine2.textTagRatio, s2ttr, 0);

        String s3 = "<li><a href=\"/news/education/\">Education</a></li>";
        int s3tc = 2, s3wc = 1;
        double s3ttr = (double) s3wc / (double) s3tc;

        HTMLLine htmlLine3 = new HTMLLine(s3);

        assertEquals(htmlLine3.tagsCount, s3tc);
        assertEquals(htmlLine3.textCount, s3wc);
        assertEquals(htmlLine3.textTagRatio, s3ttr, 0);

        String s4 = " <a title=\"Post this story to Delicious\" href=\"http://del.icio.us/post?url=http://www.bbc.co.uk/news/entertainment-arts-26222202&amp;title=BBC+News+-" +
                "+Baftas+2014%3A+Certainties%2C+surprises+and+Oscar+predictions\">Delicious</a>";
        int s4tc = 1;
        int s4wc = 1;
        double s4ttr = (double) s4wc / (double) s4tc;

        HTMLLine htmlLine4 = new HTMLLine(s4);

        assertEquals(htmlLine4.tagsCount, s4tc);
        assertEquals(htmlLine4.textCount, s4wc);
        assertEquals(htmlLine4.textTagRatio, s4ttr, 0);

    }

}
