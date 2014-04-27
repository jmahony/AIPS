package es.cnewsbit;

import es.cnewsbit.htmlutils.WhitespaceStripper;
import es.cnewsbit.utilities.ResourceLoader;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by joshmahony on 27/04/2014.
 */
public class WhitespaceStripperTest {

    /**
     *
     */
    @Test
    public void testSimpleStripWhitespace() throws IOException {

        String simpleHTML = ResourceLoader.asString(this, "/simple.html");

        String simpleHTMLWhitespace = ResourceLoader.asString(this, "/simpleWhitespace.html");

        assertEquals(simpleHTMLWhitespace.trim(), WhitespaceStripper.strip(simpleHTML));

    }

    /**
     *
     */
    @Test public void testComplexStripWhitespace() throws IOException {

        String complexHTML = ResourceLoader.asString(this, "/world-europe-26333587.html");

        String complexHTMLWhitespace = ResourceLoader.asString(this, "/world-europe-26333587Whitespace.html");

        assertEquals(complexHTMLWhitespace.trim(),WhitespaceStripper.strip(complexHTML));

    }

}
