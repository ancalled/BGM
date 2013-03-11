package kz.bgm.base;

import kz.bgm.items.Track;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FromBase {

    public static final String COLUMN_SONG_NAME = "song_name";
    public static final String COLUMN_UID = "uid";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MOBILE_RATE = "mobile_rate";
    public static final String COLUMN_PUBLIC_RATE = "public_rate";
    public static final String COLUMN_COMPOSER = "composer";
    public static final String COLUMN_ARTIST = "artist";
    public static final String COLUMN_PUBLISHER = "publisher";
    public static final String COLUMN_COMMENT = "comment";
    public static final String CONTROLLED_MECH_SHARE = "controlled_mech_share";
    public static final String COLLECT_MECH_SHARE = "collect_mech_share";

    Statement statement;
    Connection connection;

    public FromBase(Connection connection) {
        try {
            this.connection = connection;
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Track> getTracksLikeSongName(String songName) {
        return getTracksLike(COLUMN_SONG_NAME, songName);
    }

    public List<Track> getTracksLikeArtist(String artist) {
        return getTracksLike(COLUMN_ARTIST, artist);
    }

    public List<Track> getTracksByArtist(String artist) {
        return getTracks(COLUMN_ARTIST, artist);
    }

    public List<Track> getTracksEqSongName(String songName) {
        return getTracks(COLUMN_SONG_NAME, songName);
    }

    public List<Track> getTracksByUid(String songName) {
        return getTracksLike(COLUMN_UID, songName);
    }


    public List<Track> getTracks(String column, Object value) {
        try {
            if (connection.isClosed()) {
                System.out.println("connection is closed");
                return null;
            }
            long start = System.currentTimeMillis();
            System.out.println("select * from " +
                    QueryHolder.TABLE_ALL_MUSIC +
                    " where " + column + "=" + value + "' executing");

            PreparedStatement ps =
                    connection.prepareStatement(
                            "select * from " +
                                    QueryHolder.TABLE_ALL_MUSIC +
                                    " where " + column + "= ?");

            if (column.equals(COLLECT_MECH_SHARE) ||
                    column.equals(CONTROLLED_MECH_SHARE) ||
                    column.equals(COLUMN_MOBILE_RATE) ||
                    column.equals(COLUMN_PUBLIC_RATE)) {

                ps.setFloat(1, (Float) value);

            } else if (column.equals(COLUMN_ID)) {
                ps.setLong(1, (Long) value);

            } else {
                ps.setString(1, (String) value);
            }

            ResultSet resultSet = ps.executeQuery();
            long stop = System.currentTimeMillis();
            System.out.println("Query done in " + (stop - start) / 1000F);
            return fromResultToTrack(resultSet);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return Collections.emptyList();
    }


    public List<Track> getTracksLike(String column, Object value) {
        try {
            if (connection.isClosed()) {
                System.out.println("connection is closed");
                return null;
            }
            long start = System.currentTimeMillis();
            System.out.println("select * from " +
                    QueryHolder.TABLE_ALL_MUSIC +
                    " where " + column + " like '" + value + "' executing");

            PreparedStatement ps =
                    connection.prepareStatement(
                            "select * from " +
                                    QueryHolder.TABLE_ALL_MUSIC +
                                    " where " + column + " like ?");

            if (column.equals(COLLECT_MECH_SHARE) ||
                    column.equals(CONTROLLED_MECH_SHARE) ||
                    column.equals(COLUMN_MOBILE_RATE) ||
                    column.equals(COLUMN_PUBLIC_RATE)) {

                ps.setFloat(1, (Float) value);

            } else if (column.equals(COLUMN_ID)) {
                ps.setLong(1, (Long) value);

            } else {
                ps.setString(1, "%" + value + "%");
            }

            ResultSet resultSet = ps.executeQuery();
            long stop = System.currentTimeMillis();
            System.out.println("Query done in " + (stop - start) / 1000F);
            return fromResultToTrack(resultSet);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return Collections.emptyList();
    }


    //-----------------------------OLD-------------------------------------------
    public List<Track> getAllTracksFromTable(String tableName) {
        List<Track> trackList = Collections.emptyList();
        try {
            if (connection.isClosed()) {
                System.out.println("connection is closed");
                return trackList;
            }
            PreparedStatement ps = connection.prepareStatement("select * from " + tableName);

            ResultSet resultSet = ps.executeQuery();
            trackList = fromResultToTrack(resultSet);

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return trackList;
    }

    public List<Track> getTrackFromBase(String songName) {
        List<Track> trackList = Collections.emptyList();
        try {
            if (connection.isClosed()) {
                System.out.println("Connection is closed");
                return trackList;
            }
            connection.setAutoCommit(false);
            String query = QueryHolder.
                    makeQueryListOfSongs(songName);
            System.out.println(query);

            long startMils = System.currentTimeMillis();

            ResultSet resultSet = statement.executeQuery(query);
            trackList = fromResultToTrack(resultSet);

            long stopMils = System.currentTimeMillis();

            System.out.println("Select query done in " + (stopMils - startMils) / 1000F);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trackList;
    }

    private List<Track> fromResultToTrack(ResultSet resultSet) {
        if (resultSet == null) {
            return null;
        }
        List<Track> trackList = new ArrayList<Track>();
        try {
            while (resultSet.next()) {
                Track track = new Track();
                track.setId(resultSet.getLong(COLUMN_ID));
                track.setCode(resultSet.getString(COLUMN_UID));
                track.setComposition(resultSet.getString(COLUMN_SONG_NAME));
                track.setAuthors(resultSet.getString(COLUMN_COMPOSER));
                track.setArtist(resultSet.getString(COLUMN_ARTIST));
                track.setControlled_metch(resultSet.getFloat(CONTROLLED_MECH_SHARE));
                track.setCollect_metch(resultSet.getFloat(COLLECT_MECH_SHARE));
                track.setPublisher(resultSet.getString(COLUMN_PUBLISHER));
                track.setComment(resultSet.getString(COLUMN_COMMENT));
                trackList.add(track);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trackList;
    }

    public String getAuthorsByTitle(String title) {
        StringBuilder stb = null;
        try {
            if (connection.isClosed()) {
                System.out.println("Connection is closed");
                return null;
            }

            String query = QueryHolder.makeQueryAuthorsByTitleFromAllBase();

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, "%" + title + "%");
            ps.setString(2, "%" + title + "%");
            ps.setString(3, "%" + title + "%");
            ps.setString(4, "%" + title + "%");
            ps.setString(5, "%" + title + "%");


            ResultSet resultSet = ps.executeQuery();
            boolean nothing = resultSet.next();
            if (!nothing) {
                return null;
            }
            stb = new StringBuilder();

            String authors = "not found";
            String lyrAuth = resultSet.getString(1);
            String musAuth = resultSet.getString(2);

            if (lyrAuth == null) {
                lyrAuth = authors;
            }
            if (musAuth == null) {
                musAuth = authors;
            }

            if (lyrAuth.equals(musAuth)) {
                stb.append(lyrAuth);
            } else {
                stb.append(lyrAuth).append("/").append(musAuth);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return stb.toString();
    }

    public String getAuthorsByUid(String uid) {
        StringBuilder stb = null;
        try {
            if (connection.isClosed()) {
                System.out.println("Connection is closed");
                return null;
            }

            String query = QueryHolder.makeQueryAuthorsByUidFromAllBase();

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, uid);
            ps.setString(2, uid);
            ps.setString(3, uid);
            ps.setString(4, uid);
            ps.setString(5, uid);


            ResultSet resultSet = ps.executeQuery();
            boolean nothing = resultSet.next();
            if (!nothing) {
                return null;
            }
            stb = new StringBuilder();

            String lyrAuth = resultSet.getString(1);
            String musAuth = resultSet.getString(2);

            if (lyrAuth == null) {
                lyrAuth = "";
            }
            if (musAuth == null) {
                musAuth = "";
            }

            if (lyrAuth.equals(musAuth)) {
                stb.append(lyrAuth);
            } else {
                stb.append(lyrAuth).append("/").append(musAuth);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        System.out.println("Code " + uid + " | " + stb.toString());
        return stb.toString();
    }


    private ResultSet getResult(List<String> queryList)
            throws SQLException {
        ResultSet resultSet = null;
        for (String q : queryList) {
            resultSet = statement.executeQuery(q);
            if (resultSet.next()) {
                return resultSet;
            }
        }
        return resultSet;

    }
}
