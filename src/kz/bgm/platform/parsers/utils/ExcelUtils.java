package kz.bgm.platform.parsers.utils;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtils {


    private static final Logger log = Logger.getLogger(ExcelUtils.class);

    public static String getCellVal(Row row, int idx) {
        Cell cell;
        try {
            cell = row.getCell(idx);
        } catch (NullPointerException e) {
            return null;
        }

        return FORMATTER.formatCellValue(cell);
    }

    public static final DataFormatter FORMATTER = new DataFormatter(true);

    public static Workbook openFile(File file) throws IOException, InvalidFormatException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return WorkbookFactory.create(fis);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    public static boolean saveFile(Workbook wb, String filePath) throws IOException {
        if (wb == null || filePath == null) return false;
        FileOutputStream fout = new FileOutputStream(filePath + "_done");

        wb.write(fout);
        fout.close();
        return true;
    }

    public static void setValue(Sheet sheet, String val, int rowIdx, int columnIdx) {
        Row row = sheet.getRow(rowIdx);
        if (row == null) {
            return;
        }
        Cell cell = row.getCell(columnIdx);
        cell.setCellValue(val);
    }
}        //todo fnish if row or cell == null
