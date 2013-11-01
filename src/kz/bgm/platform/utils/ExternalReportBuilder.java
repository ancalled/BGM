package kz.bgm.platform.utils;


import kz.bgm.platform.model.domain.CalculatedReportItem;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static kz.bgm.platform.BgmServer.*;

public class ExternalReportBuilder {

    private static final Logger log = Logger.getLogger(ExternalReportBuilder.class);





    public static void initDatabase(String propsFile) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(propsFile));

        String dbHost = props.getProperty(BASE_HOST);
        String dbPort = props.getProperty(BASE_PORT);
        String dbName = props.getProperty(BASE_NAME);
        String dbLogin = props.getProperty(BASE_LOGIN);
        String dbPass = props.getProperty(BASE_PASS);

        System.out.println("Initializing data storage...");
        CatalogFactory.initDBStorage(dbHost, dbPort, dbName, dbLogin, dbPass);
    }

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");


    public static void main(String[] args) throws ParseException {

        if (args.length < 1) {
            System.err.println("Not enough params!");
            System.out.println("Expected: $TYPE $PLATFORM $TEMPLATE $OUTPUT $FROM_DATE $TO_DATE");
            return;
        }

        String filename = args[0];

        try {
            initDatabase("db.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CatalogStorage storage = CatalogFactory.getStorage();




    }

}













