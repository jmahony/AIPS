package es.cnewsbit.queriers;

/**
 * Created by josh on 06/04/14.
 */

import lombok.extern.log4j.Log4j2;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

@Log4j2
public class LuceneQuerier implements Querier {

    /**
     * Used to parse the raw query string
     */
    private final QueryParser QUERY_PARSER;

    /**
     * Reads the lucene index
     */
    private final IndexReader INDEX_READER;

    /**
     * Searches the index
     */
    private final IndexSearcher INDEX_SEARCHER;

    /**
     *
     * Constructor
     *
     * @param indexPath where to save the lucene index
     * @param analyzer analyses the documents
     * @throws IOException if the indexPath directory does not exist
     */
    public LuceneQuerier(String indexPath, Analyzer analyzer) throws IOException {

        Directory indexDirectory = FSDirectory.open(new File(indexPath));

        QUERY_PARSER = new QueryParser(Version.LUCENE_47, "content", analyzer);

        INDEX_READER = DirectoryReader.open(indexDirectory);

        INDEX_SEARCHER = new IndexSearcher(INDEX_READER);

    }

    /**
     *
     * Query a lucene index
     *
     * @param queryString string to search
     * @param perPage how many results to return
     * @throws ParseException
     * @throws IOException
     */
    public void query(String queryString, int perPage) throws ParseException, IOException {

        Query query = QUERY_PARSER.parse(queryString);

        TopScoreDocCollector collector = TopScoreDocCollector.create(perPage, true);

        INDEX_SEARCHER.search(query, collector);

        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        log.info("Found " + hits.length + " hits for query " + queryString);

        for (int i = 0; i < hits.length; i++) {

            int docId = hits[i].doc;

            Document d = INDEX_SEARCHER.doc(docId);

            log.info((i+1) + ". " + d.getField("handle"));

        }

    }

    /**
     *
     * Close the connection to the lucene index reader
     *
     * @throws IOException
     */
    public void close() throws IOException {

        INDEX_READER.close();

    }

}
