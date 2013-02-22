package kz.bgm.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseConnector {

    private String baseName = "bgm";
    private String baseLocation = "localhost";

    public Connection connect(String user, String pass) {
        return connect(baseName, user, pass);
    }

    public BaseConnector() {

    }

    public Connection connect(String base, String user, String pass) {
        Connection connection = null;
        try {
            System.out.println(
                    "Connecting to base " + base + " with user " + user);

            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + baseLocation + "/" + base,
                    user,
                    pass);

            System.out.println("Connected to base by user " + user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public void setBaseLocation(String baseLocation) {
        this.baseLocation = baseLocation;
    }

    public String getBaseName() {
        return baseName;
    }

    public String getBaseLocation() {
        return baseLocation;
    }

}
