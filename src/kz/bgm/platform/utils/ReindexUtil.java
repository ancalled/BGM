package kz.bgm.platform.utils;


import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.model.service.LuceneSearch;

import java.io.IOException;
import java.util.List;

public class ReindexUtil {


    private final CatalogStorage catalogStorage;
    private final LuceneSearch luceneSearch;


    public ReindexUtil() {
        catalogStorage = CatalogFactory.getStorage();
        luceneSearch = LuceneSearch.getInstance();
    }

    public void reindex() throws IOException {
        List<Track> tracks = catalogStorage.getAllTracks();
        luceneSearch.index(tracks);

    }


    public static void main(String[] args) throws IOException {
        new ReindexUtil().reindex();
    }
}
