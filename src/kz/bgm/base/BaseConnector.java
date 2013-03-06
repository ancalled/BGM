package kz.bgm.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseConnector {

    private String baseName = "bgm";
    private String baseLocation = "localhost";
    private String basePort = "3306";

    public Connection connect(String user, String pass) {
        return connect(baseLocation, basePort, baseName, user, pass);
    }

    public BaseConnector() {

    }

    public Connection connect(String host, String port, String base, String user, String pass) {
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
