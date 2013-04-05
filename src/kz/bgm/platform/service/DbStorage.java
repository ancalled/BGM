package kz.bgm.platform.service;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import kz.bgm.platform.items.Customer;
import kz.bgm.platform.items.CustomerReport;
import kz.bgm.platform.items.CustomerReportItem;
import kz.bgm.platform.items.Track;
import kz.bgm.platform.search.IdSearcher;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.*;

public class DbStorage implements CatalogStorage {

    private static final Logger log = Logger.getLogger(DbStorage.class);

    public static final int MAX_STATEMENTS = 200;
    public static final int MAX_STATEMENTS_PER_CONNECTION = 10;
    public static final int MIN_POOL_SIZE = 1;
    public static final int MAX_POOL_SIZE = 10;

    private final ComboPooledDataSource pool;
    private IdSearcher idSearcher;

    private static Map<Integer, String> catalogMap;

    private static final int RESULT_SIZE = 150;

    public DbStorage(String host, String port,
                     String base, String user, String pass) {
        pool = initPool(host, port, base, user, pass);

        fillAllCatalogs();
        //todo make indexing on  lucen
        idSearcher = new IdSearcher();

//        Connection connection = null;
//
//        try {
//            connection = pool.getConnection();
//            idSearcher.init(connection);
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        } finally {
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
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


    public void storeInCatalog(List<Track> trackList, String catalog) {
        Connection connection = null;
        try {
            int catId = getCatalogId(catalog);

            long startTime = System.currentTimeMillis();

            connection = pool.getConnection();
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO " +
                            "composition(catalog_id, code, name, artist, " +
                            "composer,shareMobile,sharePublic) " +
                            "VALUES (?,?,?,?,?,?,?)");


            for (Track t : trackList) {

                ps.setInt(1, catId);
                ps.setString(2, t.getCode());
                ps.setString(3, t.getName());
                ps.setString(4, t.getArtist());
                ps.setString(5, t.getComposer());
                ps.setFloat(6, t.getMobileShare());
                ps.setFloat(7, t.getPublicShare());

                ps.addBatch();
            }

            connection.setAutoCommit(false);
            ps.executeBatch();

            connection.commit();
            long endTime = System.currentTimeMillis();
            long doneTime = (endTime - startTime) / 1000;

            log.info(trackList.size() +
                    " tracks inserted in " + doneTime + " sec");

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


    private int fillAllCatalogs() {
        Connection connection = null;
        catalogMap = new HashMap<Integer, String>();
        try {
            connection = pool.getConnection();

            Statement st = connection.createStatement();

            ResultSet rs = st.executeQuery("SELECT *" +
                    " FROM catalog");

            while (rs.next()) {
                catalogMap.put(rs.getInt("id"),
                        rs.getString("name"));
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


    private int getCatalogId(String catalogName) {
        Connection connection = null;
        try {
            connection = pool.getConnection();
            PreparedStatement ps =
                    connection.prepareStatement("SELECT id" +
                            " FROM catalog WHERE name=?");

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
    public void addItem(Track track, boolean common) {
    }

    @Override
    public Track search(String artist, String song) {
        Connection connection = null;

        try {

            connection = pool.getConnection();

            List<String> idList = idSearcher.search(artist, song);

            List<Track> trackList = new ArrayList<Track>();

            for (int k = 0; k < idList.size() && k < RESULT_SIZE; k++) {
                Track track = searchById(connection, idList.get(k));
                trackList.add(track);

            }
            if (trackList.size() > 0) {
                return trackList.get(0);
            } else {
                return null;
            }

        } catch (Exception e) {
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

    @Override
    public Track search(String author, String song, boolean common) {
        return null;
    }


    public List<Track> search(String value, boolean withLucen) {
        if (withLucen) {
            return searchWithLucen(value);
        } else {
            return search(value);
        }
    }


    public List<Track> searchWithLucen(String value) {
        Connection connection = null;
        try {

            connection = pool.getConnection();

            if (connection == null) {
                return null;
            }

            List<String> idList = idSearcher.search(value);

            List<Track> trackList = new ArrayList<Track>();

            for (int k = 0; k < idList.size() && k < RESULT_SIZE; k++) {
                Track track = searchById(connection, idList.get(k));
                trackList.add(track);
            }

            return trackList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return Collections.emptyList();
    }

    public List<Track> search(String value) {
        Connection connection = null;
        try {
            connection = pool.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
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

            return parseTracks(rs);

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

        return Collections.emptyList();
    }

    @Override
    public void insertCustomerReportItem(List<CustomerReportItem> reportItemList) {
        Connection connection = null;
        try {
            log.info("inserting customer report items " + reportItemList.size() + " size");

            connection = pool.getConnection();
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO " +
                            "customer_report_item(report_id,composition_id,name," +
                            "artist,content_type,qty,price) " +
                            "VALUES (?,?,?,?,?,?,?)");


            for (CustomerReportItem cr : reportItemList) {
                ps.setInt(1, cr.getReportId());
                ps.setInt(2, cr.getCompositionId());
                ps.setString(3, cr.getName());
                ps.setString(4, cr.getArtist());
                ps.setString(5, cr.getContentType());
                ps.setInt(6, cr.getQty());
                ps.setFloat(7, cr.getPrice());

                ps.addBatch();
            }

            connection.setAutoCommit(false);
            ps.executeBatch();
            connection.commit();

            log.info("insert done");
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


    @Override
    public int insertCustomerReport(CustomerReport report) {
        Connection connection = null;
        try {
            log.info("inserting customer report ");
            connection = pool.getConnection();

            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO " +
                            "customer_report(customer_id,order_date,download_date) " +
                            "VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, report.getCustomerId());
            ps.setDate(2, report.getOrderDate());
            ps.setDate(3, report.getDownloadDate());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();

            log.info("insert done");
            rs.next();


            return rs.getInt(1);


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


    public Track searchById(Connection connection, String id) {

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM composition WHERE id=?");

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            Track track = null;

            while (rs.next()) {
                track = parseTrack(rs);
            }

            return track;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<Track> searchBySongName(String songName) {
        Connection connection = null;
        try {
            connection = pool.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM composition WHERE name=?");
            stmt.setString(1, songName);

            return parseTracks(stmt.executeQuery());

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

        return Collections.emptyList();
    }

    @Override
    public List<Track> searchByCode(String code) {
        Connection connection = null;
        try {
            connection = pool.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM composition WHERE code=?");
            stmt.setString(1, code);

            return parseTracks(stmt.executeQuery());

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

        return Collections.emptyList();
    }

    @Override
    public List<Track> searchByComposer(String composer) {
        Connection connection = null;
        try {
            connection = pool.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM composition WHERE composer=?");
            stmt.setString(1, composer);

            return parseTracks(stmt.executeQuery());

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
        return Collections.emptyList();
    }

    @Override
    public List<Track> searchByArtist(String artist) {
        Connection connection = null;
        try {
            connection = pool.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM composition WHERE artist=?");
            stmt.setString(1, artist);

            ResultSet rs = stmt.executeQuery();

            return parseTracks(rs);

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

        return Collections.emptyList();
    }

    @Override
    public Customer getCustomer(String name) {
        Connection connection = null;
        try {
            connection = pool.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM customer WHERE name=?");
            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();

            rs.next();
            return parseCustomer(rs);

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

    @Override
    public Customer getCustomer(int id) {

        Connection connection = null;
        try {
            connection = pool.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM composition WHERE id=?");
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            rs.next();
            return parseCustomer(rs);

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


    @Override
    public List<Track> searchByArtistLike(String artist) {
        Connection connection = null;
        try {
            connection = pool.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM composition WHERE artist LIKE ?");
            stmt.setString(1, "%" + artist + "%");

            return parseTracks(stmt.executeQuery());

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
        return Collections.emptyList();
    }

    private static List<Track> parseTracks(ResultSet rs) throws SQLException {
        if (rs == null) return null;

        List<Track> tracks = new ArrayList<Track>();
        while (rs.next()) {
            Track track = parseTrack(rs);
            tracks.add(track);
        }
        return tracks;
    }

    public String getCatalogTitle(int catID) {
        return catalogMap.get(catID);
    }

    private static Track parseTrack(ResultSet rs) throws SQLException {
        Track track = new Track();
        track.setCatalog(catalogMap.get(rs.getInt("catalog_id")));
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
        customer.setId(rs.getInt("id"));
        customer.setName(rs.getString("name"));
        customer.setRightType(rs.getString("right_type"));
        customer.setRoyalty(rs.getFloat("royalty"));
        return customer;
    }

    @Override
    public Float getRoyalty(int catalogId) {
        Connection connection = null;
        try {
            connection = pool.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT royalty FROM catalog WHERE id=?");
            stmt.setInt(1, catalogId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rs.getString("royalty");
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
        return 0f;
    }


    public void closeConnection() {
        pool.close();
    }


}
