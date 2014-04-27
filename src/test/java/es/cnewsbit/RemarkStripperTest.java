package es.cnewsbit;

import es.cnewsbit.htmlutils.RemarkStripper;
import es.cnewsbit.utilities.ResourceLoader;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by joshmahony on 27/04/2014.
 */
public class RemarkStripperTest {

    /**
     *
     */
    @Test
    public void testSimpleStripRemarks() throws IOException {

        String simpleHTML = ResourceLoader.asString(this, "/simple.html");

        String simpleHTMLRemark = ResourceLoader.asString(this, "/simpleRemarks.html");

        assertEquals(simpleHTMLRemark.trim(), RemarkStripper.strip(simpleHTML));

    }

    /**
     *
     */
    @Test public void testComplexStripRemarks() throws IOException {

        String complexHTML = ResourceLoader.asString(this, "/world-europe-26333587.html");

        String complexHTMLRemark = ResourceLoader.asString(this, "/world-europe-26333587Remarks.html");

        assertEquals(complexHTMLRemark.trim(), RemarkStripper.strip(complexHTML));

    }

}
