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
                errorNoFileReport(out, jsonObj);
                return;
            }


            Long catalogId = getCatalogId(fields);
            FileItem item = getFileItem(fields);

            if (catalogId == null || item == null) {
//                resp.sendRedirect(RESULT_URL + "?er=no-file-reports-uploaded");
                errorNoFileReport(out, jsonObj);
                return;
            }

            String updateFileName = CATALOG_UPDATES_HOME + "/" + item.getName();
            File updateFile = new File(updateFileName);
            saveToFile(item, updateFile);

            CatalogUpdate update = new CatalogUpdate();
            update.setCatalogId(catalogId);
            update.setFilePath(updateFile.getAbsolutePath());
            update.setFileName(updateFile.getName());
            update.setEncoding("utf8");
            update.setSeparator(";");
            update.setEnclosedBy("\"");
            update.setNewline("\n");
            update.setFromLine(1);

            log.info("Got catalog updates " + item.getName());

//            log.debug("Resetting temp table");
//            storage.resetTempTrackTable();

            log.debug("Loading csv to temp table...");
            update = storage.updateCatalog(update);

            log.debug("Got result: " + update.getStatus());

            if (update.getStatus() == Status.HAS_WARNINGS) {
                int i = 0;
                JSONArray jsonAr = new JSONArray();
                for (UpdateWarning wrn : update.getWarnings()) {
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

//            resp.sendRedirect(RESULT_URL);
            jsonObj.put("id", update.getId());
            jsonObj.put("status", update.getStatus() == Status.OK ? "ok" : "warn");
            jsonObj.put("redirect", RESULT_URL + "?id=" + update.getId());
            jsonObj.writeJSONString(out);



        } catch (Exception e) {
            e.printStackTrace();
//            resp.sendRedirect(RESULT_URL + "?er=" + e.getMessage());
            jsonObj.put("status", "error");
            jsonObj.put("er", e.getMessage());
            jsonObj.writeJSONString(out);

        }
    }

    private void errorNoFileReport(PrintWriter out, JSONObject jsonObj) throws IOException {
        jsonObj.put("status", "error");
        jsonObj.put("er", "no-file-reports-uploaded");
        jsonObj.writeJSONString(out);
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


