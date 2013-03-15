package kz.bgm.base;

import kz.bgm.items.Track;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Executor {

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

    private static PreparedStatement allNamesState;
    private static PreparedStatement likeAllNamesState;


    public static Executor instance;
    //todo Андрюха тут пока не стал делать с финалом потомутчто хочу пока проперти сюда кидать


    Statement statement;
    Connection connection;

    public static Executor getInstance(Connection connection) {
        if (instance == null) {
            instance = new Executor(connection);
        }
        return instance;
    }

    private Executor() {

    }

    private Executor(Connection connection) {
        this.connection = connection;

        if (connection == null) {
            return;
        }

        try {

            allNamesState = connection.prepareStatement("select * from " +
                    QueryHolder.TABLE_ALL_MUSIC +
                    " where " + COLUMN_SONG_NAME + "= ? or " +
                    COLUMN_COMPOSER + "= ? or " +
                    COLUMN_ARTIST + "= ? or " +
                    COLUMN_PUBLISHER + "= ?");

            likeAllNamesState = connection.prepareStatement("select * from " +
                    QueryHolder.TABLE_ALL_MUSIC +
                    " where " + COLUMN_SONG_NAME + " like ? or " +
                    COLUMN_COMPOSER + " like ? or " +
                    COLUMN_ARTIST + " like ? or " +
                    COLUMN_PUBLISHER + " like ?");


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


    public List<Track> getTracksLikeAllNames(String value) {
        ResultSet resultSet = null;
        try {
            likeAllNamesState.setString(1, "%" + value + "%");
            likeAllNamesState.setString(2, "%" + value + "%");
            likeAllNamesState.setString(3, "%" + value + "%");
            likeAllNamesState.setString(4, "%" + value + "%");

            long start = System.currentTimeMillis();

            System.out.println("select from all name columns" + value);
            resultSet = likeAllNamesState.executeQuery();

            long stop = System.currentTimeMillis();
            System.out.println("Query done in " + (stop - start) / 1000F);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fromResultToTrack(resultSet);
    }

    public List<Track> getTracksByAllNames(String value) {
        ResultSet resultSet = null;
        try {
            allNamesState.setString(1, value);
            allNamesState.setString(2, value);
            allNamesState.setString(3, value);
            allNamesState.setString(4, value);

            long start = System.currentTimeMillis();

            System.out.println("select from all name columns" + value);
            resultSet = allNamesState.executeQuery();

            long stop = System.currentTimeMillis();
            System.out.println("Query done in " + (stop - start) / 1000F);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fromResultToTrack(resultSet);
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


    public void storeInBase(String table, List<Track> trackList) {
        try {
            if (connection == null) {
                System.out.println("connection is null");
                return;
            }

            Statement st = connection.createStatement();
            connection.setAutoCommit(false);

            System.out.println("inserting to table " + table +
                    " " + trackList.size() + " songs");

            String query = QueryHolder.queryInsertToSongTable(table);
            PreparedStatement ps = connection.prepareStatement(query);

            for (Track t : trackList) {

//                if (st == null) {
//                    System.out.println("Error, Statement is null");
//                    return;
//                }
                //todo check this
                ps.setString(1, t.getCode());
                ps.setString(2, t.getComposition());
                ps.setString(3, t.getMusicAuthors());
                ps.setString(4, t.getLyricsAuthors());
                ps.setString(5, t.getArtist());
                ps.setFloat(6, t.getMobileRate());//todo сокрящать цифры после запятой , не более 2-х
                ps.setFloat(7, t.getPublicRate());
                ps.addBatch();

//                st.addBatch(query);
            }

//            st.executeBatch();
            ps.executeBatch();
            connection.commit();
            System.out.println("Insert query done !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //todo нах убрать КвериХолдер и сделать что то типо нормального preparedStatmentHolder
    //todo а может хибернейт вылечит меня  ?
    public void storeInBaseAllMusic(String table, List<Track> trackList) {
        try {
            if (connection == null) {
                System.out.println("connection is null");
                return;
            }

            Statement st = connection.createStatement();
            connection.setAutoCommit(false);

            System.out.println("inserting to table " + table +
                    " " + trackList.size() + " songs");

            PreparedStatement ps = getPreparedStatementAllMusic(table, trackList);

//            st.executeBatch();
            ps.executeBatch();
            connection.commit();
            System.out.println("Insert query done !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement getPreparedStatementAllMusic(String table, List<Track> trackList) throws SQLException {
        String query = QueryHolder.queryInsertToAllMusic(table);
        PreparedStatement ps = connection.prepareStatement(query);

        for (Track t : trackList) {

            //todo check this
            ps.setString(1, t.getCode());
            ps.setString(2, t.getComposition());
            ps.setString(3, t.getAuthors());
            ps.setString(4, t.getArtist());//todo сокрящать цифры после запятой , не более 2-х
            ps.setFloat(5, t.getControlled_metch());
            ps.setFloat(6, t.getCollect_metch());
            ps.setString(7, t.getPublisher());
            ps.setString(8, t.getComment());

            ps.addBatch();

//                st.addBatch(query);
        }
        return ps;
    }


}
