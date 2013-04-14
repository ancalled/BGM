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


    public static CalculatedReportItem parseBlankReport(String filePath) {
        CalculatedReportItem report = null;
        try {
            File reportBlank = new File(filePath);
            Workbook wb = ExcelUtils.openFile(reportBlank);

            Sheet sheet = wb.getSheetAt(1);

            log.info("Parsing sheet '" + sheet.getSheetName() + "'");

            Map<String, Integer> fieldsMap = new HashMap<String, Integer>();

            for (int i = 0; i < 50; i++) {
                Row row = sheet.getRow(i);
                fieldsMap = getFields(row);

                if (fieldsMap != null) {
                    break;
                }
            }
            System.out.println(fieldsMap);
//todo finish this job make the method complete

        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } catch (InvalidFormatException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return report;
    }

    private static Map<String, Integer> getFields(Row row) {
        if (row == null) return null;

        Map<String, Integer> columnMap = new HashMap<String, Integer>();

        for (String field : fieldList) {

            for (int c = 0; c < 100; c++) {
                String val = ExcelUtils.getCell(row, c);
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

    public static void main(String[] args) {
        BlankReportParser.parseBlankReport("./reports/Sony.xlsx");
    }

}













