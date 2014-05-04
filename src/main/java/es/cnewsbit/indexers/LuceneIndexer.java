package es.cnewsbit.indexers;

import es.cnewsbit.Indexable;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.lucene.analysis.Analyzer;
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

/**
 * Facade to hide some of the complexities of creating and adding to a Apache
 * Lucene index.
 */
@Log4j2
public class LuceneIndexer implements Indexer {

    private @Getter final IndexWriter INDEX_WRITER;

    /**
     *
     * Constructor
     *
     * @param indexPath where to save the lucene index
     * @param analyzer analyses the documents, tokenising, stop words, stemming
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

    @Override
    public synchronized void addToIndex(Indexable indexable) throws IOException {

        Document doc = new Document();

        // The content that the document will be indexed by
        doc.add(new TextField("content", indexable.getIndexString(), Field.Store.NO));

        // We only store a handle to reference the document by, this handle can
        // then be used as the key to store the actual document by
        doc.add(new StringField("handle", indexable.getHandle(), Field.Store.YES));

        if (INDEX_WRITER.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {

            log.debug("Adding " + indexable.getHandle() + " to index");

            INDEX_WRITER.addDocument(doc);

        } else {

            log.debug("Updating " + indexable.getHandle() + " to index");

            INDEX_WRITER.updateDocument(new Term("handle", indexable.getHandle()), doc);

        }

    }

    @Override
    public void close() throws IOException {

        log.info("Indexer closing");

        INDEX_WRITER.close();

    }

}
