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

    public static final String COMP_REPS_PATH = System.getProperty("user.dir") + "/computed-reports";

    public static boolean saveFile(Workbook wb, String fileName) throws IOException {
        if (wb == null || fileName == null) return false;
        FileOutputStream fout = new FileOutputStream(COMP_REPS_PATH + "/" + fileName);
        wb.write(fout);

        fout.close();
        return true;
    }

    public static void setValue(Sheet sheet, String val, int rowIdx, int columnIdx) {
        if (sheet == null) return;

        int cellType= 1;

        try{
            double d = Double.parseDouble(val);
            cellType=Cell.CELL_TYPE_NUMERIC;
        }catch (NumberFormatException ne){
            cellType =Cell.CELL_TYPE_STRING;

        }

        Row row = sheet.getRow(rowIdx);
        Cell cell;
        if (row == null) {
            sheet.createRow(rowIdx);
            row = sheet.createRow(rowIdx);
            row.createCell(columnIdx);
            cell = row.getCell(columnIdx);
            cell.setCellType(cellType);
        } else {
            cell = row.getCell(columnIdx);

            if (cell == null) {
                row.createCell(columnIdx);
                cell = row.getCell(columnIdx);
                cell.setCellType(cellType);
            }
        }
        cell.setCellValue(val);
    }
}
