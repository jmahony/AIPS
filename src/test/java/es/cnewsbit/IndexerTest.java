package es.cnewsbit;

import es.cnewsbit.indexers.LuceneIndexer;
import es.cnewsbit.queriers.LuceneQuerier;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.util.Version;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for the indexer class
 */
public class IndexerTest {

    /**
     * Path of the index file
     */
    private String indexPath;

    /**
     * The Analyser to use for querying and indexing
     */
    private Analyzer analyzer;

    /**
     * The standard analyser I'm using often stems number from the end of strings
     * so I need some number words
     */
    private final String[] numbers = new String[] {"one", "two", "three", "four"};


    @Before
    public void setup() throws URISyntaxException {

        indexPath = null + System.getProperty("java.io.tmpdir") + "lucene_test/" + System.currentTimeMillis();

        analyzer = new StandardAnalyzer(Version.LUCENE_47);

    }
    @Test
    public void testAddToIndexLucene() throws IOException, URISyntaxException, ParseException {

        LuceneIndexer indexer = new LuceneIndexer(indexPath, analyzer);

        // Create two mocks and add them to index
        Indexable item = mock(Indexable.class);

        when(item.getHandle()).thenReturn("the_handle");
        when(item.getIndexString()).thenReturn("term termOne termTwo");
        indexer.addToIndex(item);

        when(item.getHandle()).thenReturn("the_handleTwo");
        when(item.getIndexString()).thenReturn("term termThree termFour");
        indexer.addToIndex(item);

        indexer.close();

        // Make sure the items are in the index
        LuceneQuerier luceneQuerier = new LuceneQuerier(indexPath, analyzer);

        // termOne is only in the_handle
        String[] hits = luceneQuerier.query("termOne", 1);
        Assert.assertEquals(hits[0], "the_handle");

        luceneQuerier = new LuceneQuerier(indexPath, analyzer);

        // termFour is only in the_handleTwo
        hits = luceneQuerier.query("termFour", 1);
        Assert.assertEquals(hits[0], "the_handleTwo");

        luceneQuerier = new LuceneQuerier(indexPath, analyzer);

        // term is in both
        hits = luceneQuerier.query("term", 10);
        Assert.assertTrue(Arrays.asList(hits).contains("the_handle"));
        Assert.assertTrue(Arrays.asList(hits).contains("the_handleTwo"));

    }


    @Test
    public void testAddMultipleToIndexLucene() throws IOException, URISyntaxException, ParseException {

        LuceneIndexer indexer = new LuceneIndexer(indexPath, analyzer);

        Indexable item = mock(Indexable.class);

        // Pretty much the same as the other test, but trying out
        // Mockito answers
        when(item.getHandle()).thenAnswer(new Answer<String>() {

            private int count = 0;

            @Override
            public String answer(InvocationOnMock invocationOnMock) throws Throwable {
                return "handle" + numbers[count++];
            }

        });

        when(item.getIndexString()).thenAnswer(new Answer<String>() {

            private int count = 0;

            @Override
            public String answer(InvocationOnMock invocationOnMock) throws Throwable {
                return "term" + numbers[count++];
            }

        });

        indexer.addToIndex(item);
        indexer.addToIndex(item);

        indexer.close();

        LuceneQuerier luceneQuerier = new LuceneQuerier(indexPath, analyzer);

        String[] hits = luceneQuerier.query("termone", 1);
        Assert.assertEquals(hits[0], "handleone");

        luceneQuerier = new LuceneQuerier(indexPath, analyzer);

        // There are two log call to the handle when adding to the index and only
        // one to index string, so the numbering gets a bit out of sync
        hits = luceneQuerier.query("termtwo", 1);
        Assert.assertEquals(hits[0], "handlethree");

    }

}
