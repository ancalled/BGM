package kz.bgm.platform.utils;


import kz.bgm.platform.model.domain.CalculatedReportItem;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static kz.bgm.platform.BgmServer.*;

public class ReportBuilder {

    private static final Logger log = Logger.getLogger(ReportBuilder.class);

    private static final String COMPOSITION_CODE = "{compositionCode}";
    private static final String COMPOSITION_NAME = "{compositionName}";
    private static final String ARTIST = "{artist}";
    private static final String COMPOSER = "{composer}";
    private static final String CONTENT_TYPE = "{contentType}";
    private static final String PRICE = "{price}";
    private static final String QTY_SUM = "{qty}";
    private static final String VOL = "{vol}";
    private static final String SHARE_MOBILE = "{shareMobile}";
    private static final String CUSTOMER_ROYALTY = "{customerRoyalty}";
    private static final String CATALOG_ROYALTY = "{catalogRoyalty]";
    private static final String REVENUE = "{revenue}";
    private static final String CATALOG = "{catalog}";
    private static final String COPYRIGHT = "{copyright}";
    private static final String SHARE_PUBLIC = "{sharePublic}";

    private static List<String> fieldList = new ArrayList<>();

    static {
        fieldList.add(COMPOSITION_CODE);
        fieldList.add(COMPOSITION_NAME);
        fieldList.add(ARTIST);
        fieldList.add(COMPOSER);
        fieldList.add(CONTENT_TYPE);
        fieldList.add(PRICE);
        fieldList.add(QTY_SUM);
        fieldList.add(VOL);
        fieldList.add(SHARE_MOBILE);
        fieldList.add(CUSTOMER_ROYALTY);
        fieldList.add(CATALOG_ROYALTY);
        fieldList.add(REVENUE);
        fieldList.add(CATALOG);
        fieldList.add(COPYRIGHT);
        fieldList.add(SHARE_PUBLIC);
    }


    public static void buildReport(String template, String output,
                                   List<CalculatedReportItem> items) {
        try {
            log.info("Making Excel file report from file: " + template);

            File reportBlank = new File(template);
            Workbook wb = ExcelUtils.openFile(reportBlank);
            Sheet sheet = wb.getSheetAt(1);
            log.info("Parsing sheet '" + sheet.getSheetName() + "'");

            Map<String, Integer> fieldsMap = new HashMap<>();

            int startRow = 0;
            for (int r = 0; r < 50; r++) {
                Row row = sheet.getRow(r);
                fieldsMap = getFields(row);

                if (fieldsMap != null) {
                    startRow = r;
                    break;
                }
            }
            log.info("filling data report");
            fillExcelBlank(sheet, startRow, fieldsMap, items);
            log.info("data filled");


            log.info("saving in file " + output);
            ExcelUtils.saveFile(wb, output);

            log.info("saved");

        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }

    private static void fillExcelBlank(Sheet sheet,
                                       int rowIdx, Map<String, Integer> fields,
                                       List<CalculatedReportItem> reports) {
        if (rowIdx <= 0 || fields == null ||
                reports == null || sheet == null) {
//            System.out.println("reports "+reports);
//            System.out.println(" fields "+fields);
//            System.out.println("sheet "+sheet);
//            System.out.println("sheet "+sheet);
            return;
        }

        for (CalculatedReportItem report : reports) {
            ExcelUtils.shiftRowsDown(sheet, rowIdx);

            fillValues(report, sheet, fields, rowIdx);
//            rowIdx++;
        }
    }


    private static void fillValues(CalculatedReportItem report, Sheet sheet,
                                   Map<String, Integer> fieldMap, int rowIdx) {

        try {

            Class reportClass = report.getClass();
//            CalculatedReportItem instance = (CalculatedReportItem) reportClass.newInstance();
            List<Method> fields = getReportFields(reportClass);
            String field;

            for (String fieldName : fieldMap.keySet()) {

                field = fieldName.replaceAll("}", "");
                field = field.replaceAll("\\{", "");

                int colIdx;
                Type type;
                Object val;
                for (Method m : fields) {
                    String methodName = m.getName().toLowerCase().replaceFirst("get", "");   //think rabbit

                    if (methodName.equals(field.toLowerCase())) {
                        type = m.getGenericReturnType();
                        val = m.invoke(report);
                        colIdx = fieldMap.get(fieldName);

                        ExcelUtils.setValue(sheet,
                                val,
                                type,
                                rowIdx,
                                colIdx);
                    }
//                    else {
//                        System.out.println("false");
//                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private static List<Method> getReportFields(Class cls) {
        Method[] metMass = cls.getMethods();
        List<Method> methodList = new ArrayList<>();

        for (Method m : metMass) {
            if (m.getName().startsWith("get")) {
                methodList.add(m);
            }
        }
        return methodList;
    }

    private static Map<String, Integer> getFields(Row row) {
        if (row == null) return null;

        Map<String, Integer> columnMap = new HashMap<>();

        for (String field : fieldList) {

            for (int c = 0; c < 100; c++) {
                String val = ExcelUtils.getCellVal(row, c);
                String result = getIfEquals(field, val);
                //todo make values like massive for many column indexes
                if (result != null) {
                    ExcelUtils.clearCell(row, c);
                    if (!columnMap.containsKey(result)) {
                        columnMap.put(result, c);
                    }
                }
            }
        }

        if (columnMap.isEmpty()) {
            return null;
        } else {
            return columnMap;
        }
    }

    private static String getIfEquals(String val1, String val2) {
        if (val1.equals(val2)) {
            return val1;
        } else {
            return null;
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

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");


    public static void main(String[] args) throws ParseException {

        if (args.length < 6) {
            System.err.println("Not enough params!");
            System.out.println("Expected: $TYPE $PLATFORM $TEMPLATE $OUTPUT $FROM_DATE $TO_DATE");
            return;
        }

        String type = args[0];
        String platform = args[1];
        String template = args[2];
        String output = args[3];

        Date from = DATE_FORMAT.parse(args[4]);
        Date to = DATE_FORMAT.parse(args[5]);


//        String platform = "1mp";
//        String template = "./data/report-templates/" + platform + ".xlsx";
//        String output = "/home/ancalled/Documents/tmp/25/bgm/tmp-" + platform + "res.xlsx";
//        Date from = DATE_FORMAT.parse("2013-04-01");
//        Date to = DATE_FORMAT.parse("2013-08-01");

        try {
            initDatabase("db.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CatalogStorage storage = CatalogFactory.getStorage();

        List<CalculatedReportItem> items;
        switch (type) {
            case "mobile":
                items = storage.calculateMobileReport(platform, from, to);
                break;
            case "public":
                items = storage.calculatePublicReport(platform, from, to);
                break;
            default:
                System.err.println("Unknown report type: " + type);
                return;
        }


        System.out.println("Got " + items.size() + " items.");

        buildReport(template, output, items);
    }

}













