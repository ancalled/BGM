package kz.bgm.platform.search;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class IdSearcher {
    private static final Logger log = Logger.getLogger(IdSearcher.class);

    private static String APP_DIR = System.getProperty("user.dir");
    private static String INDEX_DIR = APP_DIR + "/lucen-indexes";
    public static final int RESULT_SIZE = 100000;
    private FSDirectory index;
    private IndexReader reader;
    private IndexSearcher searcher;

    public void init(Connection connection) throws IOException {
        long start = System.currentTimeMillis();
        indexDoc(connection);
        long stop = System.currentTimeMillis();
        float end = stop - start;
        log.info("Indexing finished in " + end / 1000);
    }

    private void indexDoc(Connection connection) throws IOException {
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);
        File indexDir = new File(INDEX_DIR);

        if (!indexDir.exists()) {
            boolean dirCreated = indexDir.mkdir();

            if (!dirCreated) {
                throw new IOException("Could not create dir " + INDEX_DIR);
            }
        }

        index = FSDirectory.open(indexDir);

        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41, analyzer);
        IndexWriter w = new IndexWriter(index, config);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name,artist, composer FROM composition");

            while (rs.next()) {
                Document doc = new Document();
                String bId = Integer.toString(rs.getInt("id"));
                doc.add(new TextField("id", bId, Field.Store.YES));
                doc.add(new TextField("name", rs.getString("name"), Field.Store.YES));
                doc.add(new TextField("artist", rs.getString("artist"), Field.Store.YES));
                doc.add(new TextField("composer", rs.getString("composer"), Field.Store.YES));
                w.addDocument(doc);
            }
            w.close();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<String> search(String value)
            throws IOException, ParseException {
        long startF = System.currentTimeMillis();
        List<String> idList = searchTracks(value);
        long stopF = System.currentTimeMillis();
        float endF = stopF - startF;
        log.info("Search finished in " + endF / 1000);
        return idList;

    }

    public List<String> search(String artist, String composition)
            throws IOException, ParseException {
        long startF = System.currentTimeMillis();
        List<String> idList = searchBySongAndArtist(artist, composition);
        long stopF = System.currentTimeMillis();
        float endF = stopF - startF;
        log.info("Search finished in " + endF / 1000);
        return idList;

    }

    private List<String> searchBySongAndArtist
            (String artist, String composition) throws IOException, ParseException {
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);
        log.info("Got query '" +
                artist +
                "' and" + " '" +
                composition + "'");

        index = FSDirectory.open(new File(INDEX_DIR));
        reader = DirectoryReader.open(index);
        searcher = new IndexSearcher(reader);

        String[] fields = new String[]{"artist", "name"};
        String[] values = new String[]{artist, composition};

        Query query =
                MultiFieldQueryParser.parse(Version.LUCENE_41, values, fields, analyzer);

        TopScoreDocCollector collector = TopScoreDocCollector.create(RESULT_SIZE, true);
        searcher.search(query, collector);

        int totalHits = collector.getTotalHits();
        TopDocs topDocs = collector.topDocs();

        float maxScore = topDocs.getMaxScore();

        ScoreDoc[] hits = topDocs.scoreDocs;

        List<String> idList = new ArrayList<String>();

        log.info("Found " + totalHits + " tracks id.");

        for (int k = 0; k < totalHits; k++) {
            ScoreDoc hit = hits[k];

            if (hit.score < 7) {
                continue;
            }

            int docId = hit.doc;
            Document d = searcher.doc(docId);
            String baseId = d.get("id");
            idList.add(baseId);
        }
        reader.close();
        return idList;
    }

    private List<String> searchTracks(String value) throws IOException, ParseException {
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);
        log.info("Got query '" + value + "'");
        FSDirectory index = FSDirectory.open(new File(INDEX_DIR));
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);

        String[] fields = new String[]{"artist", "name", "composer"};
        MultiFieldQueryParser queryParser =
                new MultiFieldQueryParser(Version.LUCENE_41, fields, analyzer);

        Query query = queryParser.parse(value);
        TopScoreDocCollector collector = TopScoreDocCollector.create(RESULT_SIZE, true);
        searcher.search(query, collector);

        int totalHits = collector.getTotalHits();
        TopDocs topDocs = collector.topDocs();

        ScoreDoc[] hits = topDocs.scoreDocs;

        List<String> idList = new ArrayList<String>();

        log.info("Found " + totalHits + " tracks id.");

        for (int k = 0; k < totalHits; k++) {
            ScoreDoc hit = hits[k];
            int docId = hit.doc;
            Document d = searcher.doc(docId);
            String baseId = d.get("id");
            idList.add(baseId);
        }
        reader.close();
        return idList;
    }


}

