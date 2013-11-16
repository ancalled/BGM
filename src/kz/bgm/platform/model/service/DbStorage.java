package kz.bgm.platform.model.service;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import kz.bgm.platform.model.domain.*;

import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import static kz.bgm.platform.model.domain.CatalogUpdate.Status;

public class DbStorage implements CatalogStorage {


    public static final int MAX_STATEMENTS = 200;
    public static final int MAX_STATEMENTS_PER_CONNECTION = 10;
    public static final int MIN_POOL_SIZE = 1;
    public static final int MAX_POOL_SIZE = 10;


    public static final AtomicBoolean catalogLoading = new AtomicBoolean(false);

    private final ComboPooledDataSource pool;

    private static Map<Long, String> catalogMap = new HashMap<>();


    public DbStorage(String host, String port,
                     String base, String user, String pass) {
        pool = initPool(host, port, base, user, pass);
        prepareCatalogsMap(catalogMap);
    }


    public boolean isCatalogLoading() {
        return catalogLoading.get();
    }

    public void saveTracks(final List<Track> tracks, final String catalog) {
        final int catId = getCatalogId(catalog);

        query(new Action<Boolean>() {
            @Override
            public Boolean execute(Connection con) throws SQLException {
                PreparedStatement ps =
                        con.prepareStatement("INSERT INTO " +
                                "composition(catalog_id, code, name, artist, " +
                                "composer,shareMobile,sharePublic) " +
                                "VALUES (?,?,?,?,?,?,?)");


                for (Track t : tracks) {

                    ps.setInt(1, catId);
                    ps.setString(2, t.getCode());
                    ps.setString(3, t.getName());
                    ps.setString(4, t.getArtist());
                    ps.setString(5, t.getComposer());
                    ps.setFloat(6, t.getMobileShare());
                    ps.setFloat(7, t.getPublicShare());

                    ps.addBatch();
                }

                con.setAutoCommit(false);
                ps.executeBatch();

                con.commit();

                return true;
            }
        });

    }


    private int getCatalogId(String catalogName) {
        Connection connection = null;
        try {
            connection = pool.getConnection();
            PreparedStatement ps =
                    connection.prepareStatement("SELECT id FROM catalog WHERE name=?",
                            ResultSet.TYPE_FORWARD_ONLY,
                            ResultSet.CONCUR_READ_ONLY);

            ps.setString(1, catalogName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }


    @Override
    public Platform getPlatform(final long id) {
        return query(new Action<Platform>() {
            @Override
            public Platform execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM platform WHERE id=?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setLong(1, id);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return parsePlatform(rs);
                }

                return null;
            }
        });
    }


