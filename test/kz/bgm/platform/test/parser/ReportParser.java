//package kz.bgm.parsers;
//
//import kz.bgm.domain.ReportItem;
//import kz.bgm.platform.model.domain.ReportItem;
//import kz.bgm.platform.utils.ExcelUtils;
//import kz.bgm.utils.ExcelUtils;
//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ReportParser {
//
//    public List<ReportItem> loadClientReport(String filename, float clientRate)
//            throws IOException, InvalidFormatException {
//
//        File file = new File(filename);
//        long startTime = System.currentTimeMillis();
//        System.out.println("Loading " + file.getName() + "... ");
//
//
//        Workbook wb = ExcelUtils.openFile(file);
//
//        List<ReportItem> domain = new ArrayList<ReportItem>();
//
//        Sheet sheet = wb.getSheetAt(1);
//        int rows = sheet.getPhysicalNumberOfRows();
//
//        System.out.println("Parsing sheet '" + sheet.getSheetName() + "' with " + rows + " rows");
//
//        int startRow = 7;
//        for (int i = startRow; i < rows; i++) {
//            Row row = sheet.getRow(i);
//            String num = ExcelUtils.getCellVal(row, 0);
//
//            if (num == null || "".equals(num.trim())) continue;
//
//            ReportItem item = new ReportItem();
//            item.setCompisition(ExcelUtils.getCellVal(row, 2));
//            item.setAuthor(ExcelUtils.getCellVal(row, 3));
//            item.setContentType(ExcelUtils.getCellVal(row, 4));
//
//            String priceStr = ExcelUtils.getCellVal(row, 5);
//
//            if (priceStr == null || "".equals(priceStr.trim())) continue;
//
//            item.setPrice(Integer.parseInt(priceStr.trim()));
//            item.setQtySum(Integer.parseInt(ExcelUtils.getCellVal(row, 6).trim()));
//            item.setRate(clientRate);
//            domain.add(item);
//        }
//
//        long endTime = System.currentTimeMillis();
//        long proc = (endTime - startTime) / 1000;
//        System.out.println("Got " + domain.size() + " domain in " + proc + " sec.");
//
//        return domain;
//    }
//
//
//
//}