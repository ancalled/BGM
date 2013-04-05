package kz.bgm.platform.web;

import kz.bgm.platform.ReportBuilder;
import kz.bgm.platform.items.ReportItem;
import kz.bgm.platform.service.CatalogFactory;
import kz.bgm.platform.service.CatalogStorage;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerReportServlet extends HttpServlet {

    public static final String FILE = "file";

    public static final String APP_DIR = System.getProperty("user.dir");
    public static final String REPORT_DIR = APP_DIR + "/reports";

    public static final Logger log = Logger.getLogger(CustomerReportServlet.class);
    public static final String REPORT = "reportList";
    public static final String CLIENT_RATE = "client_rate";
    public static final String REPORT_PATH = "reportPath";

    private FileItemFactory discFactory;
    private ServletFileUpload fileUploader;

    private CatalogStorage catalogService;

    @Override
    public void init() throws ServletException {
        discFactory = new DiskFileItemFactory();
        fileUploader = new ServletFileUpload(discFactory);

        catalogService = CatalogFactory.getStorage();
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {


        List<ReportItem> reportList = null;


        //todo finish client rate

        float clientRate = 30;

        try {
            List<FileItem> files = fileUploader.parseRequest(req);
            List<String> reportsPaths = new ArrayList<String>();
            if (files != null && clientRate > 0) {

                log.info("got" + files.size());

                for (FileItem file : files) {
                    String filePath = saveFile(file);
                    log.info("Got client report " + filePath);
//                    reportList = buildReport(filePath, clientRate);
                    storeReport(filePath, clientRate);
                    reportsPaths.add("/reports/" + new File(filePath).getName());   //todo bad - remake
                }

            } else {
                log.info("No one files uploaded because files = " +
                        files + " clientRate = " + clientRate);
            }

        } catch (Exception e) {
            log.error(e.getMessage());     //todo make errors out
            resp.sendRedirect("result.jsp?r=error");
        }



        resp.sendRedirect("result.jsp?r=ok");
    }


    private String saveFile(FileItem file) throws Exception {
        log.info("File name:" + file.getName());
        System.out.println("File size: " + file.getSize());

        String filePath = REPORT_DIR + "/" + file.getName();

        File reportFile = new File(filePath);

        file.write(reportFile);
        return reportFile.getPath();
    }

    private static int fileCounter = 1;



    //todo finishhim this
    private List<ReportItem> buildReport(String fileName, float clientRate) throws IOException, InvalidFormatException {
        return ReportBuilder.buildMobileReport(catalogService, fileName, clientRate);
    }

    private void storeReport(String fileName, float clientRate) throws IOException, InvalidFormatException {
        ReportBuilder.storeCustomerReport(fileName, catalogService, "GSM Technologies");

        log.info("All Reports stored");

    }

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        doPost(req, resp);

    }
}
