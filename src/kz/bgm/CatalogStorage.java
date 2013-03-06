package kz.bgm;

import kz.bgm.base.BaseConnector;
import kz.bgm.base.FromBase;
import kz.bgm.items.ReportItem;
import kz.bgm.items.Track;
import kz.bgm.parsers.CatalogParser;
import kz.bgm.parsers.ReportParser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogStorage {

    public static final boolean DEBUG = false;
//    public static final boolean DEBUG = true;

    public static final DataFormatter FORMATTER = new DataFormatter(true);

    private Map<String, List<Track>> authItemsMap;
    private Map<String, List<Track>> commonItemsMap;


    public CatalogStorage() {
        authItemsMap = new HashMap<String, List<Track>>();
        commonItemsMap = new HashMap<String, List<Track>>();
    }


    public void addItem(Track track, boolean common) {
        String artist = track.getArtist();
        if (artist == null || "".equals(artist)) return;

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

    private static String homeDir = System.getProperty("user.dir");

    private static void loadCatalogFromBD(CatalogStorage catalog) {
        BaseConnector bs = new BaseConnector();
        FromBase fromBase = new FromBase(bs.connect("root", "root"));

        //todo все таки в БД надо сравнивать, а не в память выкачивать )
        List<Track> comTrackList = fromBase.getAllTracksCom();
        List<Track> pubTrackList = fromBase.getAllTracksPub();

        System.out.println("Processing " + comTrackList.size() + pubTrackList.size() + " items to database...");
        for (Track i : pubTrackList) {
            catalog.addItem(i, false);
        }
        System.out.println();
        for (Track i : comTrackList) {
            catalog.addItem(i, true);
        }

        System.out.println(catalog.commonItemsMap.size());
        System.out.println(catalog.authItemsMap.size());
    }

    private static void loadCatalog(CatalogStorage catalog) throws IOException, InvalidFormatException {
        String catHome = homeDir + "/data/catalog/";

        List<Track> authItems = new ArrayList<Track>();
        List<Track> commonItems = new ArrayList<Track>();
        CatalogParser parser = new CatalogParser();


        List<Track> warnerAut = parser.loadData(
                catHome + "warner_chapel.xlsx", false, 97.5f, "WCh авторские");
        authItems.addAll(warnerAut);
//
        List<Track> nmiAutZap = parser.loadData(
                catHome + "nmi_aut_zap.xlsx", false, 97.5f, "НМИ авторские");
        authItems.addAll(nmiAutZap);

//
        List<Track> nmiAut = parser.loadData(
                catHome + "nmi_aut.xlsx", false, 70f, "НМИ авторские");
//
        authItems.addAll(nmiAut);

        List<Track> pmiAutZap = parser.loadData(
                catHome + "pmi_aut_zap.xlsx", false, 97.5f, "ПМИ авторские");
        authItems.addAll(pmiAutZap);

        List<Track> pmiAut = parser.loadData(
                catHome + "pmi_aut.xlsx", false, 70f, "ПМИ авторские");
        authItems.addAll(pmiAut);

        List<Track> nmiCom = parser.loadData(
                catHome + "nmi_com.xlsx", true, 70f, "НМИ смежные");
        commonItems.addAll(nmiCom);


        List<Track> pmiCom = parser.loadData(
                catHome + "pmi_com.xlsx", true, 70f, "ПМИ смежные");
        commonItems.addAll(pmiCom);

        System.out.println("Processing " + authItems.size() + " items to database...");

        for (Track i : authItems) {
            catalog.addItem(i, false);
        }

        for (Track i : commonItems) {
            catalog.addItem(i, true);
        }
        System.out.println(catalog.commonItemsMap.size());
        System.out.println(catalog.authItemsMap.size());
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

    public static void main(String[] args) throws IOException, InvalidFormatException, ClassNotFoundException {
//
        CatalogStorage catalog = new CatalogStorage();
//        loadCatalogFromBD(catalog);
//        List<ReportItem> items = new ArrayList<ReportItem>();
//        mergeReports(items, new ReportParser().loadClientReport("./data/October_2012_BGM (1).xlsx", 0.125f));
//        mergeReports(items, new ReportParser().loadClientReport("./data/November_2012_BGM (1).xlsx", 0.125f));
//        buildMobileReport(catalog, items);
////
//
        loadCatalog(catalog);
        System.out.println();
        List<ReportItem> items = new ArrayList<ReportItem>();
        mergeReports(items, new ReportParser().loadClientReport("./data/October_2012_BGM (1).xlsx", 0.125f));
        mergeReports(items, new ReportParser().loadClientReport("./data/November_2012_BGM (1).xlsx", 0.125f));
        System.out.println();
        buildMobileReport(catalog, items);

//        System.out.println();
//        List<ReportItem> items = MoskvafmParser.parseReport("./data/moskvafm-top-by-channels.txt");
//        System.out.println();
//        buildRadioReport(catalog, items);

    }
}
