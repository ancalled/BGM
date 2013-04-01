package kz.bgm.platform;

import kz.bgm.platform.items.Track;
import kz.bgm.platform.parsers.CatalogParser;
import kz.bgm.platform.service.CatalogStorage;
import kz.bgm.platform.service.DbStorage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CatalogLoader {

    public static String homeDir = System.getProperty("user.dir");

    public static final String CAT_HOME = homeDir + "/catalogs/";

    private static final String BASE_NAME = "base.name";
    private static final String BASE_LOGIN = "base.login";
    private static final String BASE_PASS = "base.pass";
    private static final String BASE_HOST = "base.host";
    private static final String BASE_PORT = "base.port";

    private static void loadCatalog(CatalogStorage catalog, String catalogName) throws IOException, InvalidFormatException {
        List<Track> authItems = new ArrayList<Track>();
        List<Track> commonItems = new ArrayList<Track>();
        CatalogParser parser = new CatalogParser();

        if (catalogName.equals("WCh")) {

            List<Track> warnerAut = parser.loadData(
                    CAT_HOME + "warner_chapel.xlsx", false);
            authItems.addAll(warnerAut);

        } else if (catalogName.equals("NMI_WEST")) {

            List<Track> nmiAutZap = parser.loadData(
                    CAT_HOME + "nmi_aut_zap.xlsx", false);
            authItems.addAll(nmiAutZap);

        } else if (catalogName.equals("NMI")) {
//
            List<Track> nmiAut = parser.loadData(
                    CAT_HOME + "nmi_aut.xlsx", false);
            authItems.addAll(nmiAut);

        } else if (catalogName.equals("PMI_WEST")) {

            List<Track> pmiAutZap = parser.loadData(
                    CAT_HOME + "pmi_aut_zap.xlsx", false);
            authItems.addAll(pmiAutZap);

        } else if (catalogName.equals("PMI")) {

            List<Track> pmiAut = parser.loadData(
                    CAT_HOME + "pmi_aut.xlsx", false);
            authItems.addAll(pmiAut);

        } else if (catalogName.equals("NMI related")) {

            List<Track> nmiCom = parser.loadData(
                    CAT_HOME + "nmi_com.xlsx", true);
            commonItems.addAll(nmiCom);

        } else if (catalogName.equals("PMI related")) {

            List<Track> pmiCom = parser.loadData(
                    CAT_HOME + "pmi_com.xlsx", true);
            commonItems.addAll(pmiCom);
        }


        System.out.println("Processing " + authItems.size() + " items to database...");

        System.out.println("Processing auth Tracks " + authItems.size());

        if (!authItems.isEmpty()) {
            catalog.storeInCatalog(authItems, catalogName);
        }
        if (!commonItems.isEmpty()) {
            catalog.storeInCatalog(commonItems, catalogName);
        }

    }

    public static void loadSony(CatalogStorage catalog, String fileName, String catalogName) throws IOException, InvalidFormatException {
        List<Track> trackList = new ArrayList<Track>();
        CatalogParser parser = new CatalogParser();

        if ("MSG_MCS".equals(catalogName)) {
            trackList = parser.loadMGS(
                    CAT_HOME + fileName);

        } else if ("SONY".equals(catalogName)) {
            trackList = parser.loadSonyMusic(CAT_HOME + fileName);
        }

        if (!trackList.isEmpty()) {
            catalog.storeInCatalog(trackList, catalogName);
        }
    }

    public static void main(String[] args) throws IOException, InvalidFormatException, ClassNotFoundException, SQLException {


        Properties props = new Properties();
        props.load(new FileInputStream("db.properties"));

        String host = props.getProperty(BASE_HOST);
        String port = props.getProperty(BASE_PORT);
        String base = props.getProperty(BASE_NAME);
        String user = props.getProperty(BASE_LOGIN);
        String pass = props.getProperty(BASE_PASS);

        DbStorage dbStorage = new DbStorage(host, port, base, user, pass);

        long startTime = System.currentTimeMillis();
        loadCatalog(dbStorage, "WCh");
        loadCatalog(dbStorage, "NMI_WEST");
        loadCatalog(dbStorage, "NMI");
        loadCatalog(dbStorage, "PMI_WEST");
        loadCatalog(dbStorage, "PMI");
        loadCatalog(dbStorage, "PMI related");
        loadCatalog(dbStorage, "NMI related");

        loadSony(dbStorage, "MCS_Shares.xlsx", "MSG_MCS");
        loadSony(dbStorage, "Music Sales Group Shares.xlsx", "MSG_MCS");
        loadSony(dbStorage, "R1.xlsx", "SONY");
        loadSony(dbStorage, "R2.xlsx", "SONY");
        loadSony(dbStorage, "R3.xlsx", "SONY");

        long endTime = System.currentTimeMillis();
        float doneTime = ((endTime - startTime) / 1000) / 60;
        System.out.print("All catalogs loaded in " + doneTime + " min");

        dbStorage.closeConnection();


//        InMemorycatalog catalog = new InMemorycatalog(homeDir + "/db.properties");
//
//
//        //add tracs in DB=======================================================
//        if (args.length != 2) {
//            System.out.println("Enter args 'filename' 'publisher'");
//        } else {
//            catalog.loadCatalogToBd(args[0], args[1]);
//        }
//        //======================================================================

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
