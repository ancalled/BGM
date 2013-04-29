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
    private static final String SHARE_PUBLIC = "{sharePublic}";

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
        fieldList.add(SHARE_PUBLIC);
    }


    public static void buildReportExcelFile(String templFilePath,
                                            List<CalculatedReportItem> finishReps) {
        try {
            log.info("Making Excel file report from file: " + templFilePath);

            File reportBlank = new File(templFilePath);
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
        } catch (IOException | InvalidFormatException e) {
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

    public void addField(String field) {
        fieldList.add(field);
    }

    public List<String> getFields() {
        return fieldList;
    }

//    public static void main(String[] args) {
//    }

}













