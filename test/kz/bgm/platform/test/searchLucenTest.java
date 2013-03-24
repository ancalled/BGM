package kz.bgm.platform.test;

import kz.bgm.platform.items.Track;
import kz.bgm.platform.service.DbStorage;

public class searchLucenTest {

    private static String lBase = "bgm";
    private static String lLogin = "root";
    private static String lPass = "root";
    private static String lHost = "localhost";
    private static String lPort = "3306";


    public static void main(String[] args) {
        DbStorage db = new DbStorage(lHost, lPort, lBase, lLogin, lPass);

        String artist = "Uberzone";
        String compos = "2Kool4skool";

        Track t = db.search(artist, compos);

        System.out.println("Found----" + t);
    }

}
