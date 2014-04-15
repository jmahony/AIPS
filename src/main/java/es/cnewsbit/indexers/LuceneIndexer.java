package es.cnewsbit.indexers;

import es.cnewsbit.Indexable;
import es.cnewsbit.NewsArticle;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.*;
import java.util.List;

/**
 * Created by josh on 03/04/14.
 */
@Log4j2
public class LuceneIndexer implements Indexer {

    /**
     * Index Writer, this actually creates the index
     */
    private @Getter final IndexWriter INDEX_WRITER;

    /**
     *
     * Constructor
     *
     * @param indexPath where to save the lucene index
     * @param analyzer analyses the documents
     * @throws IOException if the indexPath directory does not exist
     */
    public LuceneIndexer(String indexPath, Analyzer analyzer) throws IOException {

        IndexWriterConfig indexWriterConfig =
                new IndexWriterConfig(Version.LUCENE_47, analyzer);

        // Overwrite any existing indices
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        Directory indexDirectory = FSDirectory.open(new File(indexPath));

        INDEX_WRITER = new IndexWriter(indexDirectory, indexWriterConfig);

    }

    /**
     *
     * Adds a document to the index
     *
     * @param indexable the document to add
     * @throws IOException if the index directory does not exit
     */
    @Override
    public synchronized void addToIndex(Indexable indexable) throws IOException {

        Document doc = new Document();

        doc.add(new TextField("content", indexable.getIndexString(), Field.Store.NO));
        doc.add(new StringField("handle", indexable.getHandle(), Field.Store.YES));

        if (INDEX_WRITER.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {

            log.debug("Adding " + indexable.getHandle() + " to index");

            INDEX_WRITER.addDocument(doc);

        } else {

            log.debug("Updating " + indexable.getHandle() + " to index");

            INDEX_WRITER.updateDocument(new Term("handle", indexable.getHandle()), doc);

        }

    }

    /**
     *
     * Closes the index writer
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {

        log.info("Indexer closing");

        INDEX_WRITER.close();

    }
}
