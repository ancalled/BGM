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

    User getUser(String name);

    AdminUser getAdmin(String name, String pass);

    List<User>getUsersByCustomerId(long customerId);

    long saveCustomerReport(CustomerReport report);

    void saveCustomerReportItems(List<CustomerReportItem> reportItemList);

    CustomerReport getCustomerReport(long id);

    List<CustomerReport> getAllCustomerReports();

    List<CustomerReport> getCustomerReports(long customerId, Date from, Date to);

    List<CustomerReportItem> getCustomerReportsItems(long reportId);

    List<CalculatedReportItem> calculatePublicReport(String catalog);

    List<CalculatedReportItem> calculateMobileReport(String catalog);

    CalculatedReportItem calculateMReportAuthor(CustomerReportItem reportItems);

    long createUser(User user);

    long createCustomer(Customer customer);

    void removeUser(long id);

    void removeCustomer(long id);

    CalculatedReportItem calculateMReportRelated(CustomerReportItem reportItems);

    int getCompositionCount(long catalogId);
}
