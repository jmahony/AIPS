package es.cnewsbit;

import es.cnewsbit.htmlutils.BodyExtractor;
import es.cnewsbit.utilities.ResourceLoader;
import org.junit.Test;

import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Created by joshmahony on 27/04/2014.
 */
public class BodyExtractorTest {

    /**
     *
     * Test the simple HTML document to see if the body is correctly extracted
     *
     */
    @Test
    public void testSimpleGetBody() throws IOException {

        String simpleHTML = ResourceLoader.asString(this, "/simple.html");

        String simpleHTMLBody = ResourceLoader.asString(this, "/simpleBody.html");

        assertEquals(simpleHTMLBody.trim(), BodyExtractor.extract(simpleHTML));

    }

}
