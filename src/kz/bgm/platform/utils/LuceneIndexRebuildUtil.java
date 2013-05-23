package kz.bgm.platform.utils;


import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.model.service.LuceneSearch;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class LuceneIndexRebuildUtil {

    public static final String APP_DIR = System.getProperty("user.dir");
    public static final String INDEX_DIR = APP_DIR + "/lucen-indexes";

    public static final String BASE_NAME = "base.name";
    public static final String BASE_LOGIN = "base.login";
    public static final String BASE_PASS = "base.pass";
    public static final String BASE_HOST = "base.host";
    public static final String BASE_PORT = "base.port";

    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_ARTIST = "artist";
    public static final String FIELD_COMPOSER = "composer";

    private final CatalogStorage catalogStorage;
    private final LuceneSearch luceneSearch;


    public LuceneIndexRebuildUtil() throws IOException {
        try {
            initDatabase("db.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }

        catalogStorage = CatalogFactory.getStorage();
        luceneSearch = LuceneSearch.getInstance();
    }


    public void rebuildIndex() throws IOException {
        System.out.println("Rebuilding index");

        int trackCount = catalogStorage.getTrackCount();

        File indexDir = new File(INDEX_DIR);

        if (!indexDir.exists()) {
            boolean dirCreated = indexDir.mkdir();

            if (!dirCreated) {
                throw new IOException("Could not create dir " + INDEX_DIR);
            }
        }

        FSDirectory index = FSDirectory.open(indexDir);

        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41, analyzer);
        IndexWriter writer = new IndexWriter(index, config);



        int from = 0;
        int size = 100000;
        while (true) {
            if (from >= trackCount) break;

            List<Track> tracks = catalogStorage.getTracks(from, size);
            System.out.println("Got tracks from " + from + " size " + size);
            for (Track t : tracks) {
                Document doc = new Document();
                doc.add(new LongField(FIELD_ID, t.getId(), Field.Store.YES));
                doc.add(new TextField(FIELD_NAME, t.getName(), Field.Store.YES));
                doc.add(new TextField(FIELD_ARTIST, t.getArtist(), Field.Store.YES));
                doc.add(new TextField(FIELD_COMPOSER, t.getComposer(), Field.Store.YES));
                writer.addDocument(doc);
            }
            from += size;

        }
        writer.close();
        System.out.println("Done");
    }


    public static void initDatabase(String propsFile) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(propsFile));

        String dbHost = props.getProperty(BASE_HOST);
        String dbPort = props.getProperty(BASE_PORT);
        String dbName = props.getProperty(BASE_NAME);
        String dbLogin = props.getProperty(BASE_LOGIN);
        String dbPass = props.getProperty(BASE_PASS);

        System.out.println("Initializing data storage...");
        CatalogFactory.initDBStorage(dbHost, dbPort, dbName, dbLogin, dbPass);
    }


    public static void main(String[] args) throws IOException, ParseException {
        LuceneIndexRebuildUtil util = new LuceneIndexRebuildUtil();
        util.rebuildIndex();

    }
}
