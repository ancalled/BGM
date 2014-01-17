package kz.bgm.platform.web.admin.action;

import kz.bgm.platform.model.domain.CatalogUpdate;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.utils.JsonUtils;
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
                log.warn("No multipart fields found");
                errorNoFileReport(out, jsonObj);
                return;
            }

            String catIdStr = JsonUtils.getParam(fields, "catId");
            if (catIdStr == null) {
                errorNoFileReport(out, jsonObj);
                return;
            }

            Long catalogId = Long.parseLong(catIdStr);
            FileItem fileItem = getFileItem(fields);

            if (fileItem == null) {
                errorNoFileReport(out, jsonObj);
                return;
            }

            String updateFilePath = CATALOG_UPDATES_HOME + "/" + fileItem.getName();
            if (updateFilePath.isEmpty()) {
                updateFilePath = "tmp_file_" + new Random().nextInt(100000);
            }
            File updateFile = new File(updateFilePath);
            saveToFile(fileItem, updateFile);

            CatalogUpdate update = new CatalogUpdate();
            update.setCatalogId(catalogId);
            update.setFilePath(updateFile.getAbsolutePath());
            update.setFileName(updateFile.getName());
            update.setEncoding(JsonUtils.getParam(fields, "enc", DEFAULT_ENCODING));
            update.setSeparator(JsonUtils.getParam(fields, "fs", DEFAULT_FIELD_SEPARATOR));
            update.setEnclosedBy(JsonUtils.getParam(fields, "eb", DEFAULT_ENCLOSED_BY));
            update.setNewline(JsonUtils.getParam(fields, "nl", DEFAULT_NEWLINE));
            update.setFromLine(Integer.parseInt(JsonUtils.getParam(fields, "fl",
                    Integer.toString(DEFAULT_FROM_LINE))));

            log.info("Got catalog updates " + fileItem.getName());

            update = storage.saveCatalogUpdate(update);

            taskRunner.submit("update-task-" + update.getId(), new UpdateTask(update, storage));

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
            CatalogUpdate updateResult = storage.importCatalogUpdate(update);

            log.debug("Load complete, calc stats...");
            changeStatus(UpdateStatus.SQL_LOAD_COMPLETE);
            storage.calculateCatalogUpdateStats(update.getId(), updateResult.getStatus());

            log.debug("Stat calc complete.");
            changeStatus(UpdateStatus.UPDATE_STATISTICS_FINISHED);
            return null;
        }
    }
}


