package kz.bgm.platform.model.service;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import kz.bgm.platform.model.domain.*;

import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class DbStorage implements CatalogStorage {


    public static final int MAX_STATEMENTS = 200;
    public static final int MAX_STATEMENTS_PER_CONNECTION = 10;
    public static final int MIN_POOL_SIZE = 1;
    public static final int MAX_POOL_SIZE = 10;

    private final ComboPooledDataSource pool;

    private static Map<Long, String> catalogMap = new HashMap<Long, String>();


    public DbStorage(String host, String port,
                     String base, String user, String pass) {
        pool = initPool(host, port, base, user, pass);

        prepareCatalogsMap(catalogMap);

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
                    connection.prepareStatement("SELECT id FROM catalog WHERE name=?");

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
                        "SELECT * FROM platform WHERE id=?");
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
                        "SELECT * FROM catalog WHERE id=?");
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
                        "SELECT * FROM composition WHERE id=?");
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
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM platform");

                ResultSet rs = stmt.executeQuery();

                List<Platform> tracks = new ArrayList<Platform>();
                while (rs.next()) {
                    tracks.add(parsePlatform(rs));
                }
                return tracks;
            }
        });
    }

    @Override
    public List<Catalog> getAllCatalogs() {
        return query(new Action<List<Catalog>>() {
            @Override
            public List<Catalog> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM catalog");
                ResultSet rs = stmt.executeQuery();

                List<Catalog> tracks = new ArrayList<Catalog>();
                while (rs.next()) {
                    tracks.add(parseCatalog(rs));
                }
                return tracks;
            }
        });
    }

    @Override
    public List<Track> getAllTracks() {
        return query(new Action<List<Track>>() {
            @Override
            public List<Track> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM composition");
                ResultSet rs = stmt.executeQuery();

                List<Track> tracks = new ArrayList<Track>();
                while (rs.next()) {
                    tracks.add(parseTrack(rs));
                }
                return tracks;
            }
        });

    }


    @Override
    public List<Track> getTracks(final List<Long> ids) {
        return query(new Action<List<Track>>() {
            @Override
            public List<Track> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM composition WHERE id IN (" + DbStorage.asString(ids) + ")");

                ResultSet rs = stmt.executeQuery();

                List<Track> result = new ArrayList<Track>();
                while (rs.next()) {
                    result.add(parseTrack(rs));
                }

                return result;
            }
        });

    }


    public List<Track> searchTracks(final String value) {
        return query(new Action<List<Track>>() {
            @Override
            public List<Track> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM composition WHERE " +
                                "name LIKE ? OR " +
                                "composer LIKE ? OR " +
                                "artist LIKE ? OR " +
                                "code LIKE ? ");

                stmt.setString(1, "%" + value + "%");
                stmt.setString(2, "%" + value + "%");
                stmt.setString(3, "%" + value + "%");
                stmt.setString(4, "%" + value + "%");

                stmt.setMaxRows(100);

                ResultSet rs = stmt.executeQuery();

                List<Track> tracks = new ArrayList<Track>();
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
                        "SELECT * FROM composition WHERE name=?");
                stmt.setString(1, name);

                ResultSet rs = stmt.executeQuery();

                List<Track> tracks = new ArrayList<Track>();
                while (rs.next()) {
                    tracks.add(parseTrack(rs));
                }
                return tracks;
            }
        });

    }


    @Override
    public List<Track> searchTracksByCode(final String code) {
        return query(new Action<List<Track>>() {
            @Override
            public List<Track> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM composition WHERE code=?");
                stmt.setString(1, code);

                ResultSet rs = stmt.executeQuery();

                List<Track> tracks = new ArrayList<Track>();
                while (rs.next()) {
                    tracks.add(parseTrack(rs));
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
                        "SELECT * FROM composition WHERE composer=?");
                stmt.setString(1, composer);

                ResultSet rs = stmt.executeQuery();

                List<Track> tracks = new ArrayList<Track>();
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
                        "SELECT * FROM composition WHERE artist=?");
                stmt.setString(1, artist);

                ResultSet rs = stmt.executeQuery();

                List<Track> tracks = new ArrayList<Track>();
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
                        "SELECT * FROM composition WHERE artist LIKE?");
                stmt.setString(1, artist);

                ResultSet rs = stmt.executeQuery();

                List<Track> tracks = new ArrayList<Track>();
                while (rs.next()) {
                    tracks.add(parseTrack(rs));
                }
                return tracks;
            }
        });
    }

    @Override
    public List<Customer> getCustomers() {
        return query(new Action<List<Customer>>() {
            @Override
            public List<Customer> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM customer");
                ResultSet rs = stmt.executeQuery();

                List<Customer> customers = new ArrayList<Customer>();
                while (rs.next()) {
                    customers.add(parseCustomer(rs));
                }
                return customers;
            }
        });
    }


    @Override
    public Customer getCustomer(final String name) {
        return query(new Action<Customer>() {
            @Override
            public Customer execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM customer WHERE name=?");
                stmt.setString(1, name);

                ResultSet rs = stmt.executeQuery();
                return rs.next() ? parseCustomer(rs) : null;
            }
        });
    }


    @Override
    public Customer getCustomer(final long id) {
        return query(new Action<Customer>() {
            @Override
            public Customer execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM customer WHERE id=?");
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
                        "SELECT * FROM user_admin WHERE login = ? AND password = ?");
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
                        "SELECT * FROM user WHERE login = ? AND password = ?");
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
                        "SELECT * FROM user WHERE login = ?");
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
                        con.prepareStatement("INSERT INTO customer_report(customer_id, start_date, upload_date, type, period) VALUES (?,?,?,?,?)",
                                Statement.RETURN_GENERATED_KEYS);

                ps.setLong(1, report.getCustomerId());
                ps.setDate(2, new java.sql.Date(report.getStartDate().getTime()));
                ps.setDate(3, new java.sql.Date(report.getUploadDate().getTime()));
                ps.setInt(4, report.getType().ordinal());
                ps.setInt(5, report.getPeriod().ordinal());

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                return rs.next() ? rs.getLong(1) : -1;
            }
        });
    }


    @Override
    public void saveCustomerReportItems(final List<CustomerReportItem> items) {
        query(new Action<Boolean>() {
            @Override
            public Boolean execute(Connection con) throws SQLException {
                PreparedStatement ps =
                        con.prepareStatement("INSERT INTO " +
                                "customer_report_item(report_id,composition_id,name," +
                                "artist,content_type,qty,price) " +
                                "VALUES (?,?,?,?,?,?,?)");

                for (CustomerReportItem cr : items) {
                    ps.setLong(1, cr.getReportId());
                    if (cr.getCompositionId() != null) {
                        ps.setLong(2, cr.getCompositionId());
                    } else {
                        ps.setNull(2, Types.INTEGER);
                    }

                    ps.setString(3, cr.getName());
                    ps.setString(4, cr.getArtist());
                    ps.setString(5, cr.getContentType());
                    ps.setInt(6, cr.getQty());
                    ps.setFloat(7, cr.getPrice());

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
    public int getArtistCount(final long catalogId) {
        return query(new Action<Integer>() {
            @Override
            public Integer execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT count(DISTINCT artist)cnt FROM composition WHERE catalog_id=?");
                stmt.setLong(1, catalogId);

                ResultSet rs = stmt.executeQuery();

                return rs.next() ? rs.getInt("cnt") : -1;
            }
        });
    }

    @Override
    public List<CustomerReport> getAllCustomerReports() {
        return query(new Action<List<CustomerReport>>() {
            @Override
            public List<CustomerReport> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM customer_report");

                ResultSet rs = stmt.executeQuery();

                List<CustomerReport> reportList = new ArrayList();

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
                        "SELECT * FROM customer_report WHERE id = ?");
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
                        "SELECT * FROM customer_report WHERE customer_id = ? AND start_date BETWEEN ? AND ?");
                stmt.setLong(1, customerId);
                stmt.setDate(2, new java.sql.Date(from.getTime()));
                stmt.setDate(3, new java.sql.Date(to.getTime()));

                ResultSet rs = stmt.executeQuery();

                List<CustomerReport> reports = new ArrayList<CustomerReport>();
                while (rs.next()) {
                    reports.add(parseCustomerReport(rs));
                }

                return reports;
            }
        });
    }

    @Override
    public List<CustomerReportItem> getCustomerReportsItems(final long reportId) {
        return query(new Action<List<CustomerReportItem>>() {
            @Override
            public List<CustomerReportItem> execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM customer_report_item WHERE report_id = ?");
                stmt.setLong(1, reportId);

                ResultSet rs = stmt.executeQuery();

                List<CustomerReportItem> reports = new ArrayList<CustomerReportItem>();
                while (rs.next()) {
                    reports.add(parseCustomerReportItem(rs));
                }

                return reports;
            }
        });
    }


    @Override
    public CalculatedReportItem calculateMReportAuthor(final CustomerReportItem reportItem) {
        return query(new Action<CalculatedReportItem>() {
            @Override
            public CalculatedReportItem execute(Connection con) throws SQLException {
                CalculatedReportItem report = null;
                System.out.println("Composition id" + reportItem.getCompositionId());
                PreparedStatement ps = con.prepareStatement("SELECT\n" +
                        "  copyright,\n" +
                        "  cat.name catalog,\n" +
                        "  composition.shareMobile,\n" +
                        "  composition.code,\n" +
                        "  composition.artist,\n" +
                        "  composition.name name,\n" +
                        "  composition.composer\n" +
                        "FROM composition\n" +
                        "\n" +
                        "  LEFT JOIN catalog cat\n" +
                        "    ON (cat.id = catalog_id)\n" +
                        "\n" +
                        "WHERE composition.id =" + reportItem.getCompositionId() + "\n" +
                        "and cat.copyright='AUTHOR'\n" +
                        "GROUP BY composition.id;");

                ResultSet rs = ps.executeQuery();


                while (rs.next()) {
                    report = new CalculatedReportItem();
//                    report.setReportItemId(rs.getLong("id"));
                    report.setCompositionCode(rs.getString("code"));
                    report.setCompositionName(rs.getString("name"));
                    report.setArtist(rs.getString("artist"));
                    report.setComposer(rs.getString("composer"));
                    report.setShareMobile(rs.getFloat("shareMobile"));
                    report.setCatalog(rs.getString("catalog"));
                    report.setCopyright(rs.getString("copyright"));
                    report.setQty(reportItem.getQty());
                    report.setPrice(reportItem.getPrice());
                }
                return report;
            }
        });

    }

    @Override
    public CalculatedReportItem calculateMReportRelated(final CustomerReportItem reportItem) {
        return query(new Action<CalculatedReportItem>() {
            @Override
            public CalculatedReportItem execute(Connection con) throws SQLException {
                CalculatedReportItem report = null;
                System.out.println("Composition id" + reportItem.getCompositionId());
                PreparedStatement ps = con.prepareStatement("SELECT\n" +
                        "  copyright,\n" +
                        "  cat.name catalog,\n" +
                        "  composition.shareMobile,\n" +
                        "  composition.code,\n" +
                        "  composition.artist,\n" +
                        "  composition.name name,\n" +
                        "  composition.composer\n" +
                        "FROM composition\n" +
                        "\n" +
                        "  LEFT JOIN catalog cat\n" +
                        "    ON (cat.id = catalog_id)\n" +
                        "\n" +
                        "WHERE composition.id =" + reportItem.getCompositionId() + "\n" +
                        "and cat.copyright='RELATED'\n" +
                        "GROUP BY composition.id;");

                ResultSet rs = ps.executeQuery();


                while (rs.next()) {
                    report = new CalculatedReportItem();
                    //                    report.setReportItemId(rs.getLong("id"));
                    report.setCompositionCode(rs.getString("code"));
                    report.setCompositionName(rs.getString("name"));
                    report.setArtist(rs.getString("artist"));
                    report.setComposer(rs.getString("composer"));
                    report.setShareMobile(rs.getFloat("shareMobile"));
                    report.setCatalog(rs.getString("catalog"));
                    report.setCopyright(rs.getString("copyright"));
                    report.setQty(reportItem.getQty());
                    report.setPrice(reportItem.getPrice());
                }
                return report;
            }
        });

    }

    @Override
    public int getCompositionCount(final long catalogId) {
        return query(new Action<Integer>() {
            @Override
            public Integer execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT COUNT(*)cnt FROM composition where catalog_id= ?");

                stmt.setLong(1, catalogId);

                ResultSet rs = stmt.executeQuery();


                return rs.next() ? rs.getInt("cnt") : null;
            }
        });
    }

    @Override
    public List<CalculatedReportItem> calculateMobileReport(final String catalogName) {

        if (catalogName == null) return null;

        final String extraQuery = "".equals(catalogName) ? "" :
                "and cat.name='" + catalogName + "'";

        return query(new Action<List<CalculatedReportItem>>() {
            @Override
            public List<CalculatedReportItem> execute(Connection con) throws SQLException {

                PreparedStatement ps = con.prepareStatement("SELECT\n" +
                        "  i.id,\n" +
                        "  c.code,\n" +
                        "  i.content_type,\n" +
                        "  replace(c.name, CHAR(9), ' ') name,\n" +
                        "  replace(c.artist, CHAR(9), ' ') artist,\n" +
                        "  replace(c.composer, CHAR(9), ' ') composer,\n" +
                        "  cat.copyright copyright,\n" +
                        "  p.name platform,\n" +
                        "  cat.name catalog,\n" +
                        "\n" +
                        "  c.shareMobile,\n" +
                        "  cat.royalty cat_royalty,\n" +
                        "  cm.royalty,\n" +
                        "\n" +
                        "  price,\n" +
                        "  sum(qty) totalQty,\n" +
                        "  (price * sum(qty)) vol,\n" +
                        "\n" +
                        "  round((sum(qty) * price * (shareMobile / 100) * (cm.royalty / 100) * (cat.royalty / 100)), 3) revenue\n" +
                        "\n" +
                        "\n" +
                        "FROM customer_report_item i\n" +
                        "\n" +
                        "  LEFT JOIN composition c\n" +
                        "    ON (i.composition_id = c.id)\n" +
                        "\n" +
                        "  LEFT JOIN catalog cat\n" +
                        "    ON (cat.id = c.catalog_id " + extraQuery + ")\n" +
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
                        "WHERE cat.platform_id = 1\n" +
                        "      AND r.type = 0\n" +
                        "      AND i.composition_id > 0\n" +
                        "\n" +
                        "GROUP BY i.composition_id");

                ResultSet rs = ps.executeQuery();

                List<CalculatedReportItem> result = new ArrayList<CalculatedReportItem>();

                while (rs.next()) {
                    result.add(parseCalculatedReport(rs));
                }

                return result;
            }
        });

    }

    @Override
    public List<CalculatedReportItem> calculatePublicReport(final String catalogName) {
        if (catalogName == null) return null;

        return query(new Action<List<CalculatedReportItem>>() {
            @Override
            public List<CalculatedReportItem> execute(Connection con) throws SQLException {

                PreparedStatement ps = con.prepareStatement("SELECT\n" +
                        "  i.id,\n" +
                        "  c.code,\n" +
                        "  replace(c.name, CHAR(9), ' ') name,\n" +
                        "  replace(c.artist, CHAR(9), ' ') artist,\n" +
                        "  replace(c.composer, CHAR(9), ' ') composer,\n" +
                        "  cat.copyright copyright,\n" +
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
                        "WHERE\n" +
                        "  cat.platform_id = 1\n" +
                        "      AND r.type = 1\n" +
                        "      AND cat.copyright = 'AUTHOR'\n" +
                        "      AND i.composition_id > 0\n" +
                        "\n" +
                        "GROUP BY i.composition_id\n;");

                ResultSet rs = ps.executeQuery();

                List<CalculatedReportItem> result = new ArrayList<>();

                while (rs.next()) {
                    result.add(parseCalculatedPublicReport(rs));
                }

                return result;
            }
        });

    }

    @Override
    public void removeUser(final long id) {
        queryVoid(new VoidAction() {
            public void execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "delete from user where id = ?");

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
                        "delete from customer where id = ?");

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
                        "SELECT * FROM user WHERE customer_id = ?");
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

    //  Parsers  ----------------------------------------------------------------------------------------

    private static Platform parsePlatform(ResultSet rs) throws SQLException {
        Platform platform = new Platform();
        platform.setId(rs.getLong("id"));
        platform.setName(rs.getString("name"));
        return platform;
    }

    private static Catalog parseCatalog(ResultSet rs) throws SQLException {
        Catalog catalog = new Catalog();
        catalog.setId(rs.getLong("id"));
        catalog.setName(rs.getString("name"));
        catalog.setRoyalty(rs.getFloat("royalty"));
        catalog.setCopyright(rs.getString("copyright"));
        return catalog;
    }

    private static Track parseTrack(ResultSet rs) throws SQLException {
        Track track = new Track();
        track.setCatalog(catalogMap.get(rs.getLong("catalog_id")));
        track.setId(rs.getLong("id"));
        track.setCode(rs.getString("code"));
        track.setName(rs.getString("name"));
        track.setArtist(rs.getString("artist"));
        track.setComposer(rs.getString("composer"));
        track.setMobileShare(rs.getFloat("shareMobile"));
        track.setPublicShare(rs.getFloat("sharePublic"));
        return track;
    }

    private static Customer parseCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getLong("id"));
        customer.setName(rs.getString("name"));
        customer.setContract(rs.getString("contract"));
        customer.setRightType(rs.getString("right_type"));
        customer.setRoyalty(rs.getFloat("royalty"));
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

        return report;
    }


    private static CustomerReportItem parseCustomerReportItem(ResultSet rs) throws SQLException {

        CustomerReportItem item = new CustomerReportItem();
        item.setId(rs.getLong("id"));
        item.setReportId(rs.getLong("report_id"));
        item.setCompositionId(rs.getLong("composition_id"));
        item.setName(rs.getString("name"));
        item.setArtist(rs.getString("artist"));
        item.setContentType(rs.getString("content_type"));
        item.setQty(rs.getInt("qty"));
        item.setPrice(rs.getFloat("price"));

        return item;
    }

    @Override
    public long createCustomer(final Customer customer) {
        if (customer == null) return -1L;

        return query(new Action<Long>() {
            @Override
            public Long execute(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(
                        "insert into customer(contract, name, right_type, royalty) values(?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, customer.getContract());
                stmt.setString(2, customer.getName());
                stmt.setString(3, customer.getRightType());
                stmt.setFloat(4, customer.getRoyalty());

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
                        "insert into user(login,password,customer_id,full_name,email) values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
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
        report.setCopyright(rs.getString("copyright"));
        return report;
    }

    private static CalculatedReportItem parseCalculatedPublicReport(ResultSet rs) throws SQLException {

        CalculatedReportItem report = new CalculatedReportItem();
        report.setReportItemId(rs.getLong("id"));
        report.setCompositionCode(rs.getString("code"));
        report.setCompositionName(rs.getString("name"));
        report.setArtist(rs.getString("artist"));
        report.setQty(rs.getInt("totalQty"));
        String catalog = rs.getString("catalog");
        System.out.println("Catalog is " + catalog);
        report.setCatalog(catalog);
        report.setSharePublic(rs.getFloat("sharePublic"));
        return report;
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
        String url = "jdbc:mysql://" + host + ":" + port + "/" + base;

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