    @Override
    public Catalog getCatalog(final long id) {
        return query(new Action<Catalog>() {
            @Override
            public Catalog execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM catalog WHERE id=?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setLong(1, id);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return parseCatalog(rs);
                }

                return null;
            }
        });
    }

    @Override
    public Track getTrack(final long id) {
        return query(new Action<Track>() {
            @Override
            public Track execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM composition WHERE id=?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setLong(1, id);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return parseTrack(rs);
                }

                return null;
            }
        });
    }

    @Override
    public List<Platform> getAllPlatforms() {
        return query(new Action<List<Platform>>() {
            @Override
            public List<Platform> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM platform",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);

                ResultSet rs = stmt.executeQuery();

                List<Platform> tracks = new ArrayList<>();
                while (rs.next()) {
                    Platform p = parsePlatform(rs);
                    tracks.add(p);

                    List<Catalog> catalogs = getCatalogsByPlatform(p.getId());
                    p.setCatalogs(catalogs);
                }

                return tracks;
            }
        });
    }


    @Override
    public List<Platform> getOwnPlatforms() {
        return query(new Action<List<Platform>>() {
            @Override
            public List<Platform> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM platform p WHERE p.rights = TRUE",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);

                ResultSet rs = stmt.executeQuery();

                List<Platform> tracks = new ArrayList<>();
                while (rs.next()) {
                    Platform p = parsePlatform(rs);
                    tracks.add(p);

                    List<Catalog> catalogs = getCatalogsByPlatform(p.getId());
                    p.setCatalogs(catalogs);
                }

                return tracks;
            }
        });
    }

    @Override
    public List<Long> getAllCatalogIds() {
        return query(new Action<List<Long>>() {
            @Override
            public List<Long> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement("SELECT id FROM catalog",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery();

                List<Long> catalogs = new ArrayList<>();
                while (rs.next()) {
                    catalogs.add(rs.getLong("id"));
                }
                return catalogs;
            }
        });
    }


    @Override
    public List<Long> getOwnCatalogIds() {
        return query(new Action<List<Long>>() {
            @Override
            public List<Long> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement("SELECT c.id FROM catalog c " +
                        "LEFT JOIN platform p " +
                        "ON (c.platform_id = p.id) " +
                        "WHERE p.rights = TRUE",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery();

                List<Long> catalogs = new ArrayList<>();
                while (rs.next()) {
                    catalogs.add(rs.getLong("id"));
                }
                return catalogs;
            }
        });
    }


    @Override
    public List<Catalog> getCatalogsByPlatform(final long platformId) {
        return query(new Action<List<Catalog>>() {
            @Override
            public List<Catalog> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM  catalog WHERE platform_id = ?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY
                );
                stmt.setLong(1, platformId);

                ResultSet rs = stmt.executeQuery();

                List<Catalog> catalogs = new ArrayList<>();
                while (rs.next()) {
                    Catalog cat = parseCatalog(rs);

                    catalogs.add(cat);
                }
                return catalogs;
            }
        });
    }


    @Override
    public List<SearchResult> getTracks(final List<SearchResult> found, final List<Long> catalogIds) {

        if (found == null || catalogIds == null || found.isEmpty() || catalogIds.isEmpty()) return null;

        return query(new Action<List<SearchResult>>() {
            @Override
            public List<SearchResult> execute(Connection con) throws SQLException {

                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM composition WHERE id IN (" + asStringBySearchResults(found) +
                                ") AND catalog_id IN (" + asString(catalogIds) + ")",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);

                ResultSet rs = stmt.executeQuery();

                List<SearchResult> result = new ArrayList<>();

                while (rs.next()) {
                    Track track = parseTrack(rs);
                    for (SearchResult sr : found) {
                        if (sr.getTrackId() == track.getId()) {
                            sr.setTrack(track);
                            result.add(sr);
                            break;
                        }
                    }
                }

                return result;
            }
        });
    }

    @Override
    public List<Track> getTracks(final List<Long> ids) {

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        return query(new Action<List<Track>>() {
            @Override
            public List<Track> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM composition WHERE id IN (" + DbStorage.asString(ids) + ")",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);

                ResultSet rs = stmt.executeQuery();

                List<Track> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(parseTrack(rs));
                }

                return result;
            }
        });

    }


    @Override
    public List<Track> searchTracks(final String field, final String value, final List<Long> catalogIds) {
        return query(new Action<List<Track>>() {

            @Override
            public List<Track> execute(Connection con) throws SQLException {

                String catalogsPart = "";
                if (catalogIds != null) {
                    for (Long cat : catalogIds) {
                        if (catalogIds.indexOf(cat) == 0) {
                            catalogsPart = catalogsPart.concat("AND (");
                        }
                        catalogsPart = catalogsPart.concat("catalog_id=" + cat);
                        if (catalogIds.indexOf(cat) != catalogIds.size() - 1) {
                            catalogsPart = catalogsPart.concat(" OR ");
                        } else {
                            catalogsPart = catalogsPart.concat(")");
                        }
                    }
                }
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM composition WHERE " +
                                field + "= ? " +
                                catalogsPart,
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                if ("code".equals(field)) {
                    try {
                        stmt.setLong(1, Long.parseLong(value));
                    } catch (NumberFormatException ne) {
                        System.out.println("not digits input");
                        return Collections.emptyList();
                    }
                } else {
                    stmt.setString(1, value);
                }

                stmt.setMaxRows(100);

                ResultSet rs = stmt.executeQuery();

                List<Track> tracks = new ArrayList<>();
                while (rs.next()) {
                    tracks.add(parseTrack(rs));
                }
                return tracks;
            }
        });
    }


    @Override
    public List<Track> searchTracksByName(final String name) {
        return query(new Action<List<Track>>() {
            @Override
            public List<Track> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM composition WHERE name=?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, name);

                ResultSet rs = stmt.executeQuery();

                List<Track> tracks = new ArrayList<>();
                while (rs.next()) {
                    tracks.add(parseTrack(rs));
                }
                return tracks;
            }
        });

    }


    @Override
    public List<SearchResult> searchTracksByCode(final String code, final List<Long> catalogs) {
        return query(new Action<List<SearchResult>>() {
            @Override
            public List<SearchResult> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM composition WHERE code=? AND catalog_id " +
                                "IN (" + asString(catalogs) + ")",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, code);

                ResultSet rs = stmt.executeQuery();

                List<SearchResult> tracks = new ArrayList<>();
                while (rs.next()) {
                    tracks.add(new SearchResult(parseTrack(rs)));
                }
                return tracks;
            }
        });

    }

    @Override
    public List<Track> searchTracksByComposer(final String composer) {
        return query(new Action<List<Track>>() {
            @Override
            public List<Track> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM composition WHERE composer=?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, composer);

                ResultSet rs = stmt.executeQuery();

                List<Track> tracks = new ArrayList<>();
                while (rs.next()) {
                    tracks.add(parseTrack(rs));
                }
                return tracks;
            }
        });

    }

    @Override
    public List<Track> searchTracksByArtist(final String artist) {
        return query(new Action<List<Track>>() {
            @Override
            public List<Track> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM composition WHERE artist=?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, artist);

                ResultSet rs = stmt.executeQuery();

                List<Track> tracks = new ArrayList<>();
                while (rs.next()) {
                    tracks.add(parseTrack(rs));
                }
                return tracks;
            }
        });
    }

    @Override
    public List<Track> searchTrackByArtistLike(final String artist) {
        return query(new Action<List<Track>>() {
            @Override
            public List<Track> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM composition WHERE artist LIKE?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, artist);

                ResultSet rs = stmt.executeQuery();

                List<Track> tracks = new ArrayList<>();
                while (rs.next()) {
                    tracks.add(parseTrack(rs));
                }
                return tracks;
            }
        });
    }


    @Override
    public List<Track> getRandomTracks(final int num) {
        return query(new Action<List<Track>>() {
            @Override
            public List<Track> execute(Connection con) throws SQLException {

                List<Track> tracks = new ArrayList<>();

                for (int i = 0; i < num; i++) {
                    PreparedStatement stmt = con.prepareStatement(
                            "SELECT * FROM composition AS c JOIN\n" +
                                    "  (SELECT (RAND() * (SELECT MAX(id)  FROM composition)) AS id) r\n" +
                                    "WHERE c.id >= r.id\n" +
                                    "ORDER BY c.id ASC\n" +
                                    "LIMIT 1;",
                            ResultSet.TYPE_FORWARD_ONLY,
                            ResultSet.CONCUR_READ_ONLY);

                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        tracks.add(parseTrack(rs));
                    }
                }
                return tracks;
            }
        });
    }

    @Override
    public List<Track> getRandomTracks(final long catalogId, final int num) {
        return query(new Action<List<Track>>() {
            @Override
            public List<Track> execute(Connection con) throws SQLException {

                List<Track> tracks = new ArrayList<>();

                for (int i = 0; i < num; i++) {
                    PreparedStatement stmt = con.prepareStatement(
                            "SELECT * FROM composition AS c JOIN\n" +
                                    "  (SELECT (RAND() * (SELECT MAX(id)  FROM composition)) AS id) r\n" +
                                    "WHERE c.id >= r.id\n" +
                                    "AND catalog_id = ?\n" +
                                    "ORDER BY c.id ASC\n" +
                                    "LIMIT 1;",
                            ResultSet.TYPE_FORWARD_ONLY,
                            ResultSet.CONCUR_READ_ONLY);
                    stmt.setLong(1, catalogId);

                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        tracks.add(parseTrack(rs));
                    }
                }
                return tracks;
            }
        });
    }

    @Override
    public List<Customer> getAllCustomers() {
        return query(new Action<List<Customer>>() {
            @Override
            public List<Customer> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM customer",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery();

                List<Customer> customers = new ArrayList<>();
                while (rs.next()) {
                    customers.add(parseCustomer(rs));
                }
                return customers;
            }
        });
    }


    @Override
    public Customer getCustomer(final long id) {
        return query(new Action<Customer>() {
            @Override
            public Customer execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM customer WHERE id=?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setLong(1, id);

                ResultSet rs = stmt.executeQuery();
                return rs.next() ? parseCustomer(rs) : null;
            }
        });
    }

    @Override
    public AdminUser getAdmin(final String name, final String pass) {
        return query(new Action<AdminUser>() {
            @Override
            public AdminUser execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM user_admin WHERE login = ? AND password = ?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, name);
                stmt.setString(2, pass);

                ResultSet rs = stmt.executeQuery();
                return rs.next() ? parseAdmin(rs) : null;
            }
        });
    }

    @Override
    public User getUser(final String name, final String pass) {
        return query(new Action<User>() {
            @Override
            public User execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM user WHERE login = ? AND password = ?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, name);
                stmt.setString(2, pass);

                ResultSet rs = stmt.executeQuery();
                return rs.next() ? parseUser(rs) : null;
            }
        });
    }

    @Override
    public User getUser(final String name) {
        return query(new Action<User>() {
            @Override
            public User execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM user WHERE login = ?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setString(1, name);

                ResultSet rs = stmt.executeQuery();
                return rs.next() ? parseUser(rs) : null;
            }
        });
    }


