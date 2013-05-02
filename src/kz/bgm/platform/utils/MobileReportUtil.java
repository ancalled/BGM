package kz.bgm.platform.utils;

import kz.bgm.platform.model.domain.CalculatedReportItem;
import kz.bgm.platform.model.domain.CustomerReportItem;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.model.service.LuceneSearch;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class MobileReportUtil {

    private static String APP_DIR = System.getProperty("user.dir");

    private static final String REPS_PATH = APP_DIR + "/reports";
    private static final String COMPUTED_REPS_PATH = APP_DIR + "/computed-reports";

    private LuceneSearch luceneSearch;
    private ReportCalculator calculator;

    MobileReportUtil(String fileProps) {
        luceneSearch = LuceneSearch.getInstance();
        calculator = new ReportCalculator(fileProps);
    }

    public List<CustomerReportItem> loadReport(String filename) throws IOException, InvalidFormatException {
        if (filename == null) {
            System.out.println("File name is null");
            return Collections.emptyList();
        }

        System.out.println("\n\nLoading report...");

        List<CustomerReportItem> reportList = ReportParser.parseMobileReport(REPS_PATH + "/" + filename);

        System.out.println("Report loaded, track count " + reportList.size());

        return reportList;
    }


    public List<CustomerReportItem> findSongsByReportsFromDB(List<CustomerReportItem> reports)
            throws IOException, ParseException {
        if (reports == null || reports.isEmpty()) {
            System.out.println("Calculated reports is empty or null");
            return Collections.emptyList();
        }

        int detected = 0;

        System.out.println("\n\nSearching songs by report items");
        List<CustomerReportItem> found = new ArrayList<>();
        for (CustomerReportItem i : reports) {
            List<Long> ids = luceneSearch.search(i.getArtist(), i.getName());
            if (ids.size() > 0) {
                found.add(i);
                detected++;
            }
        }
        System.out.println("Songs detected " + detected);
        return found;
    }

    public List<CalculatedReportItem> calculateReports(List<CustomerReportItem> reps) {
        if (reps == null || reps.isEmpty()) {
            System.out.println("Customers report items is empty or null");
            return Collections.emptyList();
        }
        return calculator.calculateReports(reps);
    }

    public void saveInFile(String fileTemplate, List<CalculatedReportItem> reps) {
        if (reps == null || reps.isEmpty()) {
            System.out.println("Calculated reports is empty or null");
            return;
        }

        ReportBuilder.buildReportExcelFile(fileTemplate, reps);
    }


    private static class ReportCalculator {

        public static final String BASE_NAME = "base.name";
        public static final String BASE_LOGIN = "base.login";
        public static final String BASE_PASS = "base.pass";
        public static final String BASE_HOST = "base.host";
        public static final String BASE_PORT = "base.port";

        private CatalogStorage storage;
        private String propsFile;

        private ReportCalculator(String propsFile) {
            this.propsFile = propsFile;
        }

        public static void initDatabase(String propsFile) {
            FileInputStream fis = null;
            try {
                Properties props = new Properties();
                fis = new FileInputStream(propsFile);
                props.load(fis);

                String dbHost = props.getProperty(BASE_HOST);
                String dbPort = props.getProperty(BASE_PORT);
                String dbName = props.getProperty(BASE_NAME);
                String dbLogin = props.getProperty(BASE_LOGIN);
                String dbPass = props.getProperty(BASE_PASS);

                System.out.println("Initializing data storage...");
                CatalogFactory.initDBStorage(dbHost, dbPort, dbName, dbLogin, dbPass);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        public List<CalculatedReportItem> calculateReports(List<CustomerReportItem> reps) {
            if (reps == null || reps.isEmpty()) {
                return Collections.emptyList();
            }
            if (storage == null) {
                initDatabase(propsFile);
                storage = CatalogFactory.getStorage();
            }
            System.out.println("calculating reports..");
            List<CalculatedReportItem> calcReps = storage.calculateMobileReport("");
            System.out.println("reps calculated");
            return calcReps;
        }
    }


    public static void main(String[] args) throws IOException, InvalidFormatException, ParseException {
        String filename = null;
        if (args.length == 0) {
            System.out.println("Not enough args ... arg = file name");
            System.exit(0);
        } else {
            filename = args[0];
        }

        System.out.println("Start process the reports");
        MobileReportUtil mobUts = new MobileReportUtil("db.properties");

        List<CustomerReportItem> reportItemList = mobUts.loadReport(filename);
        List<CalculatedReportItem> calcRepList = new ArrayList<>();
        reportItemList = mobUts.findSongsByReportsFromDB(reportItemList);

        calcRepList = mobUts.calculateReports(reportItemList);

        mobUts.saveInFile(APP_DIR + "/data/report-templates/sony-atv.xlsx", calcRepList);
        System.out.println("Process done");
    }

}
