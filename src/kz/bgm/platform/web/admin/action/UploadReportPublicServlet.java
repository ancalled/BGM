package kz.bgm.platform.web.admin.action;

import kz.bgm.platform.model.domain.CustomerReport;
import kz.bgm.platform.model.domain.CustomerReportItem;
import kz.bgm.platform.model.domain.SearchResult;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.model.service.LuceneSearch;
import kz.bgm.platform.utils.ReportParser;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploadReportPublicServlet extends HttpServlet {

    public static final Logger log = Logger.getLogger(UploadReportPublicServlet.class);

    public static final String APP_HOME = System.getProperty("user.dir");
    public static final String REPORTS_HOME = APP_HOME + "/reports";

    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");


    public static final String FILE = "file";

    private ServletFileUpload fileUploader;
    private CatalogStorage catalogService;
    private LuceneSearch luceneSearch;

    @Override
    public void init() throws ServletException {
        fileUploader = new ServletFileUpload(new DiskFileItemFactory());
        catalogService = CatalogFactory.getStorage();
        luceneSearch = LuceneSearch.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
//            String dateParam = req.getParameter("dt");
//            Date reportDate = dateParam != null ? FORMAT.parse(dateParam) : new Date();

            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            Date reportDate = format.parse("2012/01/01");

//            String periodParam = req.getParameter("per");
//            int per = periodParam != null ? Integer.parseInt(periodParam) : 0;
//            CustomerReport.Period period = periodParam != null ?
//                    CustomerReport.Period.values()[per] :
//                    CustomerReport.Period.MONTH;

            CustomerReport.Period period = CustomerReport.Period.MONTH;
            List<FileItem> files = fileUploader.parseRequest(req);

            if (files == null) {
                resp.sendRedirect("/admin/reports.html?er=no-freports-uploaded");
                return;
            }


            List<CustomerReportItem> allItems = new ArrayList<>();
            for (FileItem item : files) {

                String reportFile = REPORTS_HOME + "/" + item.getName();
                saveToFile(item, reportFile);

                log.info("Got client report " + item.getName());

                List<CustomerReportItem> items = ReportParser.parsePublicReport(reportFile);
                allItems.addAll(items);
            }

            Date now = new Date();

            CustomerReport report = new CustomerReport();
            report.setStartDate(reportDate);
            report.setPeriod(period);
            report.setUploadDate(now);
            report.setType(CustomerReport.Type.PUBLIC);
            report.setTracks(allItems.size());

            long reportId = catalogService.saveCustomerReport(report);
            report.setId(reportId);

            int detected = 0;

            for (CustomerReportItem i : allItems) {
                i.setReportId(reportId);
                List<SearchResult> ids = luceneSearch.search(i.getArtist(), null, i.getTrack(), 100);
                if (ids.size() > 0) {
                    i.setCompositionId(ids.get(0).getTrackId());
                    detected++;
                }
            }
            log.info("composition detected " + detected);

            report.setDetected(detected);

            catalogService.saveCustomerReportItems(allItems);

            HttpSession ses = req.getSession(true);
            ses.setAttribute("report-" + reportId, report);

            resp.sendRedirect("/admin/view/report-upload-result?rid=" + reportId);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("/admin/reports.html?er=" + e.getMessage());

        }
    }


    private void saveToFile(FileItem item, String filename) throws Exception {
        log.info("File name:" + item.getName());
        File reportFile = new File(filename);
        item.write(reportFile);
    }


}
