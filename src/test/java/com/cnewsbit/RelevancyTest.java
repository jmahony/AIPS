package com.cnewsbit;

import com.cnewsbit.measures.Relevancy;
import org.junit.*;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by josh on 02/03/14.
 */
public class RelevancyTest {

    /**
     * Test the f measure method
     */
    @Test public void testFMeasureBasic() {

        double precision = 100.00d;

        double recall = 63.15d;

        double expected = 77.41342323;

        assertEquals(expected, Relevancy.fMeasure(precision, recall), C.DOUBLE_TEST_DELTA);

    }

    /**
     * Test the f measure method with precision negative
     */
    @Test(expected = InvalidParameterException.class) public void testFMeasurePrecisionNegative() {

        double precision = -2.00d;

        double recall = 63.15d;

        Relevancy.fMeasure(precision, recall);

    }

    /**
     * Test the f measure method with recall negative
     */
    @Test(expected = InvalidParameterException.class) public void testFMeasureRecallNegative() {

        double precision = 50.00d;

        double recall = -63.15d;

        Relevancy.fMeasure(precision, recall);

    }

    /**
     * Test the f measure method with recall negative
     */
    @Test(expected = InvalidParameterException.class) public void testFMeasureRecallPrecisionNegative() {

        double precision = -50.00d;

        double recall = -63.15d;

        Relevancy.fMeasure(precision, recall);

    }

    /**
     * Test the f measure with precision greater than 100
     */
    @Test(expected = InvalidParameterException.class) public void testFMeasurePrecisionHigh() {

        double precision = 100.000001d;

        double recall = 0.00d;

        Relevancy.fMeasure(precision, recall);

    }

    /**
     * Test the f measure with recall greater than 100
     */
    @Test(expected = InvalidParameterException.class) public void testFMeasureRecallHigh() {

        double precision = 0.00d;

        double recall = 100.000001d;

        Relevancy.fMeasure(precision, recall);

    }

    /**
     * Test the f measure method with two 0s
     */
    @Test public void testFMeasureZeroes() {

        double precision = 0.00d;

        double recall = 0.00d;

        assertEquals(0.00d, Relevancy.fMeasure(precision, recall), C.DOUBLE_TEST_DELTA);

    }

    /**
     * Test the f measure method with precision 0
     */
    @Test public void testFMeasurePrecisionZero() {

        double precision = 0.00d;

        double recall = 50.00d;

        double expected = 0.00d;

        assertEquals(expected, Relevancy.fMeasure(precision, recall), C.DOUBLE_TEST_DELTA);

    }

    /**
     * Test the f measure method with recall 0
     */
    @Test public void testFMeasureRecallZero() {

        double precision = 50.00d;

        double recall = 0.00d;

        double expected = 0.00d;

        assertEquals(expected, Relevancy.fMeasure(precision, recall), C.DOUBLE_TEST_DELTA);

    }

    /**
     * Test precision and recall basic
     */
    @Test public void testPrecisionRecallBasic() {

        double expectedPrecision, expectedRecall;

        Set<Integer> returned, relevant;

        relevant = new HashSet<Integer>(){{

            add(3);
            add(4);
            add(8);
            add(9);
            add(14);
            add(16);
            add(18);
            add(19);
            add(23);
            add(24);
            add(29);

        }};

        // ------- Test One -------
        returned = new HashSet<Integer>(){{

            add(0); // wrong
            add(3); // right
            add(4); // right
            add(6); // wrong
            add(7); // wrong

        }};

        expectedPrecision = 40.00d;
        assertEquals(expectedPrecision, Relevancy.precision(relevant, returned), C.DOUBLE_TEST_DELTA);

        expectedRecall = 18.1818d;
        assertEquals(expectedRecall, Relevancy.recall(relevant, returned), C.DOUBLE_TEST_DELTA);

        // ------- Test Two -------
        returned = new HashSet<Integer>(){{

            add(29); // right
            add(3); // right
            add(4); // right
            add(24); // right
            add(18); // right

        }};

        expectedPrecision = 100.00d;
        assertEquals(expectedPrecision, Relevancy.precision(relevant, returned), C.DOUBLE_TEST_DELTA);

        expectedRecall = 45.4545d;
        assertEquals(expectedRecall, Relevancy.recall(relevant, returned), C.DOUBLE_TEST_DELTA);

        // ------- Test Three -------
        returned = new HashSet<Integer>(){{

            add(3); // right
            add(4); // right
            add(8); // right
            add(9); // right
            add(14); // right
            add(16); // right
            add(18); // right
            add(19); // right
            add(23); // right
            add(24); // right
            add(29); // right

        }};

        expectedPrecision = 100.00d;
        assertEquals(expectedPrecision, Relevancy.precision(relevant, returned), C.DOUBLE_TEST_DELTA);

        expectedRecall = 100.00d;
        assertEquals(expectedRecall, Relevancy.recall(relevant, returned), C.DOUBLE_TEST_DELTA);

    }

