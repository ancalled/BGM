package kz.bgm;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class BgmServer {

    public static final String APP_DIR = System.getProperty("user.dir");
    public static final String WEB_PROPS = APP_DIR + "/jetty.properties";

    public static final String DB_PROPS = APP_DIR + "/db.properties";

    public static final String PORT = "port";
    public static final String WEB_RESOURCE = "resource";


    public BgmServer(Properties property) throws Exception {
        Integer port = Integer.parseInt(property.getProperty(PORT));
        Server server = new Server((port));
        property.get(WEB_RESOURCE);

        WebAppContext webApp = new WebAppContext();
        webApp.setDescriptor(APP_DIR + "/" + property.get(WEB_RESOURCE) + "/WEB-INF/web.xml");
        webApp.setResourceBase(APP_DIR + "/" + property.get(WEB_RESOURCE));
        webApp.setContextPath("/");
        server.setHandler(webApp);
        server.start();
        server.join();
    }

    public static void main(String[] args) throws Exception {
        File file = new File(WEB_PROPS);
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));

        BgmServer je = new BgmServer(properties);

    }

}
