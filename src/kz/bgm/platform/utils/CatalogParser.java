package kz.bgm.platform.utils;

import kz.bgm.platform.model.domain.Track;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CatalogParser {

    private  static final Logger log =Logger.getLogger(CatalogParser.class); 
    
    
    public List<Track> loadData(String filename, boolean commonRights)
            throws IOException, InvalidFormatException {

        File file = new File(filename);

        log.info("File size " + file.length());

        long startTime = System.currentTimeMillis();
        log.info("Loading " + file.getName() + "... ");

        Workbook wb = ExcelUtils.openFile(file);

//        int sheets = wb.getNumberOfSheets();
//        log.info("sheets = " + sheets);

        Sheet sheet = wb.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        List<Track> items = new ArrayList<>();

        int startRow = 4;
        for (int i = startRow; i < rows; i++) {
            Row row = sheet.getRow(i);

            if (isEmpty(row)) continue;

            Track comp = new Track();

            int rowIdx = 1;
            comp.setCode(ExcelUtils.getCellVal(row, rowIdx++));
            comp.setName(ExcelUtils.getCellVal(row, rowIdx++));
            if (commonRights) {
                comp.setArtist(ExcelUtils.getCellVal(row, rowIdx++));
                rowIdx++;
            } else {

                comp.setComposer(ExcelUtils.getCellVal(row, rowIdx++));
                rowIdx++;
                comp.setArtist(ExcelUtils.getCellVal(row, rowIdx++));
            }

            String mobileShare = ExcelUtils.getCellVal(row, rowIdx++);
            String publicShare = ExcelUtils.getCellVal(row, rowIdx);

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
        log.info("Got " + items.size() + " items in " + proc + " sec.");

        return items;
    }


    public List<Track> loadMGS(String filename)
            throws IOException, InvalidFormatException {

        File file = new File(filename);

        log.info("File size " + file.length());
        log.info("Loading " + file.getName() + "... ");

        long startTime = System.currentTimeMillis();

        Workbook wb = ExcelUtils.openFile(file);

        Sheet sheet = wb.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        List<Track> items = new ArrayList<>();

        int startRow = 1;
//        int size = 100000;
        for (int i = startRow; i < rows; i++) {
            Row row = sheet.getRow(i);

            if (isEmpty(row)) continue;
            Track comp = new Track();

            int rowIdx = 0;
            comp.setName(ExcelUtils.getCellVal(row, rowIdx++));
            comp.setComposer(ExcelUtils.getCellVal(row, rowIdx++));
            comp.setCode(ExcelUtils.getCellVal(row, rowIdx++));

            String sh = ExcelUtils.getCellVal(row, rowIdx++);
            rowIdx++;
            rowIdx++;

            if (!isShareEmpty(sh)) {
                String share = sh.replace(",", ".");
                comp.setMobileShare(Float.parseFloat(share));
                comp.setPublicShare(Float.parseFloat(share));
            }
            comp.setArtist(ExcelUtils.getCellVal(row, rowIdx));
            items.add(comp);
        }

        long endTime = System.currentTimeMillis();
        long proc = (endTime - startTime) / 1000;
        log.info("Got " + items.size() + " items in " + proc + " sec.");
        return items;
    }

    private boolean isShareEmpty(String money) {
        return "".equals(money) || money == null || " ".equals(money);
    }

    public List<Track> loadSonyMusic(String filename)
            throws IOException, InvalidFormatException {

        File file = new File(filename);

        log.info("File size " + file.length());

        long startTime = System.currentTimeMillis();

        log.info("Loading " + file.getName() + "... ");

        Workbook wb = ExcelUtils.openFile(file);

        Sheet sheet = wb.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        List<Track> items = new ArrayList<>();

        int startRow = 1;

        for (int i = startRow; i < rows; i++) {
            Row row = sheet.getRow(i);

            if (isEmpty(row)) continue;
            Track comp = new Track();

            int rowIdx = 0;
            comp.setName(ExcelUtils.getCellVal(row, rowIdx++));
            comp.setCode(ExcelUtils.getCellVal(row, rowIdx++));
            comp.setComposer(ExcelUtils.getCellVal(row, rowIdx++));
            comp.setArtist(ExcelUtils.getCellVal(row, rowIdx++));

            String money = ExcelUtils.getCellVal(row, rowIdx);

            if (!isShareEmpty(money)) {
                comp.setMobileShare(Float.parseFloat(money.replace(",", ".")));
                comp.setPublicShare(Float.parseFloat(money.replace(",", ".")));
            }

            items.add(comp);
        }

        long endTime = System.currentTimeMillis();
        long proc = (endTime - startTime) / 1000;
        log.info("Got " + items.size() + " items in " + proc + " sec.");

        return items;
    }


    private boolean isEmpty(Row row) {
        String number = ExcelUtils.getCellVal(row, 0);
        return number == null || "".equals(number.trim());
    }


}
