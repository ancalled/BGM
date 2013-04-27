package kz.bgm.platform;

import kz.bgm.platform.model.service.CatalogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
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
    private static final Logger log = Logger.getLogger(BgmServer.class);


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


        HandlerCollection handlers = new HandlerCollection();
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        handlers.setHandlers(new Handler[]{contexts, new DefaultHandler(), requestLogHandler});
        jettyServer.setHandler(handlers);

        NCSARequestLog requestLog = new NCSARequestLog("./logs/jetty.log");
        requestLog.setRetainDays(90);
        requestLog.setAppend(true);
        requestLog.setExtended(false);
        requestLog.setLogTimeZone("GMT");
        requestLogHandler.setRequestLog(requestLog);


        jettyServer.setHandler(requestLogHandler);
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

        log.info("Initializing data storage...");
        CatalogFactory.initDBStorage(dbHost, dbPort, dbName, dbLogin, dbPass);
    }

    public static void main(String[] args) throws Exception {
        DOMConfigurator.configure("log4j.xml");
        initDatabase(DB_PROPS);

        BgmServer server = new BgmServer(WEB_PROPS);
        server.start();
    }

}
