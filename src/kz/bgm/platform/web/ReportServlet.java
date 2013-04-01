package kz.bgm.platform.web;

import kz.bgm.platform.ReportBuilder;
import kz.bgm.platform.items.ReportItem;
import kz.bgm.platform.items.Track;
import kz.bgm.platform.parsers.ReportParser;
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

public class ReportServlet extends HttpServlet {

    public static final String FILE = "file";

    public static final String APP_DIR = System.getProperty("user.dir");
    public static final String REPORT_DIR = APP_DIR + "/reports";

    public static final Logger log = Logger.getLogger(ReportServlet.class);
    public static final String REPORT = "report";

    private ReportParser reportParser;
    private FileItemFactory discFactory;
    private ServletFileUpload fileUploader;

    private CatalogStorage catalogService;

    @Override
    public void init() throws ServletException {
        discFactory = new DiskFileItemFactory();
        fileUploader = new ServletFileUpload(discFactory);

        reportParser = new ReportParser();

        catalogService = CatalogFactory.getStorage();
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

                    List<Track>trackList=new ArrayList<Track>();
        try {
            List<FileItem> files = fileUploader.parseRequest(req);
            if (files != null) {

                log.info("got" + files.size());
                for (FileItem file : files) {
                    log.info("File name:" + file.getName());
                    System.out.println("File size: " + file.getSize());

                    String fileName = REPORT_DIR + "/" + file.getName();
                    file.write(new File(fileName));

                   trackList= buildReport(fileName);
                }
            }

            log.info("No one files uploaded");

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        req.getSession().setAttribute(REPORT,trackList);

        resp.sendRedirect("report.jsp");
    }

    private List<Track> buildReport(String fileName) throws IOException, InvalidFormatException {
        List<ReportItem> reportList = reportParser.loadClientReport(fileName, 0);
        return ReportBuilder.buildMobileReport(catalogService, reportList);
    }

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        doPost(req, resp);

    }
}
