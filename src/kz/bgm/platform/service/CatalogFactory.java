package kz.bgm.platform.service;

import kz.bgm.platform.items.Track;

import java.util.List;

public class CatalogFactory {


    private static CatalogStorage storage;

    public static void initDBStorage(String host, String port, String base, String user, String pass) {
        storage = new DbStorage(host, port, base, user, pass);
    }


    public static CatalogStorage getStorage() {
        return storage;
    }
}
