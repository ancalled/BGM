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
import org.apache.lucene.queryparser.classic.ParseException;

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

public class UploadReportMobileServlet extends HttpServlet {

    public static final Logger log = Logger.getLogger(UploadReportMobileServlet.class);

    public static final String APP_HOME = System.getProperty("user.dir");
    public static final String REPORTS_HOME = APP_HOME + "/reports";

    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static final String FILE = "file";
    public static final int RESULT_LIMIT = 10;

    public static final double SIMILARITY = 7.5 / 100; //7.5%

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

            List<CustomerReportItem> parsed = new ArrayList<>();
            fillItems(fields, report, parsed);

            Date now = new Date();

            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null) {
                report.setCustomerId(user.getCustomerId());
            }

            report.setUploadDate(now);
            report.setTracks(parsed.size());
            report.setType(CustomerReport.Type.MOBILE);

            long reportId = catalogService.saveCustomerReport(report);
            report.setId(reportId);

            List<CustomerReportItem> processed = new ArrayList<>();

            int detected = 0;

            for (CustomerReportItem i : parsed) {
                i.setReportId(reportId);

                List<SearchResult> res = null;
                try {
                    res = luceneSearch.search(i.getArtist(), null, i.getTrack(), RESULT_LIMIT);
                } catch (ParseException e) {
                    log.warn("Got parse exception: " + e.getMessage());
                }

                if (res != null && res.size() > 0) {
                    double maxScore = res.get(0).getScore();
                    for (SearchResult sr : res) {

                        if (Math.abs(sr.getScore() - maxScore) / maxScore > SIMILARITY) break;

                        CustomerReportItem pi = new CustomerReportItem(i);
                        pi.setCompositionId(sr.getTrackId());
                        pi.setDetected(true);
                        processed.add(pi);
                        detected++;
                    }
                } else {
                   processed.add(i);
                }
            }

            catalogService.saveCustomerReportItems(processed);
            catalogService.updateCustomerReport(report.getId(), detected);

            resp.sendRedirect("/customer/view/report?id=" + reportId);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("/customer/view/send-reports?er=" + e.getMessage());

        }
    }

    private void fillItems(List<FileItem> formFields, CustomerReport report, List<CustomerReportItem> items) throws Exception {

        for (FileItem item : formFields) {
            if (item.isFormField()) {
                fillReport(item, report);

            } else {
                log.info("Got client report " + item.getName());

                String reportFile = REPORTS_HOME + "/" + item.getName();
                saveToFile(item, reportFile);

                items.addAll(ReportParser.parseMobileReport(reportFile));
            }
        }
    }


    public void fillReport(FileItem item, CustomerReport report) {
        String value;

        switch (item.getFieldName()) {
            case "dt":
                value = item.getString();
                log.info("--Report date =" + value);

                Date date;
                try {
                    date = value != null ? FORMAT.parse(value) : new Date();
                } catch (java.text.ParseException e) {
                    log.warn(e.getMessage());
                    return;
                }

                report.setStartDate(date);

                break;
            case "period":
                value = item.getString();

                int per = value != null ? Integer.parseInt(value) : 0;
                CustomerReport.Period period = value != null ?
                        CustomerReport.Period.values()[per] :
                        CustomerReport.Period.MONTH;

                log.info("--Report period =" + period);

                report.setPeriod(period);

                break;
            case "cid":
                value = item.getString();
                log.info("--Report customer id =" + value);

                long customerId = Long.parseLong(value);
                Customer customer = catalogService.getCustomer(customerId);

                if (customer == null) {
                    log.info("Customer not found");
                    return;
                }

                report.setCustomerId(customer.getId());
                break;
        }

    }

    private static void saveToFile(FileItem item, String filename) throws Exception {
        log.info("File name:" + item.getName());
        File reportFile = new File(filename);
        item.write(reportFile);
    }

}
