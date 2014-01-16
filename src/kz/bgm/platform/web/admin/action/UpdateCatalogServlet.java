package kz.bgm.platform.web.admin.action;

import kz.bgm.platform.model.domain.CatalogUpdate;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.utils.TaskRunner;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

public class UpdateCatalogServlet extends HttpServlet {

    public static final String APP_HOME = System.getProperty("user.dir");
    public static final String CATALOG_UPDATES_HOME = APP_HOME + "/catalog-updates";

    public static final String VIEWS_HOME_URL = "../view";
    public static final String RESULT_URL = VIEWS_HOME_URL + "/catalog-update";


    private static final Logger log = Logger.getLogger(UpdateCatalogServlet.class);
    public static final String DEFAULT_ENCODING = "utf8";
    public static final String DEFAULT_FIELD_SEPARATOR = ";";
    public static final String DEFAULT_ENCLOSED_BY = "\"";
    public static final String DEFAULT_NEWLINE = "\n";
    public static final int DEFAULT_FROM_LINE = 1;

    private ServletFileUpload fileUploader;
    private CatalogStorage storage;
    private TaskRunner taskRunner;

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
        fileUploader = new ServletFileUpload(new DiskFileItemFactory());
        taskRunner = TaskRunner.getInstance();
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

            String catIdStr = getParam(fields, "catId");
            if (catIdStr == null) {
                errorNoFileReport(out, jsonObj);
                return;
            }

            Long catalogId = Long.parseLong(catIdStr);
            FileItem fileItem = getFileItem(fields);

            if (fileItem == null) {
//                resp.sendRedirect(RESULT_URL + "?er=no-file-reports-uploaded");
                errorNoFileReport(out, jsonObj);
                return;
            }

            String updateFileName = CATALOG_UPDATES_HOME + "/" + fileItem.getName();
            if (updateFileName.isEmpty()) {
                updateFileName = "tmp_file_" + new Random().nextInt(100000);
            }
            File updateFile = new File(updateFileName);
            saveToFile(fileItem, updateFile);

            CatalogUpdate update = new CatalogUpdate();
            update.setCatalogId(catalogId);
            update.setFilePath(updateFile.getAbsolutePath());
            update.setFileName(updateFile.getName());
            update.setEncoding(getParam(fields, "enc", DEFAULT_ENCODING));
            update.setSeparator(getParam(fields, "fs", DEFAULT_FIELD_SEPARATOR));
            update.setEnclosedBy(getParam(fields, "eb", DEFAULT_ENCLOSED_BY));
            update.setNewline(getParam(fields, "nl", DEFAULT_NEWLINE));
            update.setFromLine(Integer.parseInt(getParam(fields, "fl",
                    Integer.toString(DEFAULT_FROM_LINE))));

            log.info("Got catalog updates " + fileItem.getName());

//            log.debug("Resetting temp table");
//            storage.resetTempTrackTable();
            update = storage.saveCatalogUpdate(update);

            taskRunner.submit("update-task-" + update.getId(), new UpdateTask(update, storage));

//            log.debug("Loading csv to temp table...");
//            update = storage.updateCatalog(update);
//
//            log.debug("Got result: " + update.getStatus());
//
//            if (update.getStatus() == Status.HAS_WARNINGS) {
//                int i = 0;
//                JSONArray jsonAr = new JSONArray();
//                for (UpdateWarning wrn : update.getWarnings()) {
//                    JSONObject jsnWrn = new JSONObject();
//                    jsnWrn.put("number", wrn.getNumber());
//                    jsnWrn.put("column", wrn.getColumn());
//                    jsnWrn.put("row", wrn.getRow());
//                    jsnWrn.put("message", wrn.getMessage());
//                    jsonAr.add(jsnWrn);
//
//                    if (i++ < 10) {
//                        log.warn("#" + wrn.getRow() + " at " + wrn.getColumn() + ": " + wrn.getMessage());
//                    }
//                }
//
//                jsonObj.put("warningsList", jsonAr);
//            }
//
////            resp.sendRedirect(RESULT_URL);
//            jsonObj.put("id", update.getId());
//            jsonObj.put("status", update.getStatus() == Status.OK ? "ok" : "warn");
//            jsonObj.put("redirect", RESULT_URL + "?id=" + update.getId());
//            jsonObj.writeJSONString(out);


            jsonObj.put("status", "ok");
            jsonObj.put("uid", update.getId());
            jsonObj.writeJSONString(out);

        } catch (Exception e) {
            e.printStackTrace();
//            resp.sendRedirect(RESULT_URL + "?er=" + e.getMessage());
            jsonObj.put("status", "error");
            jsonObj.put("er", e.getMessage());
            jsonObj.writeJSONString(out);

        }
    }

    @SuppressWarnings("unchecked")
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



    private String getParam(List<FileItem> fields, String name) {
        return getParam(fields, name, null);
    }


    private String getParam(List<FileItem> fields, String name, String defaultValue) {
        for (FileItem item : fields) {
            if (item.isFormField()) {
                if (name.equals(item.getFieldName())) {
                    return item.getString();
                }
            }
        }

        return defaultValue;
    }


    private void saveToFile(FileItem item, File reportFile) throws Exception {
        log.info("Saving upload to " + reportFile.getAbsolutePath());

        if (!reportFile.getParentFile().exists()) {
            reportFile.getParentFile().mkdirs();
        }

        item.write(reportFile);
    }

    public static enum UpdateStatus {
        FILE_UPLOADED,
        SQL_LOAD_COMPLETE,
        UPDATE_STATISTICS_FINISHED
    }

    public static class UpdateTask extends TaskRunner.Task<UpdateStatus> {

        private final CatalogUpdate update;
        private final CatalogStorage storage;

        private UpdateTask(CatalogUpdate update, CatalogStorage storage) {
            this.update = update;
            this.storage = storage;
        }

        @Override
        public Object call() throws Exception {
            log.debug("Starting update catalog...");
            changeStatus(UpdateStatus.FILE_UPLOADED);
            CatalogUpdate updateResult = storage.loadCatalogUpdateIntoTmpTable(update);

            log.debug("Load complete, calc stats...");
            changeStatus(UpdateStatus.SQL_LOAD_COMPLETE);
            storage.caclCatalogUpdateStats(update.getId(), updateResult.getStatus());

            log.debug("Stat calc complete.");
            changeStatus(UpdateStatus.UPDATE_STATISTICS_FINISHED);
            return null;
        }
    }
}


