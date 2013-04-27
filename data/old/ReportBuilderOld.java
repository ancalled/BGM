package kz.bgm.platform;

import kz.bgm.platform.utils.ExcelUtils;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class ReportBuilderOld {

    public static final boolean DEBUG = false;
    public static final String APP_DIR = System.getProperty("user.dir");

    public static final String REPORTS_DIR = APP_DIR + "/reports";


    @Deprecated
    public static List<ReportItem> buildMobileReport(
            CatalogStorage catalogStorage,
            String filename, float clientRate) throws IOException, InvalidFormatException {
        System.out.println("Parsing mobile report ...");
        List<ReportItem> reportList = loadClientReport(filename, clientRate);
        System.out.println("Parsing finished");

        System.out.println("Building mobile report...");

        for (ReportItem report : reportList) {

            String artist = report.getArtist();
            String composition = report.getCompisition();
            Track track = catalogStorage.search(artist, composition);

            if (track != null) {

                float royalty = catalogStorage.getRoyalty(track.getCatalogID());


                float authRate = 0;
                int authorRevenue = 0;
                int publisherAuthRevenue = 0;
                String catalogTitle = "";

                String composers = track.getComposer();


                authRate = track.getMobileShare();
                authorRevenue = Math.round(report.getQty() * report.getPrice() * report.getRate() * authRate / 100);
                publisherAuthRevenue = Math.round(authorRevenue * royalty / 100);

                report.setCatalog(track.getCatalog());
                report.setCode(track.getCode());
                report.setComposer(composers);
                report.setAuthRate(authRate);
                report.setAuthorRevenue(authorRevenue);
                report.setPublisherAuthRevenue(publisherAuthRevenue);
//
                System.out.println("Report item :");
                System.out.println("ID:                        " + track.getId() + "\n" +
                        "Code:                      " + track.getCode() + "\n" +
                        "Name:                      " + track.getName() + "\n" +
                        "Composer:                  " + composers + "\n" +
                        "Artist:                    " + track.getArtist() + "\n" +
                        "Content type:              " + report.getContentType() + "\n" +
                        "Author rate:               " + authRate + "\n" +
                        "Qty:                       " + report.getQty() + "\n" +
                        "Price:                     " + report.getPrice() + "\n" +
                        "Royalty:                   " + royalty + "\n" +
                        "Author Revenue:            " + authorRevenue + "\n" +
                        "Publisher author revenue:  " + publisherAuthRevenue + "\n" +
                        "Catalog:                   " + catalogTitle + "\n"
                );
//
            } else {
                System.out.println("Composition '" + composition + "' with artist '" + artist + "' not found");
            }
        }
        System.out.println("Mobile Report build.");

        return reportList;
    }

    private static void buildRadioReport(CatalogStorage catalog, List<ReportItem> items) {
        int idx = 1;
//        String sep = ";";
        String sep = "^";

        for (ReportItem item : items) {

            Track track = catalog.search(item.getArtist(), item.getCompisition(), false);
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
                    if (ni.getArtist().equalsIgnoreCase(i.getArtist()) &&
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

    @Deprecated
    public static List<ReportItem> loadClientReport(String filename, float clientRate)
            throws IOException, InvalidFormatException {

        File file = new File(filename);
        long startTime = System.currentTimeMillis();
        System.out.println("Loading " + file.getName() + "... ");

        Workbook wb = ExcelUtils.openFile(file);

        List<ReportItem> items = new ArrayList<ReportItem>();
//        wb.getNumberOfSheets();
        Sheet sheet = wb.getSheetAt(1);
        int rows = sheet.getPhysicalNumberOfRows();

        System.out.println("Parsing sheet '" + sheet.getSheetName() + "' with " + rows + " rows");

        int startRow = 6;
        for (int i = startRow; i < rows; i++) {
            Row row = sheet.getRow(i);
            String num = ExcelUtils.getCellVal(row, 0);

            if (num == null || "".equals(num.trim())) continue;

            ReportItem item = new ReportItem();
            item.setCompisition(ExcelUtils.getCellVal(row, 2));
            item.setArtist(ExcelUtils.getCellVal(row, 3));
            item.setContentType(ExcelUtils.getCellVal(row, 4));

            String priceStr = ExcelUtils.getCellVal(row, 5);

            if (priceStr == null || "".equals(priceStr.trim())) continue;

            priceStr = priceStr.replace(",", ".");

            item.setPrice(Float.parseFloat(priceStr));

            String qtyStr = ExcelUtils.getCellVal(row, 6).trim();

            if (qtyStr == null || "".equals(qtyStr)) continue;

            item.setQty(Integer.parseInt(qtyStr));
            item.setRate(clientRate);
            items.add(item);
        }

        long endTime = System.currentTimeMillis();
        long proc = (endTime - startTime) / 1000;
        System.out.println("Got " + items.size() + " items in " + proc + " sec.");

        return items;
    }


//    private static String lBase = "bgm";
//    private static String lLogin = "root";
//    private static String lPass = "root";
//    private static String lHost = "localhost";
//    private static String lPort = "3306";
//
//
//    public static void main(String[] args) throws IOException, InvalidFormatException {
//        CatalogStorage dbStore = new DbStorage(lHost, lPort, lBase, lLogin, lPass);
//
//
//        ReportParser rp = new ReportParser();
//
//
//
//        buildMobileReport(dbStore, reportList);
//    }


}


