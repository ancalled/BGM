package kz.bgm.platform.web;

import kz.bgm.platform.model.domain.Customer;
import kz.bgm.platform.model.domain.CustomerReport;
import kz.bgm.platform.model.domain.CustomerReportItem;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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

                   HttpSession ses = req.getSession();
                    if (ses == null) return null;

                    req.setAttribute("query", ses.getAttribute("query"));
                    req.setAttribute("tracks", ses.getAttribute("tracks"));

                    return "search-result";
                }
            };


        } else  if ("/report-upload-result".equals(pth)) {
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
