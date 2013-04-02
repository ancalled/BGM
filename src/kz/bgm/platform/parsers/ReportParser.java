package kz.bgm.platform.parsers;

import kz.bgm.platform.items.ReportItem;
import kz.bgm.platform.parsers.utils.ExcelUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportParser {
   private static final Logger log= Logger.getLogger(ReportParser.class);
    
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
            String num = ExcelUtils.getCell(row, 0);

            if (num == null || "".equals(num.trim())) continue;

            ReportItem item = new ReportItem();
            item.setCompisition(ExcelUtils.getCell(row, 2));
            item.setArtist(ExcelUtils.getCell(row, 3));
            item.setContentType(ExcelUtils.getCell(row, 4));

            String priceStr = ExcelUtils.getCell(row, 5);

            if (priceStr == null || "".equals(priceStr.trim())) continue;

            priceStr =priceStr.replace(",",".");

            item.setPrice(Float.parseFloat(priceStr));

            String qtyStr = ExcelUtils.getCell(row, 6).trim();

            if(qtyStr==null||"".equals(qtyStr))continue;

            item.setQty(Integer.parseInt(qtyStr));
            item.setRate(clientRate);
            items.add(item);
        }

        long endTime = System.currentTimeMillis();
        long proc = (endTime - startTime) / 1000;
        log.info("Got " + items.size() + " items in " + proc + " sec.");

        return items;
    }



}
