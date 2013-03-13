package kz.bgm;

import kz.bgm.base.Executor;
import kz.bgm.base.QueryHolder;
import kz.bgm.items.ReportItem;
import kz.bgm.items.Track;
import kz.bgm.parsers.CatalogParser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class CatalogStorage {

    public static final boolean DEBUG = false;
    //    public static final boolean DEBUG = true;


    public static final DataFormatter FORMATTER = new DataFormatter(true);
    public static String homeDir = System.getProperty("user.dir");

    private Map<String, List<Track>> authItemsMap;
    private Map<String, List<Track>> commonItemsMap;
    public static final String CAT_HOME = homeDir + "/catalogs/";

    private Executor baseExecutor;

    public CatalogStorage(String propsFile) {
        authItemsMap = new HashMap<String, List<Track>>();
        commonItemsMap = new HashMap<String, List<Track>>();

        Properties props = new Properties();

        try {
            props.load(new FileInputStream(propsFile));

        } catch (IOException e) {
            e.printStackTrace();
        }

        baseExecutor = Executor.getInstance(props);

    }


    public void addItem(Track track, boolean common) {
        String artist = track.getArtist();
        if (artist == null || "".equals(artist)) {
            return;
        }

        artist = artist.toLowerCase();

        Map<String, List<Track>> map = common ? commonItemsMap : authItemsMap;

        List<Track> items = map.get(artist);
        if (items == null) {
            items = new ArrayList<Track>();
            map.put(artist, items);
        }

        items.add(track);
    }

    public Track search(String author, String song) {
        return this.search(author, song, false);
    }

    public Track search(String author, String song, boolean common) {
        if (DEBUG) {
            System.out.println("Searching for: '" + author + "': '" + song + "'");
        }

        Map<String, List<Track>> map = common ? commonItemsMap : authItemsMap;
        List<Track> comps = map.get(author.toLowerCase());

        if (comps != null) {
            if (DEBUG) {
                StringBuilder buf = new StringBuilder();
                for (int i = 0; i < comps.size(); i++) {
                    Track comp = comps.get(i);
                    buf.append(comp.getComposition());
                    if (i < comps.size() - 1) {
                        buf.append(", ");
                    }
                }
                System.out.println("Found author '" + author + "': [" + buf + "]");
            }

            for (Track comp : comps) {
                if (comp.getComposition().equalsIgnoreCase(song)) {
                    return comp;
                }
            }
        }

        return null;
    }

    //todo с базы грузить виды каталогов. там нужно табличку ...
    private static final String PUB_ALL_MUSIC = "All_Music";
    private static final String PUB_MCS = "All_Music/MCS";
    private static final String PUB_MSG = "All_Music/MSG";

    private void loadCatalogToBd(String catalogFile, String publisher) {
        try {
            CatalogParser parser = new CatalogParser();

            List<Track> trackList = null;
            if (PUB_MSG.equals(publisher) || PUB_MCS.equals(publisher)) {
                long start = System.currentTimeMillis();

                trackList = parser.loadAllMCSandMGS(CAT_HOME + catalogFile);

                long stop = System.currentTimeMillis();
                System.out.println("Catalog file " + catalogFile + " loaded on: " + (stop - start) / 1000 + " sec");

            } else if (PUB_ALL_MUSIC.equals(publisher)) {
                long start = System.currentTimeMillis();

                trackList = parser.loadAllMusic(CAT_HOME + catalogFile);

                long stop = System.currentTimeMillis();
                System.out.println("Catalog file " + catalogFile + " loaded on: " + (stop - start) / 1000 + " sec");
            }

            if (trackList != null) {
                long start = System.currentTimeMillis();

                baseExecutor.storeInBaseAllMusic(QueryHolder.TABLE_ALL_MUSIC, trackList);

                long stop = System.currentTimeMillis();
                System.out.println("Catalog stored in base on: " + (stop - start) / 1000 + " sec");
                trackList.clear();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //todo завязывать с хардкором каталогов , сделать нормальные рабочие методы
    private void loadToBaseAllMusicCats() {
        try {

            CatalogParser parser = new CatalogParser();

            List<Track> mcs = parser.loadAllMCSandMGS(CAT_HOME + "MCS_Shares.xlsx");
            baseExecutor.storeInBaseAllMusic(QueryHolder.TABLE_ALL_MUSIC, mcs);

            List<Track> msg = parser.loadAllMCSandMGS(CAT_HOME + "Music Sales Group Shares.xlsx");
            baseExecutor.storeInBaseAllMusic(QueryHolder.TABLE_ALL_MUSIC, msg);

//            List<Track> mall = parser.loadAllMCSandMGS(catHome + "Russia_11092012.xlsx");
//            toBase.storeInBaseAllMusic(QueryHolder.TABLE_MCS, mall);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void storeCatalogsToBase() throws IOException, InvalidFormatException, SQLException {

        CatalogParser parser = new CatalogParser();

        List<Track> warnerAut = parser.loadData(
                CAT_HOME + "warner_chapel.xlsx", false, 97.5f, "WCh авторские");
        baseExecutor.storeInBase(QueryHolder.TABLE_WRCH_SONGS, warnerAut);

        List<Track> nmiAutZap = parser.loadData(
                CAT_HOME + "nmi_aut_zap.xlsx", false, 97.5f, "НМИ авторские");
        baseExecutor.storeInBase(QueryHolder.TABLE_NMI_SONGS, nmiAutZap);

        List<Track> nmiAut = parser.loadData(
                CAT_HOME + "nmi_aut.xlsx", false, 70f, "НМИ авторские");
        baseExecutor.storeInBase(QueryHolder.TABLE_NMI_SONGS, nmiAut);

        List<Track> pmiAutZap = parser.loadData(
                CAT_HOME + "pmi_aut_zap.xlsx", false, 97.5f, "ПМИ авторские");
        baseExecutor.storeInBase(QueryHolder.TABLE_PMI_SONGS, pmiAutZap);

        List<Track> pmiAut = parser.loadData(
                CAT_HOME + "pmi_aut.xlsx", false, 70f, "ПМИ авторские");
        baseExecutor.storeInBase(QueryHolder.TABLE_PMI_SONGS, pmiAut);

        List<Track> nmiCom = parser.loadData(
                CAT_HOME + "nmi_com.xlsx", true, 70f, "НМИ смежные");
        baseExecutor.storeInBase(QueryHolder.TABLE_NMI_SONGS_COM, nmiCom);


        List<Track> pmiCom = parser.loadData(
                CAT_HOME + "pmi_com.xlsx", true, 70f, "ПМИ смежные");
        baseExecutor.storeInBase(QueryHolder.TABLE_PMI_SONGS_COM, pmiCom);
//

    }

    private static void loadCatalog(CatalogStorage catalog) throws IOException, InvalidFormatException {

        List<Track> authItems = new ArrayList<Track>();
        List<Track> commonItems = new ArrayList<Track>();
        CatalogParser parser = new CatalogParser();


        List<Track> warnerAut = parser.loadData(
                CAT_HOME + "warner_chapel.xlsx", false, 97.5f, "WCh авторские");
        authItems.addAll(warnerAut);
//
        List<Track> nmiAutZap = parser.loadData(
                CAT_HOME + "nmi_aut_zap.xlsx", false, 97.5f, "НМИ авторские");
        authItems.addAll(nmiAutZap);

//
        List<Track> nmiAut = parser.loadData(
                CAT_HOME + "nmi_aut.xlsx", false, 70f, "НМИ авторские");
//
        authItems.addAll(nmiAut);

        List<Track> pmiAutZap = parser.loadData(
                CAT_HOME + "pmi_aut_zap.xlsx", false, 97.5f, "ПМИ авторские");
        authItems.addAll(pmiAutZap);

        List<Track> pmiAut = parser.loadData(
                CAT_HOME + "pmi_aut.xlsx", false, 70f, "ПМИ авторские");
        authItems.addAll(pmiAut);

        List<Track> nmiCom = parser.loadData(
                CAT_HOME + "nmi_com.xlsx", true, 70f, "НМИ смежные");
        commonItems.addAll(nmiCom);


        List<Track> pmiCom = parser.loadData(
                CAT_HOME + "pmi_com.xlsx", true, 70f, "ПМИ смежные");
        commonItems.addAll(pmiCom);

        System.out.println("Processing " + authItems.size() + " items to database...");


        System.out.println("Processing common Tracks " + commonItems.size());
        System.out.println("Processing auth Tracks " + authItems.size());

        for (Track i : authItems) {
            catalog.addItem(i, false);
        }

        for (Track i : commonItems) {
            catalog.addItem(i, true);
        }
        System.out.println("CommonMap size : " + catalog.commonItemsMap.size());
        System.out.println("AuthMap size : " + catalog.authItemsMap.size());
    }

    public static void buildMobileReport(CatalogStorage catalog, List<ReportItem> reportItems) {

        System.out.println("Building mobile report...");

        int idx = 1;
//        String sep = ";";
        String sep = "^";

        for (ReportItem ri : reportItems) {

            Track authTrack = catalog.search(ri.getAuthor(), ri.getCompisition(), false);
            Track comTrack = catalog.search(ri.getAuthor(), ri.getCompisition(), true);

            if (DEBUG) {
                System.out.println(authTrack != null ? authTrack : "Not found :(");
                System.out.println();
            } else {
                if (authTrack != null || comTrack != null) {

                    Track comp = authTrack != null ? authTrack : comTrack;

                    float authRate = 0;
                    int authorRevenue = 0;
                    int publisherAuthRevenue = 0;
                    String authCatalog = "";
                    if (authTrack != null) {
                        authRate = authTrack.getMobileRate();
                        authorRevenue = Math.round(ri.getQty() * ri.getPrice() * ri.getRate() * authRate / 100);
                        publisherAuthRevenue = Math.round(authorRevenue * authTrack.getRoyalty() / 100);
                        authCatalog = authTrack.getCatalog();
                    }

                    float commonRate = 0;
                    int commonRevenue = 0;
                    int publisherCommonRevenue = 0;
                    String comCatalog = "";
                    if (comTrack != null) {
                        commonRate = comTrack.getMobileRate();
                        commonRevenue = Math.round(ri.getQty() * ri.getPrice() * ri.getRate() * commonRate / 100);
                        publisherCommonRevenue = Math.round(commonRevenue * comTrack.getRoyalty() / 100);
                        comCatalog = comTrack.getCatalog();
                    }

                    String musicAuthors = comp.getMusicAuthors() != null ? comp.getMusicAuthors() : "";
                    String lyricsAuthors = comp.getLyricsAuthors() != null ? comp.getLyricsAuthors() : "";

                    System.out.println(
                            idx++ + sep +
                                    comp.getCode() + sep +
                                    comp.getComposition() + sep +
                                    musicAuthors + sep +
                                    lyricsAuthors + sep +
                                    comp.getArtist() + sep +
                                    ri.getContentType() + sep +
                                    authRate + sep +
                                    ri.getQty() + sep +
                                    ri.getPrice() + sep +
                                    comp.getRoyalty() + sep +
                                    authorRevenue + sep +
                                    publisherAuthRevenue + sep +
                                    commonRate + sep +
                                    commonRevenue + sep +
                                    publisherCommonRevenue + sep +
                                    authCatalog + sep +
                                    comCatalog + sep
                    );

                }
            }
        }
    }


    private static void buildRadioReport(CatalogStorage catalog, List<ReportItem> items) {
        int idx = 1;
//        String sep = ";";
        String sep = "^";

        for (ReportItem item : items) {

            Track track = catalog.search(item.getAuthor(), item.getCompisition(), false);
            if (track != null) {
                System.out.println(idx++ + sep +
                        track.getCode() + sep +
                        track.getComposition() + sep +
                        track.getMusicAuthors() + sep +
                        track.getLyricsAuthors() + sep +
                        track.getPublicRate() + sep +
                        track.getCatalog() + sep
                );
            }
        }
    }

    public static void mergeReports(List<ReportItem> items, List<ReportItem> nextItems) {

        if (items.isEmpty()) {
            items.addAll(nextItems);
        } else {
            for (ReportItem ni : nextItems) {
                ReportItem found = null;
                for (ReportItem i : items) {
                    if (ni.getAuthor().equalsIgnoreCase(i.getAuthor()) &&
                            ni.getCompisition().equalsIgnoreCase(i.getCompisition()) &&
                            ni.getContentType().equalsIgnoreCase(i.getContentType()) &&
                            ni.getPrice() == i.getPrice()
                            ) {
                        found = i;
                        break;
                    }
                }
                if (found == null) {
                    items.add(ni);
                } else {
                    found.setQty(found.getQty() + ni.getQty());
                }
            }
        }
    }

    public List<Track> getTrackBySongName(String songName) {
        return baseExecutor.getTracksEqSongName(songName);
    }

    public List<Track> getTracksLikeArtist(String songName) {
        return baseExecutor.getTracksLikeArtist(songName);
    }

    public List<Track> getTracksByArtist(String songName) {
        return baseExecutor.getTracksByArtist(songName);
    }

    public List<Track> getTracksByAllNames(String value) {
        return baseExecutor.getTracksByAllNames(value);
    }

    public List<Track> getTracksLikeAllNames(String value) {
        return baseExecutor.getTracksLikeAllNames(value);
    }


    public List<Track> getTrackListLikeSongName(String songName) {
        return baseExecutor.getTracksLikeSongName(songName);
    }

    public List<Track> getTrackListByUid(String uid) {
        return baseExecutor.getTracksByUid(uid);
    }

    public static void main(String[] args) throws IOException, InvalidFormatException, ClassNotFoundException, SQLException {
        //todo  авто проверка каталогов (добаслять имена публишеров в файл ?)
//
        CatalogStorage catalog = new CatalogStorage(homeDir + "/db.properties");


        //add tracs in DB=======================================================
        if (args.length != 2) {
            System.out.println("Enter args 'filename' 'publisher'");
        } else {
            catalog.loadCatalogToBd(args[0], args[1]);
        }
        //======================================================================

        //load tracks from base================================================


//        loadCatalogFromBD(catalog);
//        List<ReportItem> items = new ArrayList<ReportItem>();
//        mergeReports(items, new ReportParser().
// loadClientReport("./data/October_2012_BGM (1).xlsx", 0.125f));
//        mergeReports(items, new ReportParser().
// loadClientReport("./data/November_2012_BGM (1).xlsx", 0.125f));
//        buildMobileReport(catalog, items);
//        CatalogStorage ct = new CatalogStorage(homeDir + "/db.properties");
//        if (args.length != 0) {
//            if ("test".equals(args[0])) {
//        System.out.println("Testing connection");
//        System.out.println("");
//        System.out.println("");
//
//        ct = new CatalogStorage(homeDir + "/db.properties");
//        Connection con = ct.getConnection();
//        con.close();
//        System.exit(0);
//            }
//        } else {
//            ct.storeCatalogsToBase();
//        }
//
//        loadCatalog(catalog);
//        System.out.println();
//        List<ReportItem> items = new ArrayList<ReportItem>();
//        mergeReports(items, new ReportParser().loadClientReport("./data/October_2012_BGM (1).xlsx", 0.125f));
//        mergeReports(items, new ReportParser().loadClientReport("./data/November_2012_BGM (1).xlsx", 0.125f));
//        System.out.println();
//        buildMobileReport(catalog, items);

//        System.out.println();
//        List<ReportItem> items = MoskvafmParser.parseReport("./data/moskvafm-top-by-channels.txt");
//        System.out.println();
//        buildRadioReport(catalog, items);

    }
}
