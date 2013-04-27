package kz.bgm.platform.test;

import kz.bgm.platform.model.domain.CustomerReport;
import kz.bgm.platform.model.service.DbStorage;

import java.sql.Date;

public class TestDB {

    private static String lBase = "bgm";
    private static String lLogin = "root";
    private static String lPass = "root";
    private static String lHost = "localhost";
    private static String lPort = "3306";

    public static void main(String[] args) {

        DbStorage db = new DbStorage(lHost, lPort, lBase, lLogin, lPass);


        CustomerReport cs = new CustomerReport();
        cs.setCustomerId(3);
        cs.setUploadDate(new Date(65465456));
        cs.setStartDate(new Date(65465456));

        System.out.println(db.insertCustomerReport(cs));
    }
}
