package es.cnewsbit;

import es.cnewsbit.htmlutils.ScriptsStripper;
import es.cnewsbit.utilities.ResourceLoader;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by joshmahony on 27/04/2014.
 */
public class ScriptStripperTest {

    /**
     *
     */
    @Test
    public void testSimpleStripScripts() throws IOException {

        String simpleHTML = ResourceLoader.asString(this, "/simple.html");

        String simpleHTMLScript = ResourceLoader.asString(this, "/simpleScripts.html");

        assertEquals(simpleHTMLScript.trim(), ScriptsStripper.strip(simpleHTML));

    }

    /**
     *
     */
    @Test public void testComplexStripScripts() throws IOException {

        String complexHTML = ResourceLoader.asString(this, "/world-europe-26333587.html");

        String complexHTMLScript = ResourceLoader.asString(this, "/world-europe-26333587Scripts.html");

        assertEquals(complexHTMLScript.trim(), ScriptsStripper.strip(complexHTML));

    }

}
