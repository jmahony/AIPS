package es.cnewsbit;

import es.cnewsbit.exceptions.InvalidKernelException;
import es.cnewsbit.extractors.GaussianSmoother;
import es.cnewsbit.extractors.Smoothable;
import es.cnewsbit.extractors.Smoother;
import es.cnewsbit.utilities.CSV;
import es.cnewsbit.utilities.HTMLLineFactory;
import es.cnewsbit.utilities.ResourceLoader;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;
/**
 * Created by joshmahony on 27/04/2014.
 */
public class GaussianSmootherTest {

    /**
     *
     */
    @Test public void testSmoothingKernelThree() throws IOException {

        String html = ResourceLoader.asString(this, "/simple.html");

        String csv = ResourceLoader.asString(this, "/simpleSmoothedKernelThree_0-25_0-5_0-25.csv");

        LinkedHashMap<Integer, String>[] knownValues = CSV.toTable(csv);

        Smoothable[] originalLines = HTMLLineFactory.build(html);

        Smoother smoother = new GaussianSmoother();

        Smoothable[] smoothedLines = smoother.smooth(originalLines);

        for (int i = 0; i < smoothedLines.length; i++) {

            assertEquals(Double.parseDouble(knownValues[i].get(2)), smoothedLines[i].getSmoothedValue(), 0);

        }

    }

    /**
     *
     */
    @Test(expected = InvalidKernelException.class) public void testSmoothingEvenKernel() throws IOException {

        String html = ResourceLoader.asString(this, "/simple.html");

        Smoothable[] originalLines = HTMLLineFactory.build(html);

        GaussianSmoother smoother = new GaussianSmoother();

        smoother.smooth(originalLines, new Double[]{0.25, 0.5});

    }

    /**
     *
     * @throws InvalidKernelException
     */
    @Test public void testSmoothingEmptyArray() throws InvalidKernelException {

        Smoothable[] originalLines = HTMLLineFactory.build("");

        GaussianSmoother smoother = new GaussianSmoother();

        Smoothable[] linesSmoothed = smoother.smooth(originalLines, new Double[]{0.25, 0.5, 0.25});

        assertArrayEquals(originalLines, linesSmoothed);

    }

}
