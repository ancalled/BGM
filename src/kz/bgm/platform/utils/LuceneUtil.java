package kz.bgm.platform.utils;


import kz.bgm.platform.BgmServer;
import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.model.service.LuceneSearch;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LuceneUtil {


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
        luceneSearch.initSearcher(BgmServer.INDEX_DIR);
    }


    public void search(String artist, String track) throws IOException, ParseException {
        List<LuceneSearch.SearchResult> res = luceneSearch.searchWithResult(artist, track, 100, 3.0);

        for (LuceneSearch.SearchResult r: res) {
            System.out.println("[" + r.getScore() + "] id: " + r.getId());
            Track t = catalogStorage.getTrack(r.getId());
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

        String dbHost = props.getProperty(BgmServer.BASE_HOST);
        String dbPort = props.getProperty(BgmServer.BASE_PORT);
        String dbName = props.getProperty(BgmServer.BASE_NAME);
        String dbLogin = props.getProperty(BgmServer.BASE_LOGIN);
        String dbPass = props.getProperty(BgmServer.BASE_PASS);

        System.out.println("Initializing data storage...");
        CatalogFactory.initDBStorage(dbHost, dbPort, dbName, dbLogin, dbPass);
    }




    public static void main(String[] args) throws IOException, ParseException {
        if (args.length < 1) {
            System.out.println("Expected:\nsearch-track.sh artist: composition");
            return;
        }

        StringBuilder buf = new StringBuilder();
        for (String a: args) {
            buf.append(a);
            buf.append(" ");
        }
        String query = buf.toString();
        query = query.substring(0, query.length() - 1);

        String[] splitted = query.split(":[\\s]?");
        String artist = splitted[0];
        String track = splitted[1];

        System.out.println("Query:"
                + "\n\tartist: '" + artist + "'"
                + "\n\ttrack: '" + track + "'"
        );

        LuceneUtil util = new LuceneUtil();
        util.search(artist, track);

//        new LuceneUtil().rebuildIndex();

    }
}
