package kz.bgm.platform.web.admin.action;

import kz.bgm.platform.model.domain.CatalogUpdate;
import kz.bgm.platform.model.domain.UpdateWarning;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static kz.bgm.platform.model.domain.CatalogUpdate.Status;

public class UpdateCatalogServlet extends HttpServlet {

    public static final String APP_HOME = System.getProperty("user.dir");
    public static final String CATALOG_UPDATES_HOME = APP_HOME + "/catalog-updates";

    public static final String VIEWS_HOME_URL = "../view";
    public static final String RESULT_URL = VIEWS_HOME_URL + "/catalog-update";



    private static final Logger log = Logger.getLogger(UpdateCatalogServlet.class);

    private ServletFileUpload fileUploader;
    private CatalogStorage storage;

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
        fileUploader = new ServletFileUpload(new DiskFileItemFactory());
    }


    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject jsonObj = new JSONObject();

        try {
            List<FileItem> fields = fileUploader.parseRequest(req);
            if (fields == null) {
//                resp.sendRedirect(RESULT_URL + "?er=no-file-reports-uploaded");
                log.warn("No multipart fields found");
                jsonObj.put("status", "error");
                jsonObj.put("er", "no-file-reports-uploaded");
                jsonObj.writeJSONString(out);

                return;
            }


            Long catalogId = getCatalogId(fields);
            FileItem item = getFileItem(fields);

            if (catalogId == null || item == null) {
//                resp.sendRedirect(RESULT_URL + "?er=no-file-reports-uploaded");
                jsonObj.put("status", "error");
                jsonObj.put("er", "no-file-reports-uploaded");
                jsonObj.writeJSONString(out);
                return;
            }

            String updateFileName = CATALOG_UPDATES_HOME + "/" + item.getName();
            File updateFile = new File(updateFileName);
            saveToFile(item, updateFile);

            log.info("Got catalog updates " + item.getName());

            log.debug("Resetting temp table");
            storage.resetTempTrackTable();

            log.debug("Loading csv to temp table...");
            CatalogUpdate res = storage.saveCatalogUpdate(updateFileName, catalogId);
            res.setFile(updateFile.getName());

            log.debug("Got result: " + res.getStatus());

            if (res.getStatus() == Status.HAS_WARNINGS) {
                int i = 0;
                JSONArray jsonAr = new JSONArray();
                for (UpdateWarning wrn : res.getWarnings()) {
                    JSONObject jsnWrn = new JSONObject();
                    jsnWrn.put("number", wrn.getNumber());
                    jsnWrn.put("column", wrn.getColumn());
                    jsnWrn.put("row", wrn.getRow());
                    jsnWrn.put("message", wrn.getMessage());
                    jsonAr.add(jsnWrn);

                    if (i++ < 10) {
                        log.warn("#" + wrn.getRow() + " at " + wrn.getColumn() + ": " + wrn.getMessage());
                    }
                }

                jsonObj.put("warningsList", jsonAr);
            }

            HttpSession ses = req.getSession(true);
            ses.setAttribute("catalog-update", res);

//            resp.sendRedirect(RESULT_URL);
            jsonObj.put("status", res.getStatus() == Status.OK ? "ok" : "warn");
            jsonObj.put("redirect", RESULT_URL);
            jsonObj.writeJSONString(out);


        } catch (Exception e) {
            e.printStackTrace();
//            resp.sendRedirect(RESULT_URL + "?er=" + e.getMessage());
            jsonObj.put("status", "error");
            jsonObj.put("er", e.getMessage());
            jsonObj.writeJSONString(out);

        }
    }


    private FileItem getFileItem(List<FileItem> fields) {
        for (FileItem item : fields) {
            if (!item.isFormField()) {
                return item;
            }
        }

        return null;
    }

    private Long getCatalogId(List<FileItem> fields) {
        for (FileItem item : fields) {
            if (item.isFormField()) {
                if ("catId".equals(item.getFieldName())) {
                    return Long.parseLong(item.getString());
                }
            }
        }

        return null;
    }


    private void saveToFile(FileItem item, File reportFile) throws Exception {
        log.info("Saving upload to " + reportFile.getAbsolutePath());

        if (!reportFile.getParentFile().exists()) {
            reportFile.getParentFile().mkdirs();
        }

        item.write(reportFile);
    }

}


