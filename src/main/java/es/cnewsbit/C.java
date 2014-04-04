package es.cnewsbit;

/**
 * Created by josh on 02/03/14.
 */
public class C {

    /**
     * Used in the tests when comparing two doubles, its the allowed delta between
     * the two.
     * TODO: Maybe move this
     */
    public static final double DOUBLE_TEST_DELTA = 0.001d;

    public static final int LOWER_BOUND_EXTRACTION_THRESHOLD = 10;

    public static final int UPPER_BOUND_EXTRACTION_THRESHOLD = 100;

    public static final double[] SMOOTHING_KERNEL = new double[] {0.25, 0.50, 0.25};
}
