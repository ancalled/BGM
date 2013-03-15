package kz.bgm.platform;

import kz.bgm.platform.items.Track;
import kz.bgm.platform.parsers.CatalogParser;
import kz.bgm.platform.service.CatalogStorage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CatalogLoader {

    public static String homeDir = System.getProperty("user.dir");

    public static final String CAT_HOME = homeDir + "/catalogs/";


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


        catalog.storeInCatalog(authItems, false);
        catalog.storeInCatalog(commonItems, true);

    }


    public static void main(String[] args) throws IOException, InvalidFormatException, ClassNotFoundException, SQLException {
        //todo  авто проверка каталогов (добаслять имена публишеров в файл ?)
//
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
