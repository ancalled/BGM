package kz.bgm.platform.utils;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LuceneSearchUtil {

    private final IndexSearcher searcher;
    private final StandardAnalyzer analyzer;

    public LuceneSearchUtil(String indexDir) throws IOException {
        FSDirectory index = FSDirectory.open(new File(indexDir));
        IndexReader reader = DirectoryReader.open(index);
        searcher = new IndexSearcher(reader);
        analyzer = new StandardAnalyzer(Version.LUCENE_41);
    }

    public List<Long> search(String artist, String composition) throws IOException, ParseException {

        System.out.println("Got query '" + artist + "' and" + " '" + composition + "'");

        artist = artist.replaceAll("[\\W]", " ");
        composition = composition.replaceAll("[\\W]", " ");

        String[] fields = new String[]{"artist", "name"};
        String[] values = new String[]{artist, composition};
        Query query;
        List<Long> result = new ArrayList<>();
        try {
            query = MultiFieldQueryParser.parse(Version.LUCENE_41, values, fields, analyzer);


            //        BooleanQuery  query = new BooleanQuery();
            //        query.add(new TermQuery(new Term("name", composition)), BooleanClause.Occur.MUST);
            //        query.add(new TermQuery(new Term("artist", artist.toLowerCase())), BooleanClause.Occur.MUST);

            //        query.add(new FuzzyQuery(new Term("name", composition)), BooleanClause.Occur.SHOULD);
            //        query.add(new FuzzyQuery(new Term("artist", artist)), BooleanClause.Occur.SHOULD);


            TopDocs hits = searcher.search(query, 100);


            System.out.println("Hits: " + hits.totalHits);

            for (ScoreDoc hit : hits.scoreDocs) {

                Document d = searcher.doc(hit.doc);
                if (d != null) {
                    IndexableField field = d.getField("id");
                    if (field != null && field.numericValue() != null) {
                        result.add(field.numericValue().longValue());
                        System.out.println(hit.score + "\t" + d.get("artist") + ": " + d.get("name"));
                    }
                }
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
        }


        return result;
    }

    public static void main(String[] args) throws IOException, ParseException {
        if (args.length < 2) {
            System.err.println("Not enough params!");
            return;
        }

        String indexDir = args[0];

        StringBuilder buf = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            String a = args[i];
            buf.append(a);
            if (i < args.length - 1) {
                buf.append(" ");
            }
        }
        String query = buf.toString();
        String[] splitted = query.split(":");
        String artist = splitted[0];
        String track = splitted[1];

        LuceneSearchUtil util = new LuceneSearchUtil(indexDir);
        List<Long> found = util.search(artist, track);

        System.out.println();
        System.out.println("Ids: " + Arrays.toString(found.toArray()));
    }
}