    /**
     * Test precision empty set
     */
    @Test public void testPrecisionBothEmpty() {

        Set<Integer> returned, relevant;

        returned = new HashSet<Integer>();

        relevant = new HashSet<Integer>();

        double precision = Relevancy.precision(relevant, returned);

        assertEquals(0.00d, precision, C.DOUBLE_TEST_DELTA);

    }

    /**
     * Test precision empty set
     */
    @Test public void testPrecisionReturnedEmpty() {

        Set<Integer> returned, relevant;

        returned = new HashSet<Integer>();

        relevant = new HashSet<Integer>(){{
               add(1);
        }};

        double precision = Relevancy.precision(relevant, returned);

        assertEquals(0.00d, precision, C.DOUBLE_TEST_DELTA);

    }


    /**
     * Test precision empty set
     */
    @Test public void testPrecisionRelevantEmpty() {

        Set<Integer> returned, relevant;

        returned = new HashSet<Integer>(){{
            add(1);
        }};

        relevant = new HashSet<Integer>();

        double precision = Relevancy.precision(relevant, returned);

        assertEquals(0.00d, precision, C.DOUBLE_TEST_DELTA);

    }


    /**
     * Test recall empty set
     */
    @Test public void testRecallBothEmpty() {

        Set<Integer> returned, relevant;

        returned = new HashSet<Integer>();

        relevant = new HashSet<Integer>();

        double recall = Relevancy.recall(relevant, returned);

        assertEquals(0.00d, recall, C.DOUBLE_TEST_DELTA);

    }


    /**
     * Test recall empty set
     */
    @Test public void testRecallReturnedEmpty() {

        Set<Integer> returned, relevant;

        returned = new HashSet<>();

        relevant = new HashSet<Integer>(){{
            add(1);
        }};

        double recall = Relevancy.recall(relevant, returned);

        assertEquals(0.00d, recall, C.DOUBLE_TEST_DELTA);

    }


    /**
     * Test recall empty set
     */
    @Test public void testRecallRelevantEmpty() {

        Set<Integer> returned, relevant;

        returned = new HashSet<Integer>(){{
            add(1);
        }};

        relevant = new HashSet<Integer>();

        double recall = Relevancy.recall(relevant, returned);

        assertEquals(0.00d, recall, C.DOUBLE_TEST_DELTA);

    }


    /**
     * Test all together
     */
    @Test public void testRecallPrecisionFMeasure() {

        double expectedPrecision,
               expectedRecall,
               expectedFMeasure,
               actualPrecision,
               actualRecall,
               actualFMeasure;

        Set<Integer> returned, relevant;

        relevant = new HashSet<Integer>(){{

            add(3);
            add(4);
            add(8);
            add(9);
            add(14);
            add(16);
            add(18);
            add(19);
            add(23);
            add(24);
            add(29);

        }};

        returned = new HashSet<Integer>(){{

            add(0); // wrong
            add(3); // right
            add(4); // right
            add(6); // wrong
            add(7); // wrong
            add(16); // right
            add(9); // right
            add(63); // wrong
            add(21); // wrong
            add(61); // wrong

        }};

        expectedPrecision = 40.00d;
        actualPrecision   = Relevancy.precision(relevant, returned);
        assertEquals(expectedPrecision, actualPrecision, C.DOUBLE_TEST_DELTA);

        expectedRecall = 36.36363636d;
        actualRecall   = Relevancy.recall(relevant, returned);
        assertEquals(expectedRecall, actualRecall, C.DOUBLE_TEST_DELTA);

        expectedFMeasure = 38.09523809d;
        actualFMeasure   = Relevancy.fMeasure(actualPrecision, actualRecall);
        assertEquals(expectedFMeasure, actualFMeasure, C.DOUBLE_TEST_DELTA);

    }
}
