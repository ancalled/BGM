package kz.bgm.platform.parsers.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtils {

    public static String getCell(Row row, int idx) {
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
}
