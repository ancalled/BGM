package kz.bgm.platform.utils;


import kz.bgm.platform.model.domain.SearchResult;
import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.model.service.LuceneSearch;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class LuceneUtil {

    public static final String APP_DIR = System.getProperty("user.dir");
    public static final String INDEX_DIR = APP_DIR + "/lucen-indexes";

    public static final String BASE_NAME = "base.name";
    public static final String BASE_LOGIN = "base.login";
    public static final String BASE_PASS = "base.pass";
    public static final String BASE_HOST = "base.host";
    public static final String BASE_PORT = "base.port";


    private final CatalogStorage catalogStorage;
    private final LuceneSearch luceneSearch;


    public LuceneUtil() throws IOException {
        try {
            initDatabase("db.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }

        catalogStorage = CatalogFactory.getStorage();
        luceneSearch = LuceneSearch.getInstance();
        luceneSearch.initSearcher(INDEX_DIR);
    }


    public void search(String artist, String authors, String track) throws IOException, ParseException {
        List<SearchResult> res = luceneSearch.search(artist, authors, track, 100);

        for (SearchResult r : res) {
            System.out.println("[" + r.getScore() + "] id: " + r.getTrackId());
            Track t = catalogStorage.getTrack(r.getTrackId());
            if (t != null) {
                System.out.println("\tartist: '" + t.getArtist() + "'" +
                        "\n\ttrack: '" + t.getName() + "'" +
                        "\n\tcomposer: '" + t.getComposer() + "'" +
                        "\n\tcatalog: '" + t.getCatalog() + "'" +
                        "\n\tcode: '" + t.getCode() + "'" +
                        "\n\tmobileShare: '" + t.getMobileShare() + "'"
                );
                System.out.println();
            }
        }
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

        if (args.length < 1) {
            System.out.println("Expected:\nsearch-track.sh artist: composition");
            return;
        }

        StringBuilder buf = new StringBuilder();
        for (String a : args) {
            buf.append(a);
            buf.append(" ");
        }
        String query = buf.toString();
        query = query.substring(0, query.length() - 1);

        String[] splitted = query.split(":[\\s]?");
        String artist = splitted[0];
        String authors = splitted.length > 1 ? splitted[1] : null;
        String track = splitted.length > 2 ? splitted[2] : null;

        System.out.println("Queried artist: '" + artist + "'" + ", track: '" + track + "'");

        LuceneUtil util = new LuceneUtil();
        util.search(artist, authors, track);

//        new LuceneUtil().rebuildIndex();

    }
}
