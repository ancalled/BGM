package kz.bgm.platform.web.action;

import kz.bgm.platform.model.domain.Customer;
import kz.bgm.platform.model.domain.CustomerReport;
import kz.bgm.platform.model.domain.CustomerReportItem;
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
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class UploadReportMobileServlet extends HttpServlet {

    public static final Logger log = Logger.getLogger(UploadReportMobileServlet.class);

    public static final String APP_HOME = System.getProperty("user.dir");
    public static final String REPORTS_HOME = APP_HOME + "/reports";

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

                        int reportId = catalogService.saveCustomerReport(report);

                        List<CustomerReportItem> items = ReportParser.parseMobileReport(filePath, reportId);

                        if (items != null) {

                            for (CustomerReportItem i : items) {
                                List<Long> ids = luceneSearch.search(i.getArtist(), i.getName());
                                if (ids.size() > 0) {
                                    i.setCompositionId(ids.get(0));
                                }
                            }

                            catalogService.saveCustomerReportItems(items);

                        }

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