package es.cnewsbit;

import java.util.HashMap;

/**
 * Global constants
 */
public class C {

    /**
     * Used in the tests when comparing two doubles, its the allowed delta between
     * the two.
     */
    public static final double DOUBLE_TEST_DELTA = 0.001d;

    /**
     * The lowest ratio allowed in the content extraction
     */
    public static final int LOWER_BOUND_EXTRACTION_THRESHOLD = 10;
    /**
     * The highest ratio allowed in the content extraction
     */
    public static final int UPPER_BOUND_EXTRACTION_THRESHOLD = 100;

    /**
     * Kernel used by the smoothing algorithm
     */
    public static final double[] SMOOTHING_KERNEL = new double[] {0.25, 0.50, 0.25};

    /**
     * The location of the Lucene index
     */
    public static final String PATH_TO_INDEX = "/lucene/index/";

    /**
     * Whether to use Boilerpipe for content extraction
     */
    public static final boolean BOILERPIPE = true;

    /**
     * A mapping of which domain uses which class
     */
    @SuppressWarnings("unchecked")
    public static final HashMap<String, String> ARTICLE_CLASS_MAP = new HashMap() {{
        put("www.bbc.co.uk",   "BBCNewsArticle");
        put("uk.reuters.com",  "UKReutersNewsArticle");
        put("news.sky.com",    "SkyNewsArticle");
        put("www.nbcnews.com", "NBCNews");
    }};

    /**
     * Local database settings
     */
    public static final String DB_NAME = "jdbc:mysql://localhost:3306/cnewsbites_v2?rewriteBatchedStatements=true";
    public static final String DB_USER = "piles";
    public static final String DB_PASSWORD = "12101210";

    /**
     * Production database settings
     */
/*
    public static final String DB_NAME = "jdbc:mysql://cnewsbites-prod.crsa1yeccgr5.eu-west-1.rds.amazonaws.com:3306/cnewsbites_prod?rewriteBatchedStatements=true";
    public static final String DB_USER = "cnewsbites";
    public static final String DB_PASSWORD = "&6&g2DI9i0A%";
*/
    /**
     * How many connections in the pool
     */
    public static final int DB_POOL_SIZE = 4;

    /**
     * How many items to batch up before sending to the database
     */
    public static final int DB_BATCH_SIZE = 50;

    /**
     * How many processors threads to create
     */
    public static final int NO_OF_PROCESSORS = 4;
}
