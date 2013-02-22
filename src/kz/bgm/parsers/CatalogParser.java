package kz.bgm.parsers;

import kz.bgm.items.Track;
import kz.bgm.utils.ExcelUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CatalogParser {

    public List<Track> loadData(String filename, boolean commonRights, float royalty, String catalog)
            throws IOException, InvalidFormatException {

        File file = new File(filename);
        long startTime = System.currentTimeMillis();
        System.out.print("Loading " + file.getName() + "... ");

        Workbook wb = ExcelUtils.openFile(file);

        int sheets = wb.getNumberOfSheets();
//        System.out.println("sheets = " + sheets);

        Sheet sheet = wb.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        List<Track> items = new ArrayList<Track>();

        int startRow = 4;
        for (int i = startRow; i < rows; i++) {
            Row row = sheet.getRow(i);

            if (isEmpty(row)) continue;

            Track comp = new Track();

            int rowIdx = 1;
            comp.setCode(ExcelUtils.getCell(row, rowIdx++));
            comp.setComposition(ExcelUtils.getCell(row, rowIdx++));
            if (commonRights) {
                comp.setArtist(ExcelUtils.getCell(row, rowIdx++));
                rowIdx++;
            } else {
                comp.setMusicAuthors(ExcelUtils.getCell(row, rowIdx++));
                comp.setLyricsAuthors(ExcelUtils.getCell(row, rowIdx++));
                comp.setArtist(ExcelUtils.getCell(row, rowIdx++));
            }

            comp.setMobileRate(Float.parseFloat(ExcelUtils.getCell(row, rowIdx++).replace(",", ".")));
            comp.setPublicRate(Float.parseFloat(ExcelUtils.getCell(row, rowIdx++).replace(",", ".")));

            comp.setRoyalty(royalty);
            comp.setCatalog(catalog);

            items.add(comp);
        }

        long endTime = System.currentTimeMillis();
        long proc = (endTime - startTime) / 1000;
        System.out.println("Got " + items.size() + " items in " + proc + " sec.");

        return items;
    }

    private boolean isEmpty(Row row) {
        String number = ExcelUtils.getCell(row, 0);
        if (number == null || "".equals(number.trim())) return true;
        return false;
    }


}
