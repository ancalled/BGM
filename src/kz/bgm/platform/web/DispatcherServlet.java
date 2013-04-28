package kz.bgm.platform.web;

import kz.bgm.platform.model.domain.Customer;
import kz.bgm.platform.model.domain.CustomerReport;
import kz.bgm.platform.model.domain.User;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.web.action.UploadReportMobileServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


public class DispatcherServlet extends HttpServlet {


    private CatalogStorage catalogStorage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        catalogStorage = CatalogFactory.getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pth = req.getPathInfo();

        Action action = null;
        if ("/search-result".equals(pth)) {
            action = new Action() {
                @Override
                public String execute(HttpServletRequest req, HttpServletResponse resp) {

                    HttpSession ses = req.getSession(false);
                    if (ses == null) return null;

                    req.setAttribute("query", ses.getAttribute("query"));
                    req.setAttribute("tracks", ses.getAttribute("tracks"));

                    return "search-result";
                }
            };


        } else if ("/report-upload-result".equals(pth)) {
            action = new Action() {
                @Override
                public String execute(HttpServletRequest req, HttpServletResponse resp) {

                    String repIdStr = req.getParameter("rid");
                    if (repIdStr == null) return null;
                    long reportId = Long.parseLong(repIdStr);

//                    CustomerReport report = catalogStorage.getCustomerReport(reportId);
//                    List<CustomerReportItem> items = catalogStorage.getCustomerReportsItems(reportId);
//                    Customer customer = catalogStorage.getCustomer(report.getCustomerId());
//
//                    req.setAttribute("report", report);
//                    req.setAttribute("items", items);
//                    req.setAttribute("customer", customer);

                    HttpSession ses = req.getSession();
                    if (ses == null) return null;

                    CustomerReport report = (CustomerReport) ses.getAttribute("report-" + reportId);
                    if (report != null) {
                        req.setAttribute("report", report);
                    }

                    return "report-upload-result";
                }
            };
        } else if ("/customers".equals(pth)) {
            action = new Action() {
                @Override
                public String execute(HttpServletRequest req, HttpServletResponse resp) {
                    List<Customer> customerList = catalogStorage.getCustomers();
                    req.setAttribute("customers", customerList);

                    return "customers";
                }
            };
        } else if ("/all-customer-reports".equals(pth)) {
            action = new Action() {
                @Override
                public String execute(HttpServletRequest req, HttpServletResponse resp) {
                    try {
                        String from = req.getParameter("from");
                        String to = req.getParameter("to");

                        Date fromDate = from == null ? new Date() : UploadReportMobileServlet.FORMAT.parse(from);
                        Date toDate = to == null ? new Date() : UploadReportMobileServlet.FORMAT.parse(to);

                        User user = (User) req.getSession().getAttribute("user");

                        if (user == null) {
                            return null;
                        }
                        List<CustomerReport> reports = catalogStorage.getCustomerReports(user.getCustomerID(),
                                fromDate, toDate);

                        req.setAttribute("reports", reports);


                    } catch (ParseException e) {
                        e.printStackTrace();
                        return null;
                    }
                    return "all-customer-reports";

                }
            };

        }


        if (action != null) {
            String view = action.execute(req, resp);
            req.getRequestDispatcher("/WEB-INF/views/" + view + ".jsp").forward(req, resp);

        } else {
            resp.sendRedirect("/404.html");
        }


    }


    public static interface Action {


        String execute(HttpServletRequest req, HttpServletResponse resp);

    }
}