//    ----------------------------------------------------------------------------------------

    @Override
    public long saveCustomerReport(final CustomerReport report) {
        return query(new Action<Long>() {
            @Override
            public Long execute(Connection con) throws SQLException {
                PreparedStatement ps =
                        con.prepareStatement("INSERT INTO customer_report(customer_id, start_date, upload_date, type, " +
                                "period, tracks, detected, revenue, accepted) VALUES (?,?,?,?,?,?,?,?,?)",
                                Statement.RETURN_GENERATED_KEYS);

                ps.setLong(1, report.getCustomerId());
                ps.setDate(2, new java.sql.Date(report.getStartDate().getTime()));
                ps.setDate(3, new java.sql.Date(report.getUploadDate().getTime()));
                ps.setInt(4, report.getType().ordinal());
                ps.setInt(5, report.getPeriod().ordinal());
                ps.setInt(6, report.getTracks());
                ps.setInt(7, report.getDetected());
                ps.setLong(8, report.getRevenue());
                ps.setBoolean(9, report.isAccepted());

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                return rs.next() ? rs.getLong(1) : -1;
            }
        });
    }

    @Override
    public long updtTracksInCustomerReport(final long id, final int tracks) {
        return query(new Action<Long>() {
            @Override
            public Long execute(Connection con) throws SQLException {
                PreparedStatement ps =
                        con.prepareStatement("UPDATE customer_report SET " +
                                "tracks = ? " +
                                "WHERE id = ?",
                                Statement.RETURN_GENERATED_KEYS);

                ps.setInt(1, tracks);
                ps.setLong(2, id);

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                return rs.next() ? rs.getLong(1) : -1;
            }
        });
    }

    @Override
    public long updtDetectedTracksInCustomerReport(final long id, final int detected) {
        return query(new Action<Long>() {
            @Override
            public Long execute(Connection con) throws SQLException {
                PreparedStatement ps =
                        con.prepareStatement("UPDATE customer_report SET " +
                                "detected = ? " +
                                "WHERE id = ?",
                                Statement.RETURN_GENERATED_KEYS);

                ps.setInt(1, detected);
                ps.setLong(2, id);

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                return rs.next() ? rs.getLong(1) : -1;
            }
        });
    }

    @Override
    public long saveCustomerReportItem(final CustomerReportItem item) {
        return query(new Action<Long>() {
            @Override
            public Long execute(Connection con) throws SQLException {
                PreparedStatement ps =
                        con.prepareStatement("INSERT INTO " +
                                "customer_report_item(report_id,composition_id,name," +
                                "artist,content_type,qty,price, number) " +
                                "VALUES (?,?,?,?,?,?,?,?)",
                                Statement.RETURN_GENERATED_KEYS);

                ps.setLong(1, item.getReportId());
                if (item.getCompositionId() != null) {
                    ps.setLong(2, item.getCompositionId());
                } else {
                    ps.setNull(2, Types.INTEGER);
                }

                ps.setString(3, item.getTrack());
                ps.setString(4, item.getArtist());
                ps.setString(5, item.getContentType());
                ps.setInt(6, item.getQty());
                ps.setFloat(7, item.getPrice());
                ps.setInt(8, item.getNumber());

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                return rs.next() ? rs.getLong(1) : -1;
            }
        });
    }

    @Override
    public long getUpdateCatalogQueryId() {
        return 0;
    }


    @Override
    public void saveCustomerReportItems(final List<CustomerReportItem> items) {
        query(new Action<Boolean>() {
            @Override
            public Boolean execute(Connection con) throws SQLException {
                PreparedStatement ps =
                        con.prepareStatement("INSERT INTO " +
                                "customer_report_item(report_id,composition_id,name," +
                                "artist,content_type,qty,price,detected,number) " +
                                "VALUES (?,?,?,?,?,?,?,?,?)");

                for (CustomerReportItem cr : items) {
                    ps.setLong(1, cr.getReportId());
                    if (cr.getCompositionId() != null) {
                        ps.setLong(2, cr.getCompositionId());
                    } else {
                        ps.setNull(2, Types.INTEGER);
                    }

                    ps.setString(3, cr.getTrack());
                    ps.setString(4, cr.getArtist());
                    ps.setString(5, cr.getContentType());
                    ps.setInt(6, cr.getQty());
                    ps.setFloat(7, cr.getPrice());
                    ps.setBoolean(8, cr.isDetected());
                    ps.setInt(9, cr.getNumber());

                    ps.addBatch();
                }

                con.setAutoCommit(false);
                ps.executeBatch();
                con.commit();

                return true;
            }
        });
    }


    @Override
    public List<CustomerReport> getAllCustomerReports() {
        return query(new Action<List<CustomerReport>>() {
            @Override
            public List<CustomerReport> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM customer_report ORDER BY start_date",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);

                ResultSet rs = stmt.executeQuery();

                List<CustomerReport> reportList = new ArrayList<>();

                while (rs.next()) {
                    reportList.add(parseCustomerReport(rs));
                }

                return reportList;
            }
        });
    }


    @Override
    public CustomerReport getCustomerReport(final long id) {
        return query(new Action<CustomerReport>() {
            @Override
            public CustomerReport execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM customer_report WHERE id = ?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setLong(1, id);

                ResultSet rs = stmt.executeQuery();
                return rs.next() ? parseCustomerReport(rs) : null;
            }
        });
    }


    @Override
    public List<CustomerReport> getCustomerReports(final long customerId, final Date from, final Date to) {
        return query(new Action<List<CustomerReport>>() {
            @Override
            public List<CustomerReport> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM customer_report WHERE customer_id = ? AND start_date BETWEEN ? AND ?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setLong(1, customerId);
                stmt.setDate(2, new java.sql.Date(from.getTime()));
                stmt.setDate(3, new java.sql.Date(to.getTime()));

                ResultSet rs = stmt.executeQuery();

                List<CustomerReport> reports = new ArrayList<>();
                while (rs.next()) {
                    reports.add(parseCustomerReport(rs));
                }

                return reports;
            }
        });
    }


    @Override
    public CustomerReportItem getCustomerReportsItem(final long id) {
        return query(new Action<CustomerReportItem>() {
            @Override
            public CustomerReportItem execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM customer_report_item WHERE id = ?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setLong(1, id);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return parseCustomerReportItem(rs);
                }
                return null;
            }
        });
    }


    public boolean acceptReport(final long reportId) {
        return query(new Action<Boolean>() {
            @Override
            public Boolean execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "UPDATE customer_report SET accepted=TRUE WHERE id=?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setLong(1, reportId);

                return stmt.executeUpdate() > 0;
            }
        });
    }


    @Override
    public List<CustomerReportItem> getCustomerReportsItems(final long reportId) {
        return query(new Action<List<CustomerReportItem>>() {
            @Override
            public List<CustomerReportItem> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM customer_report_item WHERE report_id = ?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setLong(1, reportId);

                ResultSet rs = stmt.executeQuery();

                List<CustomerReportItem> reports = new ArrayList<>();
                while (rs.next()) {
                    reports.add(parseCustomerReportItem(rs));
                }

                return reports;
            }
        });
    }


    @Override
    public List<CustomerReportItem> getCustomerReportsItems(final long reportId, final int from, final int size) {
        return query(new Action<List<CustomerReportItem>>() {
            @Override
            public List<CustomerReportItem> execute(Connection con) throws SQLException {
//                PreparedStatement stmt = con.prepareStatement(
//                        "SELECT * FROM customer_report_item WHERE report_id = ? LIMIT ?, ?",
//                        ResultSet.TYPE_FORWARD_ONLY,
//                        ResultSet.CONCUR_READ_ONLY);
//                stmt.setLong(1, reportId);
//                stmt.setInt(2, from);
//                stmt.setInt(3, size);
//
//                ResultSet rs = stmt.executeQuery();
//
//                List<CustomerReportItem> reports = new ArrayList<>();
//                while (rs.next()) {
//                    reports.add(parseCustomerReportItem(rs));
//                }
//
//                return reports;

                PreparedStatement stmt = con.prepareStatement(
                        "SELECT\n" +
                                "  i.id item_id,\n" +
                                "  i.report_id item_report_id,\n" +
                                "  i.composition_id item_composition_id,\n" +
                                "  i.name item_name,\n" +
                                "  i.artist item_artist,\n" +
                                "  i.content_type item_content_type,\n" +
                                "  i.qty item_qty,\n" +
                                "  i.price item_price,\n" +
                                "  i.detected item_detected,\n" +
                                "  i.number item_number,\n" +
                                "  i.deleted item_deleted,\n" +
                                "  t.id track_id,\n" +
                                "  t.catalog_id track_catalog_id,\n" +
                                "  t.code track_code,\n" +
                                "  t.name track_name,\n" +
                                "  t.artist track_artist,\n" +
                                "  t.composer track_composer,\n" +
                                "  t.shareMobile track_shareMobile,\n" +
                                "  t.sharePublic track_sharePublic,  \n" +
                                "  c.id cat_id,\n" +
                                "  c.name cat_name,\n" +
                                "  c.right_type cat_right_type,\n" +
                                "  c.platform_id cat_platform_id,\n" +
                                "  c.royalty cat_royalty,\n" +
                                "  c.tracks cat_tracks,\n" +
                                "  c.artists cat_artists\n" +
                                "FROM customer_report_item i\n" +
                                "  LEFT JOIN composition t ON (i.composition_id = t.id)\n" +
                                "  LEFT JOIN catalog c ON (t.catalog_id = c.id)\n" +
                                "WHERE report_id = ? " +
//                                "AND t.shareMobile > 0 " +
                                "AND (deleted IS NULL OR NOT deleted) \n" +
                                "  ORDER BY item_number, cat_right_type, track_shareMobile, track_sharePublic DESC \n" +
                                "LIMIT ?, ?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setLong(1, reportId);
                stmt.setInt(2, from);
                stmt.setInt(3, size);

                ResultSet rs = stmt.executeQuery();

                List<CustomerReportItem> reports = new ArrayList<>();
                while (rs.next()) {
                    CustomerReportItem i = parseCustomerReportItem(rs, "item_");
                    Track t = parseTrack(rs, "track_");
                    Catalog c = parseCatalog(rs, "cat_");
                    t.setFoundCatalog(c);
                    i.setFoundTrack(t);
                    reports.add(i);
                }

                return reports;
            }
        });
    }


    @Override
    public List<CalculatedReportItem> calculateMobileReport(final String platform, final Date from, final Date to) {


        return query(new Action<List<CalculatedReportItem>>() {
            @Override
            public List<CalculatedReportItem> execute(Connection con) throws SQLException {

                PreparedStatement stmt = con.prepareStatement("SELECT\n" +
                        "  i.id,\n" +
                        "  c.code,\n" +
                        "  i.content_type,\n" +
                        "  replace(c.name, CHAR(9), ' ') name,\n" +
                        "  replace(c.artist, CHAR(9), ' ') artist,\n" +
                        "  replace(c.composer, CHAR(9), ' ') composer,\n" +
                        "  IF(cat.right_type = 1, 'author', 'related') right_type,\n" +
                        "  p.name platform,\n" +
                        "  cat.name catalog,\n" +
                        "\n" +
                        "  c.shareMobile,\n" +
                        "  cat.royalty cat_royalty,\n" +
                        "  IF(cat.right_type = 1, cm.authorRoyalty, cm.relatedRoyalty) royalty,\n" +
                        "\n" +
                        "  price,\n" +
                        "  sum(qty) totalQty,\n" +
                        "  (price * sum(qty)) vol,\n" +
                        "\n" +
                        "  round((sum(qty) * price * (shareMobile / 100) * (IF(cat.right_type = 1, cm.authorRoyalty, cm.relatedRoyalty) / 100) * (cat.royalty / 100)), 3) revenue\n" +
                        "\n" +
                        "\n" +
                        "FROM customer_report_item i\n" +
                        "\n" +
                        "  LEFT JOIN composition c\n" +
                        "    ON (i.composition_id = c.id)\n" +
                        "\n" +
                        "  LEFT JOIN catalog cat\n" +
                        "    ON (cat.id = c.catalog_id)\n" +
                        "\n" +
                        "  LEFT JOIN platform p\n" +
                        "    ON (cat.platform_id = p.id)\n" +
                        "\n" +
                        "  LEFT JOIN customer_report r\n" +
                        "    ON (i.report_id = r.id)\n" +
                        "\n" +
                        "  LEFT JOIN customer cm\n" +
                        "    ON (r.customer_id = cm.id)\n" +
                        "\n" +
                        "WHERE p.name = ?\n" +
                        "      AND r.accepted=TRUE \n" +
                        "      AND r.type = 0\n" +
                        "      AND r.start_date BETWEEN ? AND ?\n" +
                        "      AND i.detected = TRUE\n" +
                        "      AND (i.deleted IS NULL OR NOT i.deleted)\n" +
                        "\n" +
                        "GROUP BY i.composition_id",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);

                stmt.setString(1, platform);
                stmt.setDate(2, new java.sql.Date(from.getTime()));
                stmt.setDate(3, new java.sql.Date(to.getTime()));


                ResultSet rs = stmt.executeQuery();

                List<CalculatedReportItem> result = new ArrayList<>();

                while (rs.next()) {
                    result.add(parseCalculatedReport(rs));
                }

                return result;
            }
        });

    }


    @Override
    public List<CalculatedReportItem> calculatePublicReport(final String platform, final Date from, final Date to) {
        if (platform == null) return null;

        return query(new Action<List<CalculatedReportItem>>() {
            @Override
            public List<CalculatedReportItem> execute(Connection con) throws SQLException {

                PreparedStatement stmt = con.prepareStatement("SELECT\n" +
                        "  i.id,\n" +
                        "  c.code,\n" +
                        "  replace(c.name, CHAR(9), ' ') name,\n" +
                        "  replace(c.artist, CHAR(9), ' ') artist,\n" +
                        "  replace(c.composer, CHAR(9), ' ') composer,\n" +
                        "  IF(cat.right_type = 1, 'author', 'related') right_type,\n" +
                        "  p.name platform,\n" +
                        "  cat.name catalog,\n" +
                        "\n" +
                        "  c.sharePublic,\n" +
                        "  cat.royalty cat_royalty,\n" +
                        "  sum(qty) totalQty\n" +
                        "\n" +
                        "FROM customer_report_item i\n" +
                        "\n" +
                        "  LEFT JOIN composition c\n" +
                        "    ON (i.composition_id = c.id)\n" +
                        "\n" +
                        "  LEFT JOIN catalog cat\n" +
                        "    ON (cat.id = c.catalog_id)\n" +
                        "\n" +
                        "  LEFT JOIN platform p\n" +
                        "    ON (cat.platform_id = p.id)\n" +
                        "\n" +
                        "  LEFT JOIN customer_report r\n" +
                        "    ON (i.report_id = r.id)\n" +
                        "\n" +
                        "\n" +
                        "WHERE p.name = ?\n" +
                        "      AND r.accepted=TRUE \n" +
                        "      AND r.type = 1\n" +
                        "      AND r.start_date BETWEEN ? AND ?\n" +
                        "      AND i.detected = TRUE\n" +
                        "      AND (i.deleted IS NULL OR NOT i.deleted)\n" +
                        "\n" +
                        "GROUP BY i.composition_id\n;",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);

                stmt.setString(1, platform);
                stmt.setDate(2, new java.sql.Date(from.getTime()));
                stmt.setDate(3, new java.sql.Date(to.getTime()));

                ResultSet rs = stmt.executeQuery();

                List<CalculatedReportItem> result = new ArrayList<>();

                while (rs.next()) {
                    result.add(parseCalculatedPublicReport(rs));
                }

                return result;
            }
        });

    }


    @Override
    public long createCustomer(final Customer customer) {
        if (customer == null) return -1L;

        return query(new Action<Long>() {
            @Override
            public Long execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "INSERT INTO customer(name, customer_type, right_type, authorRoyalty, " +
                                "relatedRoyalty, contract) VALUES(?,?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, customer.getName());
                stmt.setInt(2, customer.getCustomerType().ordinal());
                stmt.setInt(3, customer.getRightType().ordinal());
                stmt.setFloat(4, customer.getAuthorRoyalty());
                stmt.setFloat(5, customer.getRelatedRoyalty());
                stmt.setString(6, customer.getContract());

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();

                return rs.next() ? rs.getLong(1) : -1L;
            }
        });
    }

    @Override
    public long createUser(final User user) {
        if (user == null) return -1L;

        return query(new Action<Long>() {
            @Override
            public Long execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "INSERT INTO user(login, password, customer_id, full_name, email) VALUES(?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, user.getLogin());
                stmt.setString(2, user.getPass());
                stmt.setLong(3, user.getCustomerId());
                stmt.setString(4, user.getFullName());
                stmt.setString(5, user.getEmail());

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();

                return rs.next() ? rs.getLong(1) : -1;
            }
        });
    }

    @Override
    public void removeItemFromBasket(final long trackId, final long customerId) {
        queryVoid(new VoidAction() {
            public void execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "DELETE FROM customer_basket_item WHERE track_id = ? AND customer_id= ?");

                stmt.setLong(1, trackId);
                stmt.setLong(2, customerId);
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public void removeItemFromReport(final long itemId) {
        queryVoid(new VoidAction() {
            public void execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "UPDATE customer_report_item SET deleted=TRUE WHERE id = ?");

                stmt.setLong(1, itemId);
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public List<Long> getAvailableCatalogs(final long customerId) {
        return query(new Action<List<Long>>() {
            @Override
            public List<Long> execute(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(
                        "SELECT cat.id FROM catalog cat LEFT JOIN customer c " +
                                "ON c.right_type = cat.right_type " +
                                "OR c.right_type=? " +
                                "WHERE c.id = ?",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, RightType.AUTHOR_RELATED.ordinal());
                ps.setLong(2, customerId);

                ResultSet rs = ps.executeQuery();

                List<Long> result = new ArrayList<>();

                while (rs.next()) {
                    result.add(rs.getLong("id"));
                }

                return result;
            }
        });
    }

    @Override
    public Integer updateTrack(final Track track) {
        return query(new Action<Integer>() {
            public Integer execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "UPDATE composition SET catalog_id=?, code=?," +
                                "name=?,artist=?,composer=?," +
                                "shareMobile=?,sharePublic=? WHERE id = ?");

                stmt.setLong(1, track.getCatalogId());
                stmt.setString(2, track.getCode());
                stmt.setString(3, track.getName());
                stmt.setString(4, track.getArtist());
                stmt.setString(5, track.getComposer());
                stmt.setFloat(6, track.getMobileShare());
                stmt.setFloat(7, track.getPublicShare());
                stmt.setFloat(8, track.getId());
                return stmt.executeUpdate();
            }
        });

    }

    @Override
    public void removeUser(final long id) {
        queryVoid(new VoidAction() {
            public void execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "DELETE FROM user WHERE id = ?");

                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public void removeCustomer(final long id) {
        queryVoid(new VoidAction() {
            public void execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "DELETE FROM customer WHERE id = ?");

                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
        });
    }


    @Override
    public List<User> getUsersByCustomerId(final long id) {
        return query(new Action<List<User>>() {
            @Override
            public List<User> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM user WHERE customer_id = ?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setLong(1, id);

                ResultSet rs = stmt.executeQuery();

                List<User> userList = new ArrayList<>();
                while (rs.next()) {
                    userList.add(parseUser(rs));
                }
                return userList;
            }
        });
    }


    public void resetTempTrackTable() {
        query(new Action<Object>() {
            @Override
            public Object execute(Connection con) throws SQLException {

                //create a clone structure as composition
//                con.createStatement().executeUpdate(
//                        "CREATE TABLE IF NOT EXISTS comp_tmp LIKE composition"
//                );
//
//                //add additional `done`-field
//                con.createStatement().executeUpdate(
//                        "ALTER TABLE comp_tmp ADD done TINYINT NULL"
//                );

                //clear all data if remain
                con.createStatement().executeUpdate(
                        "DELETE FROM comp_tmp"
                );

                return null;
            }
        });
    }

    @Override
    public long getLastCatalogUpdateId() {
        return query(new Action<Long>() {
            @Override
            public Long execute(Connection con) throws SQLException {

                PreparedStatement ps =
                        con.prepareStatement("SELECT max(id)id FROM catalog_update");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getLong("id");
                }
                return null;
            }
        });

    }

    public CatalogUpdate updateCatalog(final CatalogUpdate update) {
        return query(new Action<CatalogUpdate>() {
            @Override
            public CatalogUpdate execute(Connection con) throws SQLException {
                catalogLoading.set(true);

                PreparedStatement ps =
                        con.prepareStatement(
                                "INSERT INTO catalog_update(catalog_id, filepath, filename, whenUpdated) " +
                                        "VALUES (?,?,?, NOW())",
                                Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, update.getCatalogId());
                ps.setString(2, update.getFilePath());
                ps.setString(3, update.getFileName());

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (!rs.next()) return null;

                long updateId = rs.getLong(1);

                update.setId(updateId);

                //create a clone structure as composition
                PreparedStatement stmt = con.prepareStatement(
                        "LOAD DATA LOCAL INFILE ?\n" +
                                "INTO TABLE comp_tmp\n" +
                                "CHARACTER SET ?\n" +
                                "FIELDS TERMINATED BY ?\n" +
                                "OPTIONALLY ENCLOSED BY ?" +
                                "LINES TERMINATED BY ?\n" +
                                "IGNORE ? LINES\n" +
                                "(@dummy, code, name, composer, artist, @shareMobile, @sharePublic)\n" +
//                                "(@dummy, code, name, composer, artist, @dummy, @dummy, @shareMobile, @sharePublic)\n" +
                                "SET update_id=?,\n" +
                                "  catalog_id=?,\n" +
                                "  shareMobile=IF(@shareMobile != '', @shareMobile, 0),\n" +
                                "  sharePublic=IF(@sharePublic != '', @sharePublic, 0)");

                stmt.setString(1, update.getFilePath());
                stmt.setString(2, update.getEncoding());
                stmt.setString(3, update.getSeparator());
                stmt.setString(4, update.getEnclosedBy());
                stmt.setString(5, update.getNewline());
                stmt.setInt(6, update.getFromLine());
                stmt.setLong(7, updateId);
                stmt.setLong(8, update.getCatalogId());

                stmt.execute();

                // Check for errors...

                rs = con.createStatement().executeQuery("SHOW WARNINGS");

                Status st = Status.OK;
                while (rs.next()) {
                    String level = rs.getString("Level");
                    if ("Warning".equals(level)) {
                        UpdateWarning w = new UpdateWarning();
                        w.setNumber(rs.getInt("Code"));
                        w.parseMessage(rs.getString("Message"));
                        update.addWarning(w);
                        st = Status.HAS_WARNINGS;
                    }
                }
                update.setStatus(st);

                ps = con.prepareStatement(
                        "UPDATE catalog_update u " +
                                "SET status = ?," +
                                "tracks = (SELECT count(*) FROM comp_tmp WHERE update_id = u.id), " +
                                "crossing = (SELECT count(DISTINCT t.id) " +
                                "FROM composition c " +
                                "INNER JOIN comp_tmp t " +
                                "ON c.code = t.code " +
                                "AND c.catalog_id = t.catalog_id " +
                                "WHERE t.update_id = u.id)" +
                                "WHERE id = ?");
                ps.setString(1, st.toString());
                ps.setLong(2, updateId);

                ps.executeUpdate();

                return update;
            }
        });
    }


    public int getTempCompCount() {
        return query(new Action<Integer>() {
            @Override
            public Integer execute(Connection con) throws SQLException {

                PreparedStatement ps =
                        con.prepareStatement("SELECT count(*)c FROM comp_tmp");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("c");
                }
                return null;
            }
        });
    }

