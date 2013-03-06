package kz.bgm.base;

import kz.bgm.items.Track;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ToBase {

    Connection connection;

    public ToBase(Connection connection) {
        this.connection = connection;
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
