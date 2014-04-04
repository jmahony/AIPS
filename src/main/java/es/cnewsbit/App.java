package es.cnewsbit;

import java.io.*;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * Hello world!
 *
 */
public class App {

    private static final String PATH_TO_INDEX = "/home/josh/Desktop/lucene/index/";
    private static final String PATH_TO_FILES = "/home/josh/Desktop/lucene/files/";


    public static void main(String[] args) {

        /*try {

            createIndex();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        NotSuire.test();

        //new App();

    }

    public static void createIndex() throws IOException, ParseException {

        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);

        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer);

        Directory dir = FSDirectory.open(new File(PATH_TO_INDEX));
        File docDir = new File(PATH_TO_FILES);
        IndexWriter indexWriter = new IndexWriter(dir, iwc);

        boolean create = true;

        if (!docDir.exists() || !docDir.canRead()) {
            System.out.println("Document directory '" +docDir.getAbsolutePath()+ "' does not exist or is not readable, please check the path");
            System.exit(1);
        }

        if (create) {
            // Create a new index in the directory, removing any
            // previously indexed documents:
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        } else {
            // Add new documents to an existing index:
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        }


        indexDocs(indexWriter, docDir);

        indexWriter.close();

        String query = "steak";

        Query q = new QueryParser(Version.LUCENE_47, "contents", analyzer).parse(query);

        int hitsPerPage = 100;

        // TODO: Whats this do?
        IndexReader reader = DirectoryReader.open(dir);

        // TODO: Whats this do?
        IndexSearcher searcher = new IndexSearcher(reader);

        // TODO: Whats this do?
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);

        searcher.search(q, collector);

        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        System.out.println("Found " + hits.length + " hits.");

        for (int i = 0; i < hits.length; i++) {

            int docId = hits[i].doc;

            Document d = searcher.doc(docId);

            System.out.println((i+1) + ". " + d.getField("path"));

        }

        reader.close();

    }

    private static void indexDocs(IndexWriter indexWriter, File file) {

        if (file.canRead()) {

            if (file.isDirectory()) {

                String[] files = file.list();

                if (files != null) {

                    for (String f : files) {

                        indexDocs(indexWriter, new File(App.PATH_TO_FILES + f));

                    }

                }

            } else {

                FileInputStream fis;

                try {

                    fis = new FileInputStream(file);

                    Document doc = new Document();

                    doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(fis, "UTF-8"))));

                    doc.add(new StringField("path", file.getPath(), Field.Store.YES));

                    if (indexWriter.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {

                        // New Index

                        System.out.println("Adding file " + file);

                        indexWriter.addDocument(doc);

                    } else {

                        // Update file

                        System.out.println("Updating file " + file);

                        indexWriter.updateDocument(new Term("path", file.getPath()), doc);

                    }

                } catch (FileNotFoundException e) {

                    e.printStackTrace();

                    return;

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();

                    return;

                } catch (IOException e) {
                    e.printStackTrace();

                    return;

                }

            }

        }

    }

    public App() {





        /**
        try {

            String html = ResourceLoader.asString(this, "/external/documents/uk-scotland-independence-devolution-idUKBREA1H1JM20140218");
            String relevanceCSV = ResourceLoader.asString(this, "/local/relevance/uk-scotland-independence-devolution-idUKBREA1H1JM20140218.csv");

            HTMLDocument doc = new HTMLDocument(html, new double[] {0.25, 0.5, 0.25});

            Set<Integer> relevancySet = CSV.toRelevancySet(relevanceCSV, 0, 1);

            RelevancyGenerator rg = new RelevancyGenerator(relevancySet, doc);

            rg.generate(1.00d, 50.00d);

            rg.printResults();

        } catch (Exception e) {

            e.printStackTrace();

        }
*/


    }


}
