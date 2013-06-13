package kz.bgm.platform.model.service;

import kz.bgm.platform.model.domain.SearchResult;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.lucene.search.BooleanClause.Occur;

public class LuceneSearch {

    private static final Logger log = Logger.getLogger(LuceneSearch.class);

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


    public List<SearchResult> search(String queryString, int limit) throws IOException, ParseException {
        log.info("Got query '" + queryString + "'");

        String[] fields = new String[]{FIELD_ARTIST, FIELD_NAME, FIELD_COMPOSER};
        QueryParser queryParser =
                new MultiFieldQueryParser(Version.LUCENE_41, fields, analyzer);

        Query query = queryParser.parse(queryString);
        TopScoreDocCollector collector = TopScoreDocCollector.create(limit, true);
        searcher.search(query, collector);

        int totalHits = collector.getTotalHits();
        TopDocs topDocs = collector.topDocs();

        ScoreDoc[] hits = topDocs.scoreDocs;

        List<SearchResult> result = new ArrayList<>();

        log.info("Found " + totalHits + " tracks id.");

        for (int k = 0; k < limit; k++) {
            ScoreDoc hit = hits[k];
            Document d = searcher.doc(hit.doc);
            long id = Long.parseLong(d.get(FIELD_ID));
            result.add(new SearchResult(id, hit.score));
        }

        return result;
    }


    public List<SearchResult> search(String artist, String authors, String composition, int limit)
            throws IOException, ParseException {

        BooleanQuery query = new BooleanQuery();
        if (composition != null && !composition.isEmpty()) {
            query.add(createTermQuery(FIELD_NAME, composition, 1), Occur.MUST);
        }

        if (authors != null && artist != null) {
            BooleanQuery sq = new BooleanQuery();
            sq.add(createTermQuery(FIELD_COMPOSER, authors, 1), Occur.SHOULD);
            sq.add(createTermQuery(FIELD_ARTIST, artist, 1), Occur.SHOULD);
            query.add(sq, Occur.MUST);

        } else if (artist != null) {
            BooleanQuery sq = new BooleanQuery();
            sq.add(createTermQuery(FIELD_ARTIST, artist, 1), Occur.SHOULD);
            sq.add(createTermQuery(FIELD_COMPOSER, artist, 1), Occur.SHOULD);
            query.add(sq, Occur.MUST);

        } else if (authors != null) {
            query.add(createTermQuery(FIELD_COMPOSER, authors, 1), Occur.MUST);
        }


        TopDocs hits = searcher.search(query, limit);

        List<SearchResult> result = new ArrayList<>();

        for (ScoreDoc hit : hits.scoreDocs) {
            Document d = searcher.doc(hit.doc);
            long id = Long.parseLong(d.get(FIELD_ID));
            result.add(new SearchResult(id, hit.score));
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

}

