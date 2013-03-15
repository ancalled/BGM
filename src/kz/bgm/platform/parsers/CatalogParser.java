package kz.bgm.platform.parsers;

import kz.bgm.platform.items.Track;
import kz.bgm.platform.parsers.utils.ExcelUtils;
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

        System.out.println("File size " + file.length());

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


    public List<Track> loadAllMCSandMGS(String filename)
            throws IOException, InvalidFormatException {

        File file = new File(filename);

        System.out.println("File size " + file.length());

        System.out.print("Loading " + file.getName() + "... ");

        Workbook wb = ExcelUtils.openFile(file);

        Sheet sheet = wb.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        List<Track> items = new ArrayList<Track>();

        int startRow = 1;
        int size = 100000;
        for (int i = startRow; i < rows; i++) {
            Row row = sheet.getRow(i);

            if (isEmpty(row)) continue;
            Track comp = new Track();

            int rowIdx = 0;
            comp.setComposition(ExcelUtils.getCell(row, rowIdx++));
            comp.setAuthors(ExcelUtils.getCell(row, rowIdx++));
            comp.setCode(ExcelUtils.getCell(row, rowIdx++));

            String money1 = ExcelUtils.getCell(row, rowIdx++).replace(",", ".");
            String money2 = ExcelUtils.getCell(row, rowIdx++).replace(",", ".");
            money1 = moneyCorrector(money1);
            money2 = moneyCorrector(money2);

            comp.setControlled_metch(Float.parseFloat(money1));
            comp.setCollect_metch(Float.parseFloat(money2));
            comp.setPublisher(ExcelUtils.getCell(row, rowIdx++));
            comp.setArtist(ExcelUtils.getCell(row, rowIdx));
            items.add(comp);
        }

        long endTime = System.currentTimeMillis();
        return items;
    }

    private String moneyCorrector(String money) {
        if ("".equals(money) || money == null) {
            return "0";
        } else {
            return money;
        }
    }

    public List<Track> loadAllMusic(String filename)
            throws IOException, InvalidFormatException {

        File file = new File(filename);

        System.out.println("File size " + file.length());

        long startTime = System.currentTimeMillis();

        System.out.print("Loading " + file.getName() + "... ");

        Workbook wb = ExcelUtils.openFile(file);

        Sheet sheet = wb.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        List<Track> items = new ArrayList<Track>();

        int startRow = 1;

        for (int i = startRow; i < rows; i++) {
            Row row = sheet.getRow(i);

            if (isEmpty(row)) continue;
            Track comp = new Track();

            int rowIdx = 0;
            comp.setComposition(ExcelUtils.getCell(row, rowIdx++));
            comp.setCode(ExcelUtils.getCell(row, rowIdx++));
            comp.setAuthors(ExcelUtils.getCell(row, rowIdx++));
            comp.setArtist(ExcelUtils.getCell(row, rowIdx++));

            String money = ExcelUtils.getCell(row, rowIdx++);
            money = moneyCorrector(money);

            comp.setCollect_metch(Float.parseFloat(money.replace(",", ".")));
            comp.setComment(ExcelUtils.getCell(row, rowIdx));

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
