package kz.bgm.platform.utils;

import kz.bgm.platform.model.domain.CustomerReportItem;
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

    private static final Logger log = Logger.getLogger(ReportParser.class);


    public static List<CustomerReportItem> parseMobileReport(String fileName, long reportId)
            throws IOException, InvalidFormatException {

        log.info("Parsing mobile report from: " + fileName + "... ");

        Workbook wb = ExcelUtils.openFile(new File(fileName));

        List<CustomerReportItem> items = new ArrayList<CustomerReportItem>();

        Sheet sheet = wb.getSheetAt(1);
        int rows = sheet.getPhysicalNumberOfRows();

        //todo use templates instead of hardcoded cell numbers
        int startRow = 7;
        for (int i = startRow; i < rows; i++) {
            Row row = sheet.getRow(i);
            String num = ExcelUtils.getCellVal(row, 0);

            if (num == null || "".equals(num.trim())) continue;

            CustomerReportItem item = new CustomerReportItem();

            String name = ExcelUtils.getCellVal(row, 2);
            String artist = ExcelUtils.getCellVal(row, 3);

            item.setName(name);
            item.setArtist(artist);
            item.setContentType(ExcelUtils.getCellVal(row, 4));

            String priceStr = ExcelUtils.getCellVal(row, 8);

            if (priceStr == null || "".equals(priceStr.trim())) continue;

            item.setPrice(Integer.parseInt(priceStr.trim()));
            item.setQty(Integer.parseInt(ExcelUtils.getCellVal(row, 5).trim()));
            item.setReportId(reportId);
            items.add(item);
        }

        return items;
    }




    public static List<CustomerReportItem> parsePublicReport(String fileName, long reportId)
            throws IOException, InvalidFormatException {

        log.info("Parsing public report from: " + fileName + "... ");

        Workbook wb = ExcelUtils.openFile(new File(fileName));

        List<CustomerReportItem> items = new ArrayList<CustomerReportItem>();

        Sheet sheet = wb.getSheetAt(1);
        int rows = sheet.getPhysicalNumberOfRows();

        int startRow = 1;
        for (int i = startRow; i < rows; i++) {
            Row row = sheet.getRow(i);

            CustomerReportItem item = new CustomerReportItem();

            String artist = ExcelUtils.getCellVal(row, 1);
            String name = ExcelUtils.getCellVal(row, 2);
            String qrtStr = ExcelUtils.getCellVal(row, 3);
            int qty = Integer.parseInt(qrtStr.trim());

            item.setReportId(reportId);
            item.setArtist(artist);
            item.setName(name);
            item.setQty(qty);
            items.add(item);
        }

        return items;
    }





}
