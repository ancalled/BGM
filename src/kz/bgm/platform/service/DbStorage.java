package kz.bgm.platform.service;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import kz.bgm.platform.items.Track;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DbStorage implements CatalogStorage {


    public static final int MAX_STATEMENTS = 200;
    public static final int MAX_STATEMENTS_PER_CONNECTION = 10;
    public static final int MIN_POOL_SIZE = 10;
    public static final int MAX_POOL_SIZE = 50;

    private final ComboPooledDataSource pool;

    public DbStorage(String host, String port, String base, String user, String pass) {
        pool = connect(host, port, base, user, pass);
    }

    private ComboPooledDataSource connect(String host, String port, String base, String user, String pass) {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + base;
        System.out.println("Connecting to base " + base + " with user " + user);
        ComboPooledDataSource pool = new ComboPooledDataSource();
        try {
            configureConnectionPoll(user, pass, url, pool);

            System.out.println("Connected to base " + base + " by user " + user);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return pool;
    }

    private void configureConnectionPoll(String user, String pass, String url, ComboPooledDataSource pool) throws PropertyVetoException {
        pool.setDriverClass("com.mysql.jdbc.Driver");
        pool.setJdbcUrl(url);
        pool.setUser(user);
        pool.setPassword(pass);
        pool.setMinPoolSize(MIN_POOL_SIZE);
        pool.setMaxPoolSize(MAX_POOL_SIZE);
        pool.setMaxStatements(MAX_STATEMENTS);
        pool.setMaxStatementsPerConnection(MAX_STATEMENTS_PER_CONNECTION);
    }


    public void storeInCatalog(List<Track> trackList, boolean common) {
        Connection connection = null;
        try {
            connection = pool.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO " +
                    "allmusic(uid, song_name, composer, artist, " +
                    "controlled_mech_share, collect_mech_share, publisher, comment) " +
                    "VALUES (?,?,?,?,?,?,?,?)");


            for (Track t : trackList) {

                ps.setString(1, t.getCode());
                ps.setString(2, t.getComposition());
                ps.setString(3, t.getAuthors());
                ps.setString(4, t.getArtist());
                ps.setFloat(5, t.getControlled_metch());
                ps.setFloat(6, t.getCollect_metch());
                ps.setString(7, t.getPublisher());
                ps.setString(8, t.getComment());

                ps.addBatch();
            }

            connection.setAutoCommit(false);

            ps.executeBatch();

            connection.commit();

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

    @Override
    public List<Track> search(String value) {
        Connection connection = null;
        try {
            connection = pool.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM allmusic WHERE " +
                            "song_name LIKE ? OR " +
                            "composer LIKE ? OR " +
                            "artist LIKE ? OR " +
                            "publisher LIKE ?");

            stmt.setString(1, "%" + value + "%");
            stmt.setString(2, "%" + value + "%");
            stmt.setString(3, "%" + value + "%");
            stmt.setString(4, "%" + value + "%");

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
    public List<Track> searchBySongName(String songName) {
        Connection connection = null;
        try {
            connection = pool.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM allmusic WHERE song_name=?");
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
                    "SELECT * FROM allmusic WHERE artist=?");
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
                    "SELECT * FROM allmusic WHERE artist LIKE ?");
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
        track.setId(rs.getLong("id"));
        track.setCode(rs.getString("uid"));
        track.setComposition(rs.getString("song_name"));
        track.setAuthors(rs.getString("composer"));
        track.setArtist(rs.getString("artist"));
        track.setControlled_metch(rs.getFloat("controlled_mech_share"));
        track.setCollect_metch(rs.getFloat("collect_mech_share"));
        track.setPublisher(rs.getString("publisher"));
        track.setComment(rs.getString("comment"));
        return track;
    }

    public void closeConnection() {
        pool.close();
    }


}
