package kz.bgm.platform.utils;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

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
        try (FileInputStream fis = new FileInputStream(file)) {
            return WorkbookFactory.create(fis);
        }
    }


    public static boolean saveFile(Workbook wb, String filePath) throws IOException {
        if (wb == null || filePath == null) return false;
        FileOutputStream fout = new FileOutputStream(filePath);
        wb.write(fout);

        fout.close();
        return true;
    }

    public static void shiftRowsDown(Sheet sheet, int rowIdx) {
        sheet.shiftRows(rowIdx, sheet.getLastRowNum(), 1);
    }

    public static void clearCell(Row row, int cellIdx) {
        Cell cell = row.getCell(cellIdx);
        cell.setCellValue("");
    }

    public static void setValue(Sheet sheet, Object val, Type type, int rowIdx, int columnIdx) {
        if (sheet == null) return;
        Row row = sheet.getRow(rowIdx);


        Cell cell;
        if (row == null) {
            sheet.createRow(rowIdx);
            row = sheet.createRow(rowIdx);
            row.createCell(columnIdx);
            cell = row.getCell(columnIdx);
        } else {
            cell = row.getCell(columnIdx);
            if (cell == null) {
                row.createCell(columnIdx);
                cell = row.getCell(columnIdx);
            }
        }


        Workbook wb = sheet.getWorkbook();

        if (type == String.class) {
            cell.setCellType(Cell.CELL_TYPE_STRING);

            cell.setCellValue((String) val);
        } else if (type == Float.class || type == float.class) {
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            CellStyle cs = wb.createCellStyle();
            cs.setDataFormat(wb.createDataFormat().getFormat("##0.00"));
            cell.setCellStyle(cs);
            cell.setCellValue((Float) val);
        } else if (type == Integer.class || type == int.class) {
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue((Integer) val);
        } else if (type == Double.class || type == double.class) {
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            CellStyle cs = wb.createCellStyle();
            cs.setDataFormat(wb.createDataFormat().getFormat("##0.00"));
            cell.setCellStyle(cs);
            cell.setCellValue((Double) val);
        } else if (type == Boolean.class || type == boolean.class) {
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue((Boolean) val);
        }

    }
}
