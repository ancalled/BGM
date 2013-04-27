package kz.bgm.platform.model.service;

import kz.bgm.platform.model.domain.*;

import java.util.List;

public interface CatalogStorage {

    void saveTracks(List<Track> trackList, String catalog);


    Track getTrack(long id);

    List<Track> getTracks(List<Long> ids);


    List<Track> getAllTracks();

    List<Track> searchTracks(String value);

    List<Track> searchTracksByName(String songName);

    List<Track> searchTracksByCode(String code);

    List<Track> searchTracksByComposer(String composer);

    List<Track> searchTracksByArtist(String artist);

    List<Track> searchTrackByArtistLike(String artist);


    Customer getCustomer(String name);

    Customer getCustomer(long id);

    List<CalculatedReportItem> calculatePlatformReport(String catalog);



    void saveCustomerReportItems(List<CustomerReportItem> reportItemList);

    int saveCustomerReport(CustomerReport report);


    User getUser(String name, String pass);

}
