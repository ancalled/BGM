package kz.bgm.platform.model.service;

import kz.bgm.platform.model.domain.Track;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LuceneSearch {

    private static final Logger log = Logger.getLogger(LuceneSearch.class);


    public static final int RESULT_SIZE = 100000;

    public static final double THRESHOLD = 6.5;
    public static final int LIMIT = 100;

    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_ARTIST = "artist";
    public static final String FIELD_COMPOSER = "composer";

    private StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);
    private IndexSearcher searcher;


    private LuceneSearch() {
    }

    public void initSearcher(String indexDir) throws IOException {
        FSDirectory index = FSDirectory.open(new File(indexDir));
        IndexReader reader = DirectoryReader.open(index);
        searcher = new IndexSearcher(reader);
    }


    public void index(List<Track> tracks, String indexDirPath) throws IOException {
        File indexDir = new File(indexDirPath);

        if (!indexDir.exists()) {
            boolean dirCreated = indexDir.mkdir();

            if (!dirCreated) {
                throw new IOException("Could not create dir " + indexDirPath);
            }
        }

        FSDirectory index = FSDirectory.open(indexDir);

        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41, analyzer);
        IndexWriter w = new IndexWriter(index, config);

        for (Track t : tracks) {
            Document doc = new Document();
            doc.add(new LongField(FIELD_ID, t.getId(), Field.Store.YES));
            doc.add(new TextField(FIELD_NAME, t.getName(), Field.Store.YES));
            doc.add(new TextField(FIELD_ARTIST, t.getArtist(), Field.Store.YES));
            doc.add(new TextField(FIELD_COMPOSER, t.getComposer(), Field.Store.YES));
            w.addDocument(doc);

        }

        w.close();
    }


    public List<Long> search(String artist, String composition)
            throws IOException, ParseException {
        return search(artist, composition, LIMIT, THRESHOLD);
    }

    public List<Long> search(String artist, String composition, int limit, double threshold)
            throws IOException, ParseException {

        log.info("Got query '" + artist + "' and" + " '" + composition + "'");

        String[] fields = new String[]{FIELD_ARTIST, FIELD_NAME};
        String[] values = new String[]{artist, composition};
        Query query = MultiFieldQueryParser.parse(Version.LUCENE_41, values, fields, analyzer);


        TopDocs hits = searcher.search(query, limit);

        List<Long> result = new ArrayList<>();

        System.out.println("Hits: " + hits.totalHits);

        for (ScoreDoc hit : hits.scoreDocs) {
            if (hit.score < threshold) {
                continue;
            }

            Document d = searcher.doc(hit.doc);
            if (d.getField(FIELD_ID).numericValue() != null) {
                result.add(d.getField(FIELD_ID).numericValue().longValue());
            }

//            System.out.println(hit.score + "\t" + d.get("artist") + ": " + d.get("name"));
        }

        return result;
    }


    public List<Long> search(String queryString) throws IOException, ParseException {
        return search(queryString, 0, 100);
    }


    public List<Long> search(String queryString, int start, int length) throws IOException, ParseException {
        log.info("Got query '" + queryString + "'");

        String[] fields = new String[]{FIELD_ARTIST, FIELD_NAME, FIELD_COMPOSER};
        QueryParser queryParser =
                new MultiFieldQueryParser(Version.LUCENE_41, fields, analyzer);

        Query query = queryParser.parse(queryString);
        TopScoreDocCollector collector = TopScoreDocCollector.create(start + length, true);
        searcher.search(query, collector);

        int totalHits = collector.getTotalHits();
        TopDocs topDocs = collector.topDocs();

        ScoreDoc[] hits = topDocs.scoreDocs;

        List<Long> result = new ArrayList<>(length);

        log.info("Found " + totalHits + " tracks id.");

        if (start > totalHits)
            return Collections.emptyList();

        int end = Math.min(start + length, totalHits);
        for (int k = start; k < end; k++) {
            ScoreDoc hit = hits[k];
            Document d = searcher.doc(hit.doc);
            result.add(Long.parseLong(d.get(FIELD_ID)));

            System.out.println(hit.score + "\t" + d.get(FIELD_ARTIST) + ": " + d.get(FIELD_NAME));

        }

        return result;
    }


    public List<SearchResult> searchWithResult(String artist, String composition, int limit, double threshold)
            throws IOException, ParseException {

//        log.info("Got query '" + artist + "' and" + " '" + composition + "'");

//        String[] fields = new String[]{FIELD_ARTIST, FIELD_NAME};
//        String[] values = new String[]{artist, composition};
//        Query query = MultiFieldQueryParser.parse(Version.LUCENE_41, values, fields, analyzer);

        BooleanQuery query = new BooleanQuery();
        query.add(createTermQuery(FIELD_ARTIST, artist, 1.5f), BooleanClause.Occur.MUST);
        if (!composition.isEmpty()) {
            query.add(createTermQuery(FIELD_NAME, composition, 1), BooleanClause.Occur.MUST);
        }

        TopDocs hits = searcher.search(query, limit);

        List<SearchResult> result = new ArrayList<>();

//        System.out.println("Hits: " + hits.totalHits);

        for (ScoreDoc hit : hits.scoreDocs) {
            if (hit.score < threshold) {
                continue;
            }

            Document d = searcher.doc(hit.doc);
            long id = Long.parseLong(d.get(FIELD_ID));
            result.add(new SearchResult(id, hit.score));

//            System.out.println(hit.score + "\t" + d.get("artist") + ": " + d.get("name"));
        }

        return result;
    }

    private Query createTermQuery(String field, String value, float boost) throws ParseException {
        Query parse = new QueryParser(Version.LUCENE_41, field, analyzer).parse(value);
        parse.setBoost(boost);
        return parse;
    }


    private static final LuceneSearch INSTANCE = new LuceneSearch();

    public static LuceneSearch getInstance() {
        return INSTANCE;
    }

    public static class SearchResult {
        private final long id;
        private final float score;

        public SearchResult(long id, float score) {
            this.id = id;
            this.score = score;
        }

        public long getId() {
            return id;
        }

        public float getScore() {
            return score;
        }
    }

}

