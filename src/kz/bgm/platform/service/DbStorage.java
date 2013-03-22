package kz.bgm.platform.service;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import kz.bgm.platform.items.Track;

import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.*;

public class DbStorage implements CatalogStorage {


    public static final int MAX_STATEMENTS = 200;
    public static final int MAX_STATEMENTS_PER_CONNECTION = 10;
    public static final int MIN_POOL_SIZE = 1;
    public static final int MAX_POOL_SIZE = 10;

    private final ComboPooledDataSource pool;

    public DbStorage(String host, String port,
                     String base, String user, String pass) {
        pool = initPool(host, port, base, user, pass);

        fillAllCatalogs();
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

            System.out.println(trackList.size() +
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
        catalogs = new HashMap<Integer, String>();
        try {
            connection = pool.getConnection();

            Statement st = connection.createStatement();

            ResultSet rs = st.executeQuery("SELECT *" +
                    " FROM catalog");

            while (rs.next()) {
                catalogs.put(rs.getInt("id"),
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
    public Track search(String author, String song) {
        return null;
    }

    @Override
    public Track search(String author, String song, boolean common) {
        return null;
    }

    private static Map<Integer, String> catalogs;

    @Override
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


    public Track searchById(String id) {
        Connection connection = null;
        try {
            connection = pool.getConnection();

            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM composition WHERE id=?");
            stmt.setString(1, id);

            Track tr = parseTrack(stmt.executeQuery());
            return tr;
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

    private static Track parseTrack(ResultSet rs) throws SQLException {
        Track track = new Track();
        track.setCatalog(catalogs.get(rs.getInt("catalog_id")));
        track.setId(rs.getLong("id"));
        track.setCode(rs.getString("code"));
        track.setName(rs.getString("name"));
        track.setArtist(rs.getString("artist"));
        track.setComposer(rs.getString("composer"));
        track.setMobileShare(rs.getFloat("shareMobile"));
        track.setPublicShare(rs.getFloat("sharePublic"));
        return track;
    }

    public void closeConnection() {
        pool.close();
    }


}
