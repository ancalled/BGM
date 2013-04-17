package kz.bgm.platform.parsers;


import kz.bgm.platform.items.CalculatedReportItem;
import kz.bgm.platform.parsers.utils.ExcelUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlankReportParser {

    private static final Logger log = Logger.getLogger(BlankReportParser.class);

    private static final String COMPOSITION_CODE = "{code}";
    private static final String COMPOSITION_NAME = "{composition}";
    private static final String ARTIST = "{artist}";
    private static final String COMPOSER = "{composer}";
    private static final String CONTENT_TYPE = "{cont_type}";
    private static final String PRICE = "{prise}";
    private static final String QTY_SUM = "{qty_sum}";
    private static final String VOL = "{vol}";
    private static final String SHARE_MOBILE = "{share_mob}";
    private static final String CUSTOMER_ROYALTY = "{cust_royal}";
    private static final String CATALOG_ROYALTY = "{cat_royal]";
    private static final String REVENUE = "{revenue}";
    private static final String CATALOG = "{cat}";
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


    public static void createCalcReportExcel(String filePath,
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

            fillExcelBlank(sheet, startRow, fieldsMap, finishReps);

            ExcelUtils.saveFile(wb, reportBlank.getName());

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
            fillValues(report, sheet, fields, rowIdx);
            rowIdx++;
        }
    }


    private static void fillValues(CalculatedReportItem report, Sheet sheet,
                                   Map<String, Integer> fieldMap, int rowIdx) {

        if (fieldMap.containsKey(COMPOSITION_CODE)) {
            int codeIdx = fieldMap.get(COMPOSITION_CODE);
            String code = report.getCompositionCode();
            ExcelUtils.setValue(sheet, code, rowIdx, codeIdx);
        }
        if (fieldMap.containsKey(COMPOSITION_NAME)) {
            int nameIdx = fieldMap.get(COMPOSITION_NAME);
            String name = report.getCompositionName();
            ExcelUtils.setValue(sheet, name, rowIdx, nameIdx);
        }
        if (fieldMap.containsKey(ARTIST)) {
            int artistIdx = fieldMap.get(ARTIST);
            String artist = report.getArtist();
            ExcelUtils.setValue(sheet, artist, rowIdx, artistIdx);
        }
        if (fieldMap.containsKey(COMPOSER)) {
            int composerIdx = fieldMap.get(COMPOSER);
            String composer = report.getComposer();
            ExcelUtils.setValue(sheet, composer, rowIdx, composerIdx);
        }

        if (fieldMap.containsKey(CONTENT_TYPE)) {
            int contentIdx = fieldMap.get(CONTENT_TYPE);
            String content = report.getContentType();
            ExcelUtils.setValue(sheet, content, rowIdx, contentIdx);
        }

        if (fieldMap.containsKey(PRICE)) {
            int priceIdx = fieldMap.get(PRICE);
            String price = Float.toString(report.getPrice());
            ExcelUtils.setValue(sheet, price, rowIdx, priceIdx);
        }

        if (fieldMap.containsKey(QTY_SUM)) {
            int qtySumIdx = fieldMap.get(QTY_SUM);
            String qty = Float.toString(report.getQtySum());
            ExcelUtils.setValue(sheet, qty, rowIdx, qtySumIdx);
        }

        if (fieldMap.containsKey(VOL)) {
            int volIdx = fieldMap.get(VOL);
            String vol = Float.toString(report.getVol());
            ExcelUtils.setValue(sheet, vol, rowIdx, volIdx);
        }

        if (fieldMap.containsKey(SHARE_MOBILE)) {
            int mobIdx = fieldMap.get(SHARE_MOBILE);
            String mobile = Float.toString(report.getShareMobile());
            ExcelUtils.setValue(sheet, mobile, rowIdx, mobIdx);
        }

        if (fieldMap.containsKey(CUSTOMER_ROYALTY)) {
            int royalIdx = fieldMap.get(CUSTOMER_ROYALTY);
            String royal = Float.toString(report.getCustomerRoyalty());
            ExcelUtils.setValue(sheet, royal, rowIdx, royalIdx);
        }

        if (fieldMap.containsKey(CATALOG_ROYALTY)) {
            int catIdx = fieldMap.get(CATALOG_ROYALTY);
            String catRoy = Float.toString(report.getCatalogRoyalty());
            ExcelUtils.setValue(sheet, catRoy, rowIdx, catIdx);
        }


        if (fieldMap.containsKey(REVENUE)) {
            int revenIdx = fieldMap.get(REVENUE);
            String revenue = Float.toString(report.getRevenue());
            ExcelUtils.setValue(sheet, revenue, rowIdx, revenIdx);
        }

        if (fieldMap.containsKey(CATALOG)) {
            int catalogIdx = fieldMap.get(CATALOG);
            String cat = report.getCatalog();
            ExcelUtils.setValue(sheet, cat, rowIdx, catalogIdx);
        }

        if (fieldMap.containsKey(COPYRIGHT)) {
            int copyIdx = fieldMap.get(COPYRIGHT);
            String copy = report.getCopyright();
            ExcelUtils.setValue(sheet, copy, rowIdx, copyIdx);
        }
    }

    private static Map<String, Integer> getFields(Row row) {
        if (row == null) return null;

        Map<String, Integer> columnMap = new HashMap<String, Integer>();

        for (String field : fieldList) {

            for (int c = 0; c < 100; c++) {
                String val = ExcelUtils.getCellVal(row, c);
                String result = getIfEquals(field, val);

                if (result != null) {
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













