package kz.bgm.platform.service;

import kz.bgm.platform.items.Track;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbStorage implements CatalogStorage {


    private final Connection connection;

    public DbStorage(String host, String port, String base, String user, String pass) {
        this.connection = connect(host, port, base, user, pass);

    }

    private Connection connect(String host, String port, String base, String user, String pass) {
        Connection connection = null;
        try {
            System.out.println("Connecting to base " + base + " with user " + user);
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + "/" + base,
                    user,
                    pass);

            System.out.println("Connected to base by user " + user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


    public void storeInCatalog(List<Track> trackList, boolean common) {

        try {
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
        try {
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
        }

        return null;
    }

    @Override
    public List<Track> searchBySongName(String songName) {
        try {

            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM allmusic WHERE song_name=?");
            stmt.setString(1, songName);

            return parseTracks(stmt.executeQuery());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Track> searchByArtist(String artist) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM allmusic WHERE artist=?");
            stmt.setString(1, artist);

            ResultSet rs = stmt.executeQuery();

            return parseTracks(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public List<Track> searchByArtistLike(String artist) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM allmusic WHERE artist LIKE ?");
            stmt.setString(1, "%" + artist + "%");

            return parseTracks(stmt.executeQuery());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
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





}
