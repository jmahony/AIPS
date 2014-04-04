package es.cnewsbit.indexers;

import es.cnewsbit.Indexable;
import es.cnewsbit.NewsArticle;
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
public class LuceneIndexer implements Indexer {

    /**
     * Directory the Lucene index will be written to
     */
    public static final String INDEX_PATH = "/home/josh/lucene/";

    public static void createIndex(List<Indexable> indexables) {

        // Create an analyser, this is used to process the articles content
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);

        // Configuration for the index writer
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);

        // Overwrite any existing indices
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        try {

            // Open the directory to write the index to
            Directory dir = FSDirectory.open(new File(INDEX_PATH));

            IndexWriter indexWriter = new IndexWriter(dir, iwc);

            // Go through each article adding it to the
            for (Indexable indexable : indexables) {

                Document doc = new Document();

                doc.add(new TextField("content", indexable.getIndexString(), Field.Store.NO));
                doc.add(new StringField("handle", indexable.getHandle(), Field.Store.YES));

                if (indexWriter.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {

                    indexWriter.addDocument(doc);

                } else {

                    indexWriter.updateDocument(new Term("path", indexable.getHandle()), doc);

                }

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

}
