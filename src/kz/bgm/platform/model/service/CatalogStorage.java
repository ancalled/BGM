package kz.bgm.platform.model.service;

import kz.bgm.platform.model.domain.*;

import java.util.Date;
import java.util.List;

public interface CatalogStorage {

    void saveTracks(List<Track> trackList, String catalog);

    Platform getPlatform(long id);

    Catalog getCatalog(long id);

    Track getTrack(long id);

    List<Platform> getAllPlatforms();

    List<Catalog> getAllCatalogs();

    List<Track> getAllTracks();


    List<Track> getTracks(List<Long> ids);

    List<Track> searchTracks(String value);

    List<Track> searchTracksByName(String songName);

    List<Track> searchTracksByCode(String code);

    List<Track> searchTracksByComposer(String composer);

    List<Track> searchTracksByArtist(String artist);

    List<Track> searchTrackByArtistLike(String artist);

    List<Customer> getCustomers();

    Customer getCustomer(String name);

    Customer getCustomer(long id);

    User getUser(String name, String pass);

    Admin getAdmin(String name, String pass);


    long saveCustomerReport(CustomerReport report);

    void saveCustomerReportItems(List<CustomerReportItem> reportItemList);


    CustomerReport getCustomerReport(long id);

    List<CustomerReport> getCustomerReports(long customerId, Date from, Date to);

    List<CustomerReportItem> getCustomerReportsItems(long reportId);


    List<CalculatedReportItem> calculatePlatformReport(String catalog);



}