//    public String getQueryProcessTime(final long queryProcessId) {
//        return query(new Action<String>() {
//            @Override
//            public String execute(Connection con) throws SQLException {
//                PreparedStatement stmt = con.prepareStatement(
//                        "SELECT time FROM INFORMATION_SCHEMA.PROCESSLIST WHERE info LIKE '%@proc_id = " + queryId + "%'");
//
//                stmt.execute();
//                ResultSet rs = stmt.getResultSet();
//                if (rs.next()) {
//                    return rs.getString(1);
//                }
//
//                return null;
//            }
//        });
//    }

    public Long saveCatalogUpdate(final CatalogUpdate update) {
        return query(new Action<Long>() {
            @Override
            public Long execute(Connection con) throws SQLException {
                PreparedStatement ps =
                        con.prepareStatement(
                                "INSERT INTO catalog_update(whenUpdated, catalog_id, status, tracks, crossing, applied, filepath, filename) " +
                                        "VALUES (?,?,?,?,?,?,?,?)",
                                Statement.RETURN_GENERATED_KEYS);

                ps.setDate(1, new java.sql.Date(update.getWhenUpdated().getTime()));
                ps.setLong(2, update.getCatalogId());
                ps.setString(3, update.getStatus().toString());
                ps.setInt(4, update.getTracks());
                ps.setInt(5, update.getCrossing());
                ps.setBoolean(6, update.isApplied());
                ps.setString(7, update.getFilePath());
                ps.setString(8, update.getFileName());


                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                return rs.next() ? rs.getLong(1) : -1;
            }
        });
    }


    public List<TrackDiff> geChangedTracks(final long updateId, final int from, final int size) {

        return query(new Action<List<TrackDiff>>() {
            @Override
            public List<TrackDiff> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT c.code code," +
                                "c.id c_id, " +
                                "c.code c_code, " +
                                "c.catalog_id c_catalog_id, " +
                                "c.name c_name, " +
                                "c.artist c_artist, " +
                                "c.composer c_composer, " +
                                "c.shareMobile c_shareMobile, " +
                                "c.sharePublic c_sharePublic, " +
                                "t.id t_id, " +
                                "t.code t_code, " +
                                "t.catalog_id t_catalog_id, " +
                                "t.name t_name, " +
                                "t.artist t_artist, " +
                                "t.composer t_composer, " +
                                "t.shareMobile t_shareMobile, " +
                                "t.sharePublic t_sharePublic " +
                                "FROM comp_tmp t " +
                                "INNER JOIN composition c " +
                                "ON c.code = t.code " +
                                "AND c.catalog_id = t.catalog_id " +
                                "WHERE t.update_id = ? " +
                                "LIMIT ?, ?"
                );
                stmt.setLong(1, updateId);
                stmt.setInt(2, from);
                stmt.setInt(3, size);

                ResultSet rs = stmt.executeQuery();

                List<TrackDiff> res = new ArrayList<>();
                int num = from;
                while (rs.next()) {
                    TrackDiff d = new TrackDiff();
                    d.setNumber(num++);
                    d.setCode(rs.getString("code"));
                    d.setOldTrack(parseTrack(rs, "c_"));
                    d.setNewTrack(parseTrack(rs, "t_"));
                    res.add(d);
                }

                return res;
            }
        });

    }

    @Override
    public List<Long> getCustomerBasket(final long customerId) {
        return query(new Action<List<Long>>() {
            @Override
            public List<Long> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT track_id FROM customer_basket_item WHERE customer_id = ?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setLong(1, customerId);

                ResultSet rs = stmt.executeQuery();

                List<Long> trackIdList = new ArrayList<>();
                while (rs.next()) {
                    trackIdList.add(rs.getLong("track_id"));
                }
                return trackIdList;
            }
        });
    }

    @Override
    public void addItemToBasket(final long customerId, final long trackId) {

        query(new Action<Object>() {
            @Override
            public Object execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "INSERT INTO customer_basket_item (customer_id, track_id) VALUES (?,?)",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY
                );
                stmt.setLong(1, customerId);
                stmt.setLong(2, trackId);

                stmt.executeUpdate();
                return null;
            }
        });
    }

    @Override
    public List<Track> getNewTracks(final long updateId, final int from, final int size) {
        return query(new Action<List<Track>>() {
            @Override
            public List<Track> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM composition WHERE artist LIKE?");
//                stmt.setString(1, artist);

                ResultSet rs = stmt.executeQuery();

                List<Track> tracks = new ArrayList<>();
                while (rs.next()) {
                    tracks.add(parseTrack(rs));
                }
                return tracks;
            }
        });
    }


    @Override
    public void applyCatalogUpdate(final long updateId) {
        query(new Action<Object>() {
            @Override
            public Object execute(Connection con) throws SQLException {
                PreparedStatement stmt1 = con.prepareStatement(
                        "UPDATE composition c\n" +
                                "  INNER JOIN comp_tmp t\n" +
                                "    ON c.code = t.code\n" +
                                "     AND c.catalog_id = t.catalog_id\n" +
                                "SET c.name = IF(t.name != '', t.name, c.name),\n" +
                                "  c.composer = IF(t.composer != '', t.composer, c.composer),\n" +
                                "  c.artist = IF(t.artist != '', t.artist, c.artist),\n" +
                                "  c.shareMobile = IF(t.shareMobile != '', t.shareMobile, c.shareMobile),\n" +
                                "  c.sharePublic = IF(t.sharePublic != '', t.sharePublic, c.sharePublic),\n" +
                                "  t.done = 1\n" +
                                "WHERE t.update_id = ?"
                );
                stmt1.setLong(1, updateId);
                stmt1.executeUpdate();


                PreparedStatement stmt2 = con.prepareStatement(
                        "INSERT INTO composition (code, name, composer, artist, shareMobile, sharePublic, catalog_id)\n" +
                                "  SELECT\n" +
                                "    code,\n" +
                                "    name,\n" +
                                "    composer,\n" +
                                "    artist,\n" +
                                "    shareMobile,\n" +
                                "    sharePublic,\n" +
                                "    catalog_id\n" +
                                "  FROM comp_tmp\n" +
                                "  WHERE done IS null AND update_id = ?"
                );
                stmt2.setLong(1, updateId);
                stmt2.executeUpdate();

                PreparedStatement stmt3 = con.prepareStatement(
                        "UPDATE  catalog_update SET applied = TRUE WHERE id = ?"
                );
                stmt3.setLong(1, updateId);
                stmt3.executeUpdate();


                return null;
            }
        });

    }

    @Override
    public List<Track> getTracks(final int from, final int size) {
        return query(new Action<List<Track>>() {
            @Override
            public List<Track> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM composition LIMIT " + from + "," + size,
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery();

                List<Track> tracks = new ArrayList<>();
                while (rs.next()) {
                    tracks.add(parseTrack(rs));
                }
                return tracks;
            }
        });
    }

    @Override
    public List<Track> getTempTracks(final long catalogId, final int from, final int size) {
        return query(new Action<List<Track>>() {
            @Override
            public List<Track> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM comp_tmp WHERE catalog_id=" +
                        catalogId + " LIMIT " + from + "," + size,

                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery();

                List<Track> tracks = new ArrayList<>();
                while (rs.next()) {
                    tracks.add(parseTrack(rs));
                }
                return tracks;
            }
        });
    }

    @Override
    public List<Track> getTracks(final long catalogId, final int from, final int size) {
        return query(new Action<List<Track>>() {
            @Override
            public List<Track> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM composition WHERE catalog_id=" +
                        catalogId + " LIMIT " + from + "," + size,

                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery();

                List<Track> tracks = new ArrayList<>();
                while (rs.next()) {
                    tracks.add(parseTrack(rs));
                }
                return tracks;
            }
        });
    }

    @Override
    public int getTrackCount() {
        return query(new Action<Integer>() {
            @Override
            public Integer execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT COUNT(*)cnt FROM composition",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("cnt");
                }
                return null;
            }
        });
    }

    public List<Track> getUpdates(final long updateId) {
        return query(new Action<List<Track>>() {
            @Override
            public List<Track> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM comp_tmp WHERE id = ?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setLong(1, updateId);

                ResultSet rs = stmt.executeQuery();
                List<Track> tracks = new ArrayList<>();
                while (rs.next()) {
                    tracks.add(parseTrack(rs));
                }

                return tracks;
            }
        });
    }


    @Override
    public CatalogUpdate getCatalogUpdate(final long updateId) {
        return query(new Action<CatalogUpdate>() {
            @Override
            public CatalogUpdate execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM catalog_update WHERE id = ?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setLong(1, updateId);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return parseCatalogUpdate(rs);
                }

                return null;
            }
        });
    }


    @Override
    public List<CatalogUpdate> getCatalogUpdates(final long catalogId) {
        return query(new Action<List<CatalogUpdate>>() {
            @Override
            public List<CatalogUpdate> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM catalog_update WHERE catalog_id = ?",
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                stmt.setLong(1, catalogId);

                ResultSet rs = stmt.executeQuery();

                List<CatalogUpdate> reportList = new ArrayList<>();

                while (rs.next()) {
                    reportList.add(parseCatalogUpdate(rs));
                }

                return reportList;
            }
        });
    }

    @Override
    public void updateCatalogsStat() {
        query(new Action() {
            @Override
            public Object execute(Connection con) throws SQLException {
                con.createStatement().executeUpdate(
                        "UPDATE catalog c\n" +
                                "SET tracks = (SELECT\n" +
                                "                count(DISTINCT t.id)\n" +
                                "              FROM composition t\n" +
                                "              WHERE t.catalog_id = c.id)"
                );

                con.createStatement().executeUpdate(
                        "UPDATE catalog c\n" +
                                "SET artists = (SELECT\n" +
                                "                count(DISTINCT t.artist)\n" +
                                "              FROM composition t\n" +
                                "              WHERE t.catalog_id = c.id)"
                );
                return null;
            }
        });
    }


    //  Parsers  ----------------------------------------------------------------------------------------

    private static Platform parsePlatform(ResultSet rs) throws SQLException {
        Platform platform = new Platform();
        platform.setId(rs.getLong("id"));
        platform.setName(rs.getString("name"));
        platform.setRights(rs.getBoolean("rights"));
        return platform;
    }

    private static Catalog parseCatalog(ResultSet rs) throws SQLException {
        return parseCatalog(rs, "");
    }

    private static Catalog parseCatalog(ResultSet rs, String tblPrefix) throws SQLException {
        Catalog catalog = new Catalog();
        catalog.setId(rs.getLong(tblPrefix + "id"));
        catalog.setName(rs.getString(tblPrefix + "name"));
        catalog.setRoyalty(rs.getFloat(tblPrefix + "royalty"));
        int rightType = rs.getInt(tblPrefix + "right_type");
        catalog.setRightType(RightType.values()[rightType]);
        catalog.setTracks(rs.getInt(tblPrefix + "tracks"));
        catalog.setPlatformId(rs.getLong(tblPrefix + "platform_id"));
        catalog.setArtists(rs.getInt(tblPrefix + "artists"));
        return catalog;
    }

    private static Track parseTrack(ResultSet rs) throws SQLException {
        return parseTrack(rs, "");
    }


    private static Track parseTrack(ResultSet rs, String tblPrefix) throws SQLException {
        Track track = new Track();
        track.setCatalog(catalogMap.get(rs.getLong(tblPrefix + "catalog_id")));
        track.setId(rs.getLong(tblPrefix + "id"));
        track.setCode(rs.getString(tblPrefix + "code"));
        track.setName(rs.getString(tblPrefix + "name"));
        track.setArtist(rs.getString(tblPrefix + "artist"));
        track.setComposer(rs.getString(tblPrefix + "composer"));
        track.setMobileShare(rs.getFloat(tblPrefix + "shareMobile"));
        track.setPublicShare(rs.getFloat(tblPrefix + "sharePublic"));
        return track;
    }

    private static Customer parseCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getLong("id"));
        customer.setName(rs.getString("name"));
        customer.setCustomerType(CustomerType.values()[rs.getInt("customer_type")]);
        customer.setRightType(RightType.values()[rs.getInt("right_type")]);
        customer.setAuthorRoyalty(rs.getFloat("authorRoyalty"));
        customer.setRelatedRoyalty(rs.getFloat("relatedRoyalty"));
        customer.setContract(rs.getString("contract"));

        return customer;
    }

    private User parseUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setLogin(rs.getString("login"));
        user.setPass(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setCustomerId(rs.getLong("customer_id"));
        return user;
    }

    private AdminUser parseAdmin(ResultSet rs) throws SQLException {
        AdminUser adminUser = new AdminUser();
        adminUser.setId(rs.getLong("id"));
        adminUser.setLogin(rs.getString("login"));
        adminUser.setPass(rs.getString("password"));
        return adminUser;
    }

    private static CustomerReport parseCustomerReport(ResultSet rs) throws SQLException {

        CustomerReport report = new CustomerReport();
        report.setId(rs.getLong("id"));
        int type = rs.getInt("type");
        report.setType(CustomerReport.Type.values()[type]);

        if (report.getType() == CustomerReport.Type.MOBILE) {
            long customerId = rs.getLong("customer_id");
            report.setCustomerId(customerId);
        }

        report.setStartDate(rs.getDate("start_date"));
        int period = rs.getInt("period");
        report.setPeriod(CustomerReport.Period.values()[period]);
        report.setUploadDate(rs.getDate("upload_date"));

        report.setTracks(rs.getInt("tracks"));
        report.setDetected(rs.getInt("detected"));
        report.setRevenue(rs.getLong("revenue"));
        report.setAccepted(rs.getBoolean("accepted"));

        return report;
    }


    private static CustomerReportItem parseCustomerReportItem(ResultSet rs) throws SQLException {
        return parseCustomerReportItem(rs, "");
    }

    private static CustomerReportItem parseCustomerReportItem(ResultSet rs, String tablePrefix) throws SQLException {

        CustomerReportItem item = new CustomerReportItem();
        item.setId(rs.getLong(tablePrefix + "id"));
        item.setReportId(rs.getLong(tablePrefix + "report_id"));
        item.setCompositionId(rs.getLong(tablePrefix + "composition_id"));
        item.setTrack(rs.getString(tablePrefix + "name"));
        item.setArtist(rs.getString(tablePrefix + "artist"));
        item.setContentType(rs.getString(tablePrefix + "content_type"));
        item.setQty(rs.getInt(tablePrefix + "qty"));
        item.setPrice(rs.getFloat(tablePrefix + "price"));
        item.setDetected(rs.getBoolean(tablePrefix + "detected"));
        item.setNumber(rs.getInt(tablePrefix + "number"));

        return item;
    }


    private static CalculatedReportItem parseCalculatedReport(ResultSet rs) throws SQLException {

        CalculatedReportItem report = new CalculatedReportItem();
        report.setReportItemId(rs.getLong("id"));
        report.setCompositionCode(rs.getString("code"));
        report.setCompositionName(rs.getString("name"));
        report.setArtist(rs.getString("artist"));
        report.setComposer(rs.getString("composer"));
        report.setPrice(rs.getFloat("price"));
        report.setQty(rs.getInt("totalQty"));
        report.setContentType(rs.getString("content_type"));
        report.setVol(rs.getFloat("vol"));
        report.setShareMobile(rs.getFloat("shareMobile"));
        report.setCustomerRoyalty(rs.getFloat("royalty"));
        report.setCatalogRoyalty(rs.getFloat("cat_royalty"));
        report.setRevenue(rs.getFloat("revenue"));
        report.setCatalog(rs.getString("catalog"));
        report.setCopyright(rs.getString("right_type"));
        return report;
    }

    private static CalculatedReportItem parseCalculatedPublicReport(ResultSet rs) throws SQLException {


        CalculatedReportItem report = new CalculatedReportItem();
        report.setReportItemId(rs.getLong("id"));
        report.setCompositionCode(rs.getString("code"));
        report.setCompositionName(rs.getString("name"));
        report.setArtist(rs.getString("artist"));
        report.setComposer(rs.getString("composer"));
//        report.setPrice(rs.getFloat("price"));
        report.setQty(rs.getInt("totalQty"));
//        report.setContentType(rs.getString("content_type"));
//        report.setVol(rs.getFloat("vol"));
        report.setShareMobile(rs.getFloat("sharePublic"));
//        report.setCustomerRoyalty(rs.getFloat("royalty"));
        report.setCatalogRoyalty(rs.getFloat("cat_royalty"));
//        report.setRevenue(rs.getFloat("revenue"));
        report.setCatalog(rs.getString("catalog"));
        report.setCopyright(rs.getString("right_type"));
        return report;
    }


    private static CatalogUpdate parseCatalogUpdate(ResultSet rs) throws SQLException {

        CatalogUpdate update = new CatalogUpdate();
        update.setId(rs.getLong("id"));
        update.setWhenUpdated(rs.getDate("whenUpdated"));
        update.setCatalogId(rs.getLong("catalog_id"));
        update.setStatus(Status.valueOf(rs.getString("status")));
        update.setTracks(rs.getInt("tracks"));
        update.setCrossing(rs.getInt("crossing"));
        update.setApplied(rs.getBoolean("applied"));
        update.setFilePath(rs.getString("filepath"));
        update.setFileName(rs.getString("filename"));

        return update;
    }

    //    ------------------------------------------------

    public void closeConnection() {
        pool.close();
    }


    private static ComboPooledDataSource initPool(String host,
                                                  String port,
                                                  String base,
                                                  String user,
                                                  String pass) {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + base + "?characterEncoding=UTF-8";

        ComboPooledDataSource pool = new ComboPooledDataSource();
        try {
            pool.setDriverClass("com.mysql.jdbc.Driver");
            pool.setJdbcUrl(url);
            pool.setUser(user);
            pool.setPassword(pass);
            pool.setMinPoolSize(MIN_POOL_SIZE);
            pool.setMaxPoolSize(MAX_POOL_SIZE);
            pool.setMaxStatements(MAX_STATEMENTS);
            pool.setMaxStatementsPerConnection(MAX_STATEMENTS_PER_CONNECTION);

        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return pool;
    }

    private int prepareCatalogsMap(Map<Long, String> catalogMap) {
        Connection connection = null;
        try {
            connection = pool.getConnection();

            Statement st = connection.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM catalog");

            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                catalogMap.put(id, name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    public static String asString(List<Long> ids) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            long id = ids.get(i);
            buf.append(id);
            if (i < ids.size() - 1) {
                buf.append(", ");
            }
        }

        return buf.toString();
    }

    public static String asStringBySearchResults(List<SearchResult> ids) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            SearchResult r = ids.get(i);
            buf.append(r.getTrackId());
            if (i < ids.size() - 1) {
                buf.append(", ");
            }
        }

        return buf.toString();
    }


    private <T> T query(Action<T> action) {

        Connection connection = null;
        try {
            connection = pool.getConnection();
            return action.execute(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


    private void queryVoid(VoidAction action) {

        Connection connection = null;
        try {
            connection = pool.getConnection();
            action.execute(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public static interface Action<T> {

        T execute(Connection con) throws SQLException;

    }

    public static interface VoidAction {

        void execute(Connection con) throws SQLException;

    }


}
