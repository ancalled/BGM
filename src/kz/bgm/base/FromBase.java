package kz.bgm.base;

import kz.bgm.items.Track;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FromBase extends BaseOperator {

    Statement statement;

    public FromBase(Connection connection) {
        super(connection);
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Track> getAllTracksCom() {
        List<Track> pmiTrackList = getAllTracksFromTable(QueryHolder.TABLE_PMI_SONGS_COM);
        List<Track> nmiTrackList = getAllTracksFromTable(QueryHolder.TABLE_NMI_SONGS_COM);
        pmiTrackList.addAll(nmiTrackList);

        return pmiTrackList;
    }

    public List<Track> getAllTracksPub() {
        List<Track> pmiList = getAllTracksFromTable(QueryHolder.TABLE_PMI_SONGS);
        List<Track> nmiList = getAllTracksFromTable(QueryHolder.TABLE_NMI_SONGS);
        List<Track> wrList = getAllTracksFromTable(QueryHolder.TABLE_WRCH_SONGS);
        pmiList.addAll(nmiList);
        pmiList.addAll(wrList);
        return pmiList;
    }

    public List<Track> getAllTracksFromTable(String tableName) {
        List<Track> trackList = Collections.emptyList();
        try {
            if (connection.isClosed()) {
                System.out.println("connection is closed");
                return trackList;
            }
            String selectQuery = QueryHolder.queryAllSongsByTable(tableName);
            System.out.println(selectQuery);


            ResultSet resultSet = statement.executeQuery(selectQuery);
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
                track.setCode(resultSet.getString(2));
                track.setComposition(resultSet.getString(3));
                track.setArtist(resultSet.getString(4));
                track.setMusicAuthors(resultSet.getString(5));
                track.setLyricsAuthors(resultSet.getString(6));
                track.setMobileRate(resultSet.getFloat(7));
                track.setPublicRate(resultSet.getFloat(8));
//                System.out.println("==============================");
//                System.out.println(track);
//                System.out.println("==============================");
                trackList.add(track);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trackList;

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
