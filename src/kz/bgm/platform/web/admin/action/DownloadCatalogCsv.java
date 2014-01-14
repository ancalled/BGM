package kz.bgm.platform.web.admin.action;

import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
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

public class DownloadCatalogCsv extends HttpServlet {

    private static final Logger log = Logger.getLogger(DownloadCatalogCsv.class);

    private CatalogStorage storage;


    public static final String TMP_CSV_PATH = "C:/tmp/";
    public static final String CSV_EXT = ".csv";
    private static final String CSV_PATH = "./web/catalog-csv";

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
    }


    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject jsonObj = new JSONObject();

        String catalogIdStr = req.getParameter("catalog_id");
        String catalogName = req.getParameter("catalog_name");
        if (catalogIdStr != null && catalogName != null) {
            try {
                int catalogId = Integer.parseInt(catalogIdStr);

                Path from = Paths.get(TMP_CSV_PATH + "/" +
                        catalogName + CSV_EXT);

                if (Files.exists(from)) {
                    try {
                        Files.delete(from);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Path to = Paths.get(CSV_PATH + "/" +
                        catalogName + CSV_EXT);

                if (Files.exists(to)) {
                    Files.delete(to);
                }

                storage.downloadCatalogInCsv(catalogId, from.toString());

                Files.copy(from, to);
                jsonObj.put("path", to.getFileName().toString());
                jsonObj.writeJSONString(out);
                return;
            } catch (NumberFormatException ne) {
                ne.printStackTrace();
                log.warn("Not valid catalog id");
            }
        }
        jsonObj.put("path", "error");
        jsonObj.writeJSONString(out);
    }

}
