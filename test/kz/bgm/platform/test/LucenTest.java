package kz.bgm.platform.test;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import kz.bgm.platform.items.Track;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LucenTest {

    private static String lBase = "bgm";
    private static String lLogin = "root";
    private static String lPass = "root";
    private static String lHost = "localhost";
    private static String lPort = "3306";

    private static String appDir = System.getProperty("user.dir");
    private static String lucenDir = appDir+"/lucenDoc";

    private final ComboPooledDataSource pool;

    public LucenTest(String host, String port,
                     String base, String user, String pass) {
        pool = initPool(host, port, base, user, pass);
    }

    public static void main(String[] args) throws IOException, ParseException {

        LucenTest lucenTest = new LucenTest(lHost, lPort, lBase, lLogin, lPass);

        //Индексирование
        lucenTest.indexDoc();

        //Поиск
        List<Track> tr = lucenTest.searchTracks("Jones");

        for (Track t : tr) {
            System.out.println(t);
        }

    }

    public List<Track> searchTracks(String value) throws IOException, ParseException {
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);

        FSDirectory index = FSDirectory.open(new File(lucenDir));

        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);


        Query query = new QueryParser(Version.LUCENE_41, "artist", analyzer).parse(value);
        ScoreDoc[] hits = searcher.search(query, 1).scoreDocs;

        List<Track> trackList = new ArrayList<Track>();

        System.out.println("Found " + hits.length + " hits.");

        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            String baseId = d.get("id");
            Track track = searchById(baseId);
            trackList.add(track);
        }
        reader.close();
        return trackList;
    }

    public Track searchById(String id) {
        Connection connection = null;
        try {
            connection = pool.getConnection();

            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM composition WHERE id=?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            Track track = null;
            while (rs.next()) {

                track = parseTrack(rs);
            }

            return track;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void indexDoc() throws IOException {


        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);
        FSDirectory index = FSDirectory.open(new File(lucenDir));
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41, analyzer);
        IndexWriter w = new IndexWriter(index, config);
        w.close();

        Connection connection = null;

        try {
            connection = pool.getConnection();
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


    private static ComboPooledDataSource initPool(String host,
                                                  String port,
                                                  String base,
                                                  String user,
                                                  String pass) {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + base;

        ComboPooledDataSource pool = new ComboPooledDataSource();
        try {
            pool.setDriverClass("com.mysql.jdbc.Driver");
            pool.setJdbcUrl(url);
            pool.setUser(user);
            pool.setPassword(pass);
            pool.setMinPoolSize(10);
            pool.setMaxPoolSize(20);
            pool.setMaxStatements(20);
            pool.setMaxStatementsPerConnection(5);

        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return pool;
    }

    private static Track parseTrack(ResultSet rs) throws SQLException {
        Track track = new Track();
        track.setId(rs.getLong("id"));
        track.setCode(rs.getString("code"));
        track.setName(rs.getString("name"));
        track.setArtist(rs.getString("artist"));
        track.setComposer(rs.getString("composer"));
        track.setMobileShare(rs.getFloat("shareMobile"));
        track.setPublicShare(rs.getFloat("sharePublic"));
        return track;
    }


}
