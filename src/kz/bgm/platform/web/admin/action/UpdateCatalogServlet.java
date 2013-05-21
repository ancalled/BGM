package kz.bgm.platform.web.admin.action;

import kz.bgm.platform.model.domain.CatalogUpdate;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class UpdateCatalogServlet extends HttpServlet {

    public static final String APP_HOME = System.getProperty("user.dir");
    public static final String CATALOG_UPDATES_HOME = APP_HOME + "/catalog-updates";


    private static final Logger log = Logger.getLogger(UpdateCatalogServlet.class);

    private ServletFileUpload fileUploader;
    private CatalogStorage storage;

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
        fileUploader = new ServletFileUpload(new DiskFileItemFactory());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            List<FileItem> fields = fileUploader.parseRequest(req);
            if (fields == null) {
                resp.sendRedirect("../view/catalog-update-result?er=no-file-reports-uploaded");
                log.warn("No multipart fields found");
                return;
            }


            Long catalogId = getCatalogId(fields);
            FileItem item = getFileItem(fields);

            if (catalogId == null || item == null) {
                resp.sendRedirect("../view/catalog-update-result?er=no-file-reports-uploaded");
                return;
            }

            String updateFileName = CATALOG_UPDATES_HOME + "/" + item.getName();
            File updateFile = new File(updateFileName);
            saveToFile(item, updateFile);

            log.info("Got catalog updates " + item.getName());

            log.debug("Resetting temp table");
            storage.resetTempTrackTable();

            log.debug("Loading csv to temp table...");
            CatalogUpdate res = storage.loadCatalog(updateFileName, catalogId);
            res.setFile(updateFile.getName());

            log.debug("Got result: " + res.getStatus());

            if (res.getStatus() == CatalogUpdate.Status.HAS_ERRORS) {
                int i = 0;
                for (String er : res.getErrors()) {
                    log.warn(er);
                    if (i++ > 10) break;
                }
            }

            HttpSession ses = req.getSession(true);
            ses.setAttribute("catalog-upload-result", res);

            resp.sendRedirect("../view/catalog-update-result");


        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("../view/catalog-update-result?er=" + e.getMessage());

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
        log.info("File name:" + item.getName());

        if (!reportFile.getParentFile().exists()) {
            reportFile.getParentFile().mkdirs();
        }

        item.write(reportFile);
    }

}


