package kz.bgm.platform.model.service;

import kz.bgm.platform.model.domain.Track;
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
import java.util.ArrayList;
import java.util.List;

public class LuceneSearch {

    private static final Logger log = Logger.getLogger(LuceneSearch.class);

    private static String APP_DIR = System.getProperty("user.dir");
    private static String INDEX_DIR = APP_DIR + "/lucen-indexes";
    public static final int RESULT_SIZE = 100000;

    private FSDirectory index;
    private StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);


    private LuceneSearch() {
    }


    public void index(List<Track> tracks) throws IOException {
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

        for (Track t : tracks) {
            Document doc = new Document();
            doc.add(new TextField("id", t.getId() + "", Field.Store.YES));
            doc.add(new TextField("name", t.getName(), Field.Store.YES));
            doc.add(new TextField("artist", t.getArtist(), Field.Store.YES));
            doc.add(new TextField("composer", t.getComposer(), Field.Store.YES));
            w.addDocument(doc);

        }

        w.close();
    }


    public List<Long> search(String artist, String composition) throws IOException, ParseException {

        log.info("Got query '" +
                artist +
                "' and" + " '" +
                composition + "'");

        index = FSDirectory.open(new File(INDEX_DIR));
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);

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

        List<Long> result = new ArrayList<Long>();

        log.info("Found " + totalHits + " tracks id.");

        for (ScoreDoc hit : hits) {
            if (hit.score < 7) {
                continue;
            }

            int docId = hit.doc;
            Document d = searcher.doc(docId);
            result.add(Long.parseLong(d.get("id")));
        }
        reader.close();

        return result;
    }


    public List<Long> search(String value) throws IOException, ParseException {
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

        List<Long> result = new ArrayList<Long>();

        log.info("Found " + totalHits + " tracks id.");

        for (int k = 0; k < totalHits; k++) {
            ScoreDoc hit = hits[k];
            int docId = hit.doc;
            Document d = searcher.doc(docId);
            result.add(Long.parseLong(d.get("id")));
        }
        reader.close();
        return result;
    }


    private static final LuceneSearch INSTANCE = new LuceneSearch();

    public static LuceneSearch getInstance() {
        return INSTANCE;
    }


}

