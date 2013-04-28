package kz.bgm.platform.utils;


import kz.bgm.platform.model.domain.CalculatedReportItem;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

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

    private static List<String> fieldList = new ArrayList<String>();

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
    }


    public static void buildReportExcelFile(String filePath,
                                            List<CalculatedReportItem> finishReps) {
        try {
            log.info("Making Excel file report from file: " + filePath);

            File reportBlank = new File(filePath);
            Workbook wb = ExcelUtils.openFile(reportBlank);
            Sheet sheet = wb.getSheetAt(1);
            log.info("Parsing sheet '" + sheet.getSheetName() + "'");

            Map<String, Integer> fieldsMap = new HashMap<String, Integer>();

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
            fillExcelBlank(sheet, startRow, fieldsMap, finishReps);
            log.info("data filled");

            String fileName = reportBlank.getName() + new Date().getTime();

            log.info("saving in file " + fileName);
            ExcelUtils.saveFile(wb, fileName);

            log.info("saved");
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } catch (InvalidFormatException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }

    private static void fillExcelBlank(Sheet sheet,
                                       int rowIdx, Map<String, Integer> fields,
                                       List<CalculatedReportItem> reports) {
        if (rowIdx <= 0 || fields == null ||
                reports == null || sheet == null) return;

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
            CalculatedReportItem instance = (CalculatedReportItem) reportClass.newInstance();
            List<Method> fields = getReportFields(reportClass);

            String field = null;

            for (String fieldName : fieldMap.keySet()) {
                field = fieldName.replaceAll("}", "");
                field = field.replaceAll("\\{", "");
                int colIdx = 0;
                Type type = null;
                Object val = null;

                for (Method m : fields) {
                    String methodName = m.getName().toLowerCase();
                    if (methodName.contains(field.toLowerCase())) {
                        type = m.getGenericReturnType();
                        val = m.invoke(report);
                        colIdx = fieldMap.get(fieldName);
                    }
                    if (colIdx != 0) {
                        ExcelUtils.setValue(sheet,
                                val,
                                type,
                                rowIdx,
                                colIdx);
                        break;
                    }
                }

            }


        } catch (Exception e) {
            e.printStackTrace();

        }


//        if (fieldMap.containsKey(COMPOSITION_CODE)) {
//            int codeIdx = fieldMap.get(COMPOSITION_CODE);
//            String code = report.getCompositionCode();
//            ExcelUtils.setValue(sheet, code, rowIdx, codeIdx);
//        }
//        if (fieldMap.containsKey(COMPOSITION_NAME)) {
//            int nameIdx = fieldMap.get(COMPOSITION_NAME);
//            String name = report.getCompositionName();
//            ExcelUtils.setValue(sheet, name, rowIdx, nameIdx);
//        }
//        if (fieldMap.containsKey(ARTIST)) {
//            int artistIdx = fieldMap.get(ARTIST);
//            String artist = report.getArtist();
//            ExcelUtils.setValue(sheet, artist, rowIdx, artistIdx);
//        }
//        if (fieldMap.containsKey(COMPOSER)) {
//            int composerIdx = fieldMap.get(COMPOSER);
//            String composer = report.getComposer();
//            ExcelUtils.setValue(sheet, composer, rowIdx, composerIdx);
//        }
//
//        if (fieldMap.containsKey(CONTENT_TYPE)) {
//            int contentIdx = fieldMap.get(CONTENT_TYPE);
//            String content = report.getContentType();
//            ExcelUtils.setValue(sheet, content, rowIdx, contentIdx);
//        }
//
//        if (fieldMap.containsKey(PRICE)) {
//            int priceIdx = fieldMap.get(PRICE);
//            String price = Float.toString(report.getPrice());
//            ExcelUtils.setValue(sheet, price, rowIdx, priceIdx);
//        }
//
//        if (fieldMap.containsKey(QTY_SUM)) {
//            int qtySumIdx = fieldMap.get(QTY_SUM);
//            String qty = Float.toString(report.getQty());
//            ExcelUtils.setValue(sheet, qty, rowIdx, qtySumIdx);
//        }
//
//        if (fieldMap.containsKey(VOL)) {
//            int volIdx = fieldMap.get(VOL);
//            String vol = Float.toString(report.getVol());
//            ExcelUtils.setValue(sheet, vol, rowIdx, volIdx);
//        }
//
//        if (fieldMap.containsKey(SHARE_MOBILE)) {
//            int mobIdx = fieldMap.get(SHARE_MOBILE);
//            String mobile = Float.toString(report.getShareMobile());
//            ExcelUtils.setValue(sheet, mobile, rowIdx, mobIdx);
//        }
//
//        if (fieldMap.containsKey(CUSTOMER_ROYALTY)) {
//            int royalIdx = fieldMap.get(CUSTOMER_ROYALTY);
//            String royal = Float.toString(report.getCustomerRoyalty());
//            ExcelUtils.setValue(sheet, royal, rowIdx, royalIdx);
//        }
//
//        if (fieldMap.containsKey(CATALOG_ROYALTY)) {
//            int catIdx = fieldMap.get(CATALOG_ROYALTY);
//            String catRoy = Float.toString(report.getCatalogRoyalty());
//            ExcelUtils.setValue(sheet, catRoy, rowIdx, catIdx);
//        }
//
//
//        if (fieldMap.containsKey(REVENUE)) {
//            int revenIdx = fieldMap.get(REVENUE);
//            String revenue = Float.toString(report.getRevenue());
//            ExcelUtils.setValue(sheet, revenue, rowIdx, revenIdx);
//        }
//
//        if (fieldMap.containsKey(CATALOG)) {
//            int catalogIdx = fieldMap.get(CATALOG);
//            String cat = report.getCatalog();
//            ExcelUtils.setValue(sheet, cat, rowIdx, catalogIdx);
//        }
//
//        if (fieldMap.containsKey(COPYRIGHT)) {
//            int copyIdx = fieldMap.get(COPYRIGHT);
//            String copy = report.getCopyright();
//            ExcelUtils.setValue(sheet, copy, rowIdx, copyIdx);
//        }
    }


    private static List<Method> getReportFields(Class cls) {
        Method[] metMass = cls.getMethods();
        List<Method> methodList = new ArrayList<Method>();

        for (Method m : metMass) {
            if (m.getName().startsWith("get")) {
                methodList.add(m);
            }
        }
        return methodList;
    }

    private static Map<String, Integer> getFields(Row row) {
        if (row == null) return null;

        Map<String, Integer> columnMap = new HashMap<String, Integer>();

        for (String field : fieldList) {

            for (int c = 0; c < 100; c++) {
                String val = ExcelUtils.getCellVal(row, c);
                String result = getIfEquals(field, val);

                if (result != null) {
                    ExcelUtils.clearCell(row, c);
                    columnMap.put(result, c);
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

    public void addField(String field) {
        fieldList.add(field);
    }

    public List<String> getFields() {
        return fieldList;
    }

//    public static void main(String[] args) {
//    }

}













