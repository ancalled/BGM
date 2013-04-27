package kz.bgm.platform.model.service;

import kz.bgm.platform.model.domain.*;

import java.util.List;

public interface CatalogStorage {

    void storeInCatalog(List<Track> trackList, String catalog);

    void addItem(Track track, boolean common);

    Track search(String author, String song);

    Track search(String author, String song, boolean common);

    Float getRoyalty(int catalogId);

    List<Track> getAllTracks();

    List<Track> searchBySongName(String songName);

    List<Track> searchByCode(String code);

    List<Track> searchByComposer(String composer);

    List<Track> searchByArtistLike(String artist);

    List<Track> searchByArtist(String artist);

    Customer getCustomer(String name);

    Customer getCustomer(int id);

    List<CalculatedReportItem> getCalculatedReports(String catalog);

    List<Track> search(String value);


    void saveCustomerReportItems(List<CustomerReportItem> reportItemList);

    int saveCustomerReport(CustomerReport report);

    List<Track> search(String author, boolean b);

    User getUser(String name, String pass);

}
