package kz.bgm.platform;

import kz.bgm.platform.items.ReportItem;
import kz.bgm.platform.items.Track;
import kz.bgm.platform.parsers.ReportParser;
import kz.bgm.platform.service.CatalogStorage;
import kz.bgm.platform.service.DbStorage;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportBuilder {

    public static final boolean DEBUG = false;
    public static final String APP_DIR = System.getProperty("user.dir");

    public static final String REPORTS_DIR = APP_DIR + "/reports";

    private static final Logger log = Logger.getLogger(ReportBuilder.class);

    //todo Доделать ReportBuilder
    public static List<Track> buildMobileReport(CatalogStorage catalog, List<ReportItem> reportItems) {

        log.info("Building mobile report...");
        List<Track> trackList = new ArrayList<Track>();

        int idx = 1;
//        String sep = ";";
        String sep = "^";
        for (ReportItem report : reportItems) {

            Track track = catalog.search(report.getAuthor(), report.getCompisition());

            if (track != null) {

                trackList.add(track);
//                float royalty = catalog.getRoyalty(track.getCatalogID());


                //todo определиться с расчетами
//                float authRate = 0;
//                int authorRevenue = 0;
//                int publisherAuthRevenue = 0;
//                int authCatalog = -1;
//
//                authRate = track.getMobileShare();
//                authorRevenue = Math.round(report.getQty() * report.getPrice() * report.getRate() * authRate / 100);
//                publisherAuthRevenue = Math.round(authorRevenue * royalty / 100);
//                authCatalog = track.getCatalogID();
//
//                String composers = track.getComposer();

//                System.out.println(
//                        idx++ + sep +
//                                track.getCode() + sep +
//                                track.getName() + sep +
//                                composers + sep +
//                                track.getArtist() + sep +
//                                report.getContentType() + sep +
//                                authRate + sep +
//                                report.getQty() + sep +
//                                report.getPrice() + sep +
//                                royalty + sep +
//                                authorRevenue + sep +
//                                publisherAuthRevenue + sep +
//                                authCatalog + sep
//                );

            }
        }
        return trackList;
    }

    private static String lBase = "bgm";
    private static String lLogin = "root";
    private static String lPass = "root";
    private static String lHost = "localhost";
    private static String lPort = "3306";


    public static void main(String[] args) throws IOException, InvalidFormatException {
        CatalogStorage dbStore = new DbStorage(lHost, lPort, lBase, lLogin, lPass);


        ReportParser rp = new ReportParser();

        List<ReportItem> reportList = rp.loadClientReport(REPORTS_DIR + "/Отчетная ведемость для -Beeline.xls", 0);

        buildMobileReport(dbStore, reportList);
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
                        track.getName() + sep +
                        track.getComposer() + sep +
                        track.sharePublic() + sep +
                        track.getCatalogID() + sep
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


}


