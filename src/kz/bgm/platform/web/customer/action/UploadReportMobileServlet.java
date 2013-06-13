package kz.bgm.platform.web.customer.action;

import kz.bgm.platform.model.domain.*;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploadReportMobileServlet extends HttpServlet {

    public static final Logger log = Logger.getLogger(UploadReportMobileServlet.class);

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
            List<FileItem> fields = fileUploader.parseRequest(req);

            if (fields == null) {
                resp.sendRedirect("/customer/reports.html?er=no-file-reports-uploaded");
                return;
            }

            CustomerReport report = new CustomerReport();

            List<CustomerReportItem> allItems = new ArrayList<>();
            fillItems(fields, report, allItems);

            Date now = new Date();

            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null) {
                report.setCustomerId(user.getCustomerId());
            }

            report.setUploadDate(now);
            report.setTracks(allItems.size());
            report.setType(CustomerReport.Type.MOBILE);

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
            report.setDetected(detected);

            catalogService.saveCustomerReportItems(allItems);

            HttpSession ses = req.getSession(true);
            ses.setAttribute("report-" + reportId, report);

            resp.sendRedirect("/customer/view/report-upload-result.jsp?rid=" + reportId);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("/customer/reports.html?er=" + e.getMessage());

        }
    }

    private void fillItems(List<FileItem> fields, CustomerReport report, List<CustomerReportItem> allItems) throws Exception {
        for (FileItem item : fields) {
            if (item.isFormField()) {
                fillParam(item, report);
            } else {

                String reportFile = REPORTS_HOME + "/" + item.getName();
                saveToFile(item, reportFile);

                log.info("Got client report " + item.getName());

                List<CustomerReportItem> items = ReportParser.parseMobileReport(reportFile);
                allItems.addAll(items);
            }
        }
    }


    public CustomerReport fillParam(FileItem item, CustomerReport customerReport) {
        String paramName = item.getFieldName();
        String value;

        switch (paramName) {
            case "dt":
                value = item.getString();
                log.info("--Report date =" + value);

                Date reportDate = null;
                try {
                    reportDate = value != null ? FORMAT.parse(value) : new Date();
                } catch (ParseException e) {
                    log.warn(e.getMessage());
                }

                customerReport.setStartDate(reportDate);

                break;
            case "period":
                value = item.getString();
                int per = value != null ? Integer.parseInt(value) : 0;
                CustomerReport.Period period = value != null ?
                        CustomerReport.Period.values()[per] :
                        CustomerReport.Period.MONTH;

                log.info("--Report period =" + period);

                customerReport.setPeriod(period);

                break;
            case "cid":
                value = item.getString();
                log.info("--Report customer id =" + value);

                long customerId = Long.parseLong(value);
                Customer customer = catalogService.getCustomer(customerId);

                if (customer == null) {
                    log.info("Customer not found");
                    return null;
                }
                customerReport.setCustomerId(customer.getId());
                break;
        }


        return customerReport;
    }

    private void saveToFile(FileItem item, String filename) throws Exception {
        log.info("File name:" + item.getName());
        File reportFile = new File(filename);
        item.write(reportFile);
    }

}
