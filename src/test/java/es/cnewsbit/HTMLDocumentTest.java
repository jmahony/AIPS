package es.cnewsbit;

/**
 * Created by cnewsbit on 25/02/2014.
 */
public class HTMLDocumentTest {

    /**
     *
     * Make the right amount of lines are taken from the document, the test numbers are counted manually.
     *
     */
    /*@Test public void testGetLines() throws IOException {

        String simpleHTML = ResourceLoader.asString(this, "/simple.html");

        int lines1 = 83;

        assertEquals(lines1, HTMLDocument.getLines(simpleHTML).length);

        String complexHTML = ResourceLoader.asString(this, "/world-europe-26333587.html");

        int lines2 = 2968;

        assertEquals(lines2, HTMLDocument.getLines(complexHTML).length);

    }*/

    /**
     *
     */
    /*@Test public void testSmoothingKernelThree() throws IOException {

        String html = ResourceLoader.asString(this, "/simple.html");

        String csv = ResourceLoader.asString(this, "/simpleSmoothedKernelThree_0-25_0-5_0-25.csv");

        LinkedHashMap<Integer, String>[] knownValues = CSV.toTable(csv);

        HTMLLine[] lines = HTMLDocument.getLines(html);

        HTMLLine[] linesSmoothed = HTMLDocument.smooth(lines, new double[] {0.25, 0.5, 0.25});

        for (int i = 0; i < linesSmoothed.length; i++) {

            assertEquals(Double.parseDouble(knownValues[i].get(2)), linesSmoothed[i].getSmoothedTextTagRatio(), 0);

        }

    }

    /**
     *
     */
    /*@Test(expected = InvalidKernelException.class) public void testSmoothingEvenKernel() throws IOException {

        String html = ResourceLoader.asString(this, "/simple.html");

        HTMLLine[] lines = HTMLDocument.getLines(html);

        HTMLLine[] linesSmoothed = HTMLDocument.smooth(lines, new double[]{0.25, 0.5});

    }

    /**
     *
     * @throws InvalidKernelException
     */
    /*@Test public void testSmoothingEmptyArray() throws InvalidKernelException {

        HTMLLine[] lines = HTMLDocument.getLines("");

        HTMLLine[] linesSmoothed = HTMLDocument.smooth(lines, new double[]{0.25, 0.5, 0.25});

        assertArrayEquals(lines, linesSmoothed);

    }*/

}
