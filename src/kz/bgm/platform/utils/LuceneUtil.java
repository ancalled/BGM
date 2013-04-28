package kz.bgm.platform.utils;


import kz.bgm.platform.BgmServer;
import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.model.service.LuceneSearch;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class LuceneUtil {


    private final CatalogStorage catalogStorage;
    private final LuceneSearch luceneSearch;


    public LuceneUtil() {
        try {
            initDatabase("db.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }

        catalogStorage = CatalogFactory.getStorage();
        luceneSearch = LuceneSearch.getInstance();

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


    public void rebuildIndex() throws IOException {
        List<Track> tracks = catalogStorage.getAllTracks();
        luceneSearch.index(tracks, BgmServer.INDEX_DIR);

    }


    public static void main(String[] args) throws IOException {
        new LuceneUtil().rebuildIndex();
    }
}
