package kz.bgm.platform;

import kz.bgm.platform.service.CatalogFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BgmServer {

    public static final String APP_DIR = System.getProperty("user.dir");
    public static final String WEB_PROPS = APP_DIR + "/jetty.properties";

    public static final String DB_PROPS = APP_DIR + "/db.properties";

    public static final String PORT = "port";
    public static final String WEB_RESOURCE = "resource";

    private final Server jettyServer;


    public BgmServer(String propsName) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(propsName));

        int port = Integer.parseInt(props.getProperty(PORT));

        jettyServer = new Server(port);

        String res = props.getProperty(WEB_RESOURCE);

        WebAppContext webApp = new WebAppContext();
        webApp.setDescriptor(APP_DIR + "/" + res + "/WEB-INF/web.xml");
        webApp.setResourceBase(APP_DIR + "/" + res);
        webApp.setContextPath("/");
        jettyServer.setHandler(webApp);

    }

    public void start() throws Exception {
        jettyServer.start();
        jettyServer.join();
    }

    private static final String BASE_NAME = "base.name";
    private static final String BASE_LOGIN = "base.login";
    private static final String BASE_PASS = "base.pass";
    private static final String BASE_HOST = "base.host";
    private static final String BASE_PORT = "base.port";


    public static void initDatabase(String propsFile) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(propsFile));

        String dbHost = props.getProperty(BASE_HOST);
        String dbPort = props.getProperty(BASE_PORT);
        String dbName = props.getProperty(BASE_NAME);
        String dbLogin = props.getProperty(BASE_LOGIN);
        String dbPass = props.getProperty(BASE_PASS);

        System.out.println("Initializing data storage...");
        CatalogFactory.initDBStorage(dbHost, dbPort, dbName, dbLogin, dbPass);
    }

    public static void main(String[] args) throws Exception {

        initDatabase(DB_PROPS);

        BgmServer server = new BgmServer(WEB_PROPS);
        server.start();
    }

}
