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

    public List<Track> loadData(String filename, boolean commonRights)
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
            comp.setName(ExcelUtils.getCell(row, rowIdx++));
            if (commonRights) {
                comp.setArtist(ExcelUtils.getCell(row, rowIdx++));
                rowIdx++;
            } else {

                comp.setComposer(ExcelUtils.getCell(row, rowIdx++ ));
                rowIdx++;
                comp.setArtist(ExcelUtils.getCell(row, rowIdx++));
            }

            String mobileShare = ExcelUtils.getCell(row, rowIdx++);
            String publicShare = ExcelUtils.getCell(row, rowIdx);

            if (!isShareEmpty(mobileShare)) {
                comp.setMobileShare(Float.parseFloat(mobileShare.replace(",", ".")));
            }
            if (!isShareEmpty(publicShare)) {
                comp.setPublicShare(Float.parseFloat(publicShare.replace(",", ".")));
            }
            items.add(comp);
        }

        long endTime = System.currentTimeMillis();
        long proc = (endTime - startTime) / 1000;
        System.out.println("Got " + items.size() + " items in " + proc + " sec.");

        return items;
    }


    public List<Track> loadMGS(String filename)
            throws IOException, InvalidFormatException {

        File file = new File(filename);

        System.out.println("File size " + file.length());
        System.out.println("Loading " + file.getName() + "... ");

        long startTime = System.currentTimeMillis();

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
            comp.setName(ExcelUtils.getCell(row, rowIdx++));
            comp.setComposer(ExcelUtils.getCell(row, rowIdx++));
            comp.setCode(ExcelUtils.getCell(row, rowIdx++));

            String sh = ExcelUtils.getCell(row, rowIdx++);
            rowIdx++;
            rowIdx++;

            if (!isShareEmpty(sh)) {
                String share = sh.replace(",", ".");
                comp.setMobileShare(Float.parseFloat(share));
                comp.setPublicShare(Float.parseFloat(share));
            }
            comp.setArtist(ExcelUtils.getCell(row, rowIdx));
            items.add(comp);
        }

        long endTime = System.currentTimeMillis();
        long proc = (endTime - startTime) / 1000;
        System.out.println("Got " + items.size() + " items in " + proc + " sec.");
        return items;
    }

    private boolean isShareEmpty(String money) {
        return "".equals(money) || money == null || " ".equals(money);
    }

    public List<Track> loadSonyMusic(String filename)
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
            comp.setName(ExcelUtils.getCell(row, rowIdx++));
            comp.setCode(ExcelUtils.getCell(row, rowIdx++));
            comp.setComposer(ExcelUtils.getCell(row, rowIdx++));
            comp.setArtist(ExcelUtils.getCell(row, rowIdx++));

            String money = ExcelUtils.getCell(row, rowIdx);

            if (!isShareEmpty(money)) {
                comp.setMobileShare(Float.parseFloat(money.replace(",", ".")));
                comp.setPublicShare(Float.parseFloat(money.replace(",", ".")));
            }

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
