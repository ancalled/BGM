package kz.bgm.platform.service;

import kz.bgm.platform.items.Customer;
import kz.bgm.platform.items.CustomerReport;
import kz.bgm.platform.items.CustomerReportItem;
import kz.bgm.platform.items.Track;

import java.util.List;

public interface CatalogStorage {

    void storeInCatalog(List<Track> trackList, String catalog);

    void addItem(Track track, boolean common);

    Track search(String author, String song);

    Track search(String author, String song, boolean common);

    Float getRoyalty(int catalogId);

    List<Track> searchBySongName(String songName);

    List<Track> searchByCode(String code);

    List<Track> searchByComposer(String composer);

    List<Track> searchByArtistLike(String artist);

    List<Track> searchByArtist(String artist);

    Customer getCustomer(String name);

    Customer getCustomer(int id);

    List<Track> search(String value);

    void insertCustomerReportItem(List<CustomerReportItem> reportItemList);

    int insertCustomerReport(CustomerReport report);


    List<Track> search(String author, boolean b);

}
