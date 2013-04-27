package kz.bgm.platform.web.action;

import kz.bgm.platform.model.domain.Customer;
import kz.bgm.platform.model.domain.CustomerReport;
import kz.bgm.platform.model.domain.CustomerReportItem;
import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.utils.ReportParser;
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
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class MobileReportServlet extends HttpServlet {

    public static final Logger log = Logger.getLogger(MobileReportServlet.class);

    public static final String APP_HOME = System.getProperty("user.dir");
    public static final String REPORTS_HOME = APP_HOME + "/reports";

    public static final String FILE = "file";

    private ServletFileUpload fileUploader;
    private CatalogStorage catalogService;

    @Override
    public void init() throws ServletException {
        fileUploader = new ServletFileUpload(new DiskFileItemFactory());
        catalogService = CatalogFactory.getStorage();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            List<FileItem> files = fileUploader.parseRequest(req);

            if (files != null) {

                for (FileItem file : files) {
                    String filePath = saveFile(file);

                    log.info("Got client report " + filePath);
//                    reportList = buildReport(filePath, clientRate);

                    Date reportDate = new Date();               //todo alternatively, get it from request params
                    CustomerReport.Period period = CustomerReport.Period.MONTH;  //todo and this

                    String customerName = "GSM Technologies";   //todo: put user to session after authorization -> get user from session -> get customer id (company id) from user


                    log.info("Storing to DB Customer reports " +
                            filePath + " by customer" + customerName);

                    Customer customer = catalogService.getCustomer(customerName);

                    if (customer != null) {
                        Date now = new Date();

                        CustomerReport report = new CustomerReport();
                        report.setCustomerId(customer.getId());
                        report.setStartDate(reportDate);
                        report.setPeriod(period);
                        report.setUploadDate(now);
                        report.setType(CustomerReport.Type.MOBILE);

                        int reportId = catalogService.insertCustomerReport(report);

                        List<CustomerReportItem> items = ReportParser.parseMobileReport(filePath, reportId);

                        if (items != null) {

                            for (CustomerReportItem i: items) {
                                Track tr = catalogService.search(i.getArtist(), i.getName());
                                if (tr != null) {
                                    i.setCompositionId((int) tr.getId());
                                }
                            }

                            catalogService.insertCustomerReportItem(items);

                        }

                        log.info("storing done");
                    }

                }

            } else {
                log.debug("No files to upload!");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            resp.sendRedirect("result.jsp?r=error");
        }


        resp.sendRedirect("result.jsp?r=ok");
    }


    private String saveFile(FileItem file) throws Exception {
        log.info("File name:" + file.getName());
        File reportFile = new File(REPORTS_HOME + "/" + file.getName());
        file.write(reportFile);
        return reportFile.getPath();
    }


}
