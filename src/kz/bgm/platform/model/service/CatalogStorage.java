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

    List<Platform> getOwnPlatforms();

    List<Long> getOwnCatalogIds();

    List<Long> getAllCatalogIds();



    List<Catalog> getCatalogsByPlatform(long catId);

    int getTrackCount();

    List<Track> getTracks(int from, int size);

    List<Track> getTracks(long catalogId, int from, int size);

    List<Track> getTempTracks(long catalogId, int from, int size);

    List<Track> getTracks(List<Long> ids);

    List<SearchResult> getTracks(List<SearchResult> ids, List<Long> catalogIds);

    List<Track> searchTracks(String field, String value, List<Long> catalogIds);

    List<Track> searchTracksByName(String songName);

    List<SearchResult> searchTracksByCode(String code, List<Long> catalogs);

    List<Track> searchTracksByComposer(String composer);

    List<Track> searchTracksByArtist(String artist);

    List<Track> searchTrackByArtistLike(String artist);

    List<Track> getRandomTracks(int num);

    List<Track> getRandomTracks(long catalogId, int num);

    List<Customer> getAllCustomers();

    Customer getCustomer(long id);

    User getUser(String name, String pass);

    User getUser(String name);

    AdminUser getAdmin(String name, String pass);

    List<User> getUsersByCustomerId(long customerId);

    long saveCustomerReport(CustomerReport report);

    long updtDetectedTracksInCustomerReport(long id, int detected);

    long updtTracksInCustomerReport(long id, int tracks);

    long saveCustomerReportItem(CustomerReportItem item);

    long getUpdateCatalogQueryId();

//    String getQueryProcessTime(long processId);

    void saveCustomerReportItems(List<CustomerReportItem> reportItemList);

    CustomerReport getCustomerReport(long id);

    List<CustomerReport> getAllCustomerReports();

    List<CustomerReport> getCustomerReports(long customerId, Date from, Date to);

    CustomerReportItem getCustomerReportsItem(long id);

    boolean acceptReport(long reportId);

    List<CustomerReportItem> getCustomerReportsItems(long reportId);

    List<CustomerReportItem> getCustomerReportsItems(long reportId, int from, int size);

    List<CalculatedReportItem> calculatePublicReport(String platform, Date from, Date to);

    List<CalculatedReportItem> calculateMobileReport(String platform, Date from, Date to);

    long createUser(User user);

    long createCustomer(Customer customer);

    void removeUser(long id);

    void removeCustomer(long id);


    CatalogUpdate saveCatalogUpdate(CatalogUpdate update);

    CatalogUpdate loadCatalogUpdateIntoTmpTable(CatalogUpdate update);

    public CatalogUpdate caclCatalogUpdateStats(long updateId, CatalogUpdate.Status st);



    List<TrackDiff> geChangedTracks(long updateId, int from, final int size);

    List<Track> getNewTracks(long updateId, int from, final int size);

    void applyCatalogUpdate(long updateId);

    List<Track> getUpdates(final long updateId);

    CatalogUpdate getCatalogUpdate(long updateId);

    List<CatalogUpdate> getCatalogUpdates(long catalogId);

    void updateCatalogsStat();

    List<Long> getCustomerBasket(long userId);

    void addItemToBasket(long customerId, long trackId);

    void removeItemFromBasket(long trackId, long userId);

    void removeItemFromReport(long itemId);

    List<Long> getAvailableCatalogs(long customerId);

    Integer updateTrack(Track track);

    void createCatalog(Catalog catalog);

    void createPlatform(Platform platform);

    void exportCatalogToCSV(long catalogId, String catalogName,
                            String fieldTerminator,
                            String enclosedBy, String linesTerminator);

}

