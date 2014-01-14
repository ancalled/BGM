package kz.bgm.platform.web.admin.action;

import kz.bgm.platform.model.domain.Catalog;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.utils.Transliterator;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DownloadCatalogCsv extends HttpServlet {

    private static final Logger log = Logger.getLogger(DownloadCatalogCsv.class);

    public static final DateFormat STAMP_FORMAT = new SimpleDateFormat("yyyyMMddHHmmSS");
    public static final String DEFAULT_FIELD_TERMINATOR = ";";
    public static final String DEFAULT_ENCLOSED_BY = "'";
    public static final String DEFAULT_LINES_TERMINATOR = "\\n";

    private CatalogStorage storage;

    public static final String CSV_EXT = ".csv";
    private static final String USER_DIR = System.getProperty("user.dir");
    public static final String RESOURCES_PATH = "/catalog-csv";
    private static final String CSV_PATH = USER_DIR + "/web" + RESOURCES_PATH;



    private String tmpSqlPath;

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
        tmpSqlPath = getServletContext().getInitParameter("tmp-sql-file");
    }


    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject jsonObj = new JSONObject();

        String catIdStr = req.getParameter("cid");

        if (catIdStr != null) {
            try {
                int catId = Integer.parseInt(catIdStr);

                Catalog catalog = storage.getCatalog(catId);
                if (catalog == null) {
                    jsonObj.put("path", "error");
                    jsonObj.writeJSONString(out);
                    return;
                }

                String fieldTerminator = req.getParameter("ft");
                String enclosedBy = req.getParameter("eb");
                String linesTerminator = req.getParameter("lt");

                if (fieldTerminator == null) {
                    fieldTerminator = DEFAULT_FIELD_TERMINATOR;
                }

//                if (enclosedBy == null) {
//                    enclosedBy = DEFAULT_ENCLOSED_BY;
//                }

                if (linesTerminator == null) {
                    linesTerminator = DEFAULT_LINES_TERMINATOR;
                }

                log.info("Exporting catalog '" + catalog.getName() + "' to csv...");

                String catalogName = Transliterator.convert(catalog.getName());
                catalogName = catalogName.replace(" ", "_");

                String timestamp = STAMP_FORMAT.format(new Date());

                Path csvfile = Paths.get(tmpSqlPath + "/" + catalogName + "_" + timestamp + CSV_EXT);

//                if (Files.exists(csvfile)) {
//                    try {
//                        Files.delete(csvfile);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }


                Path outfile = Paths.get(CSV_PATH + "/" + catalogName + "_" + timestamp + CSV_EXT);

                if (Files.exists(outfile)) {
                    Files.delete(outfile);
                }

//                Files.createDirectories(outfile);


                storage.exportCatalogToCSV(catId, csvfile.toString(), fieldTerminator, enclosedBy, linesTerminator);

                log.info("Processed outfile: " + outfile);

                Files.copy(csvfile, outfile);
                long size = Files.size(outfile);

                jsonObj.put("path", RESOURCES_PATH + "/" + outfile.getFileName().toString());
                jsonObj.put("size", size);
                jsonObj.writeJSONString(out);
                return;

            } catch (Exception e) {
                log.error(e);
            }
        }
        jsonObj.put("path", "error");
        jsonObj.writeJSONString(out);
    }

}
