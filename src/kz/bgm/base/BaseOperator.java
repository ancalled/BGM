package kz.bgm.base;

import java.sql.Connection;
import java.sql.SQLException;

 public class BaseOperator {

    public Connection connection;

    BaseOperator(Connection connection) {
        this.connection = connection;
    }

    public void stopConnection() {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
