package kz.bgm.platform.parsers.utils;

import kz.bgm.platform.service.CatalogStorage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

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

        return CatalogStorage.FORMATTER.formatCellValue(cell);
    }




    public static Workbook openFile(File file) throws IOException, InvalidFormatException {
        FileInputStream fis = null;
        try {

            fis = new FileInputStream(file);

            // Open the workbook and then create the FormulaEvaluator and
            // DataFormatter instances that will be needed to, respectively,
            // force evaluation of forumlae found in cells and create a
            // formatted String encapsulating the cells contents.
            Workbook workbook = WorkbookFactory.create(fis);
            //evaluator = this.workbook.getCreationHelper().createFormulaEvaluator();
            //formatter = new DataFormatter(true);
            return workbook;
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
}
