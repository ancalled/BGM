package kz.bgm.base;

import kz.bgm.items.Track;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ToBase extends BaseOperator {


    public ToBase(Connection connection) {
        super(connection);
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

            for (Track t : trackList) {
                String query = QueryHolder.queryInsertToSongTable(table, t);

                if (st == null) {
                    System.out.println("Error, Statement is null");
                    return;
                }

                st.addBatch(query);
            }

            st.executeBatch();
            connection.commit();

            System.out.println("Insert query done !");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
