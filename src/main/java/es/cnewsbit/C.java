package es.cnewsbit;

import java.util.HashMap;

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

    public static final String PATH_TO_INDEX = "/lucene/index/";

    public static final boolean BOILERPIPE = true;

    public static final HashMap<String, String> ARTICLE_CLASS_MAP= new HashMap() {{
        put("www.bbc.co.uk",   "BBCNewsArticle");
        put("uk.reuters.com",  "UKReutersNewsArticle");
        put("news.sky.com",    "SkyNewsArticle");
        put("www.nbcnews.com", "NBCNews");
    }};
/*
    public static final String DB_NAME = "jdbc:mysql://localhost:3306/cnewsbites_v2?rewriteBatchedStatements=true";
    public static final String DB_USER = "piles";
    public static final String DB_PASSWORD = "12101210";
*/
    public static final String DB_NAME = "jdbc:mysql://cnewsbites-prod.crsa1yeccgr5.eu-west-1.rds.amazonaws.com:3306/cnewsbites_prod?rewriteBatchedStatements=true";
    public static final String DB_USER = "cnewsbites";
    public static final String DB_PASSWORD = "&6&g2DI9i0A%";

}
