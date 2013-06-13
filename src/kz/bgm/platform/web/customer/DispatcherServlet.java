package kz.bgm.platform.web.customer;

import kz.bgm.platform.model.domain.*;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;


public class DispatcherServlet extends HttpServlet {


    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static final int TRACKS_PER_PAGE = 50;
    private CatalogStorage catalogStorage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        catalogStorage = CatalogFactory.getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        final HttpSession ses = req.getSession();
        final User user = (User) ses.getAttribute("user");
        req.setAttribute("user", user);

        Customer customer = catalogStorage.getCustomer(user.getCustomerId());
        req.setAttribute("customer", customer);

        String pth = req.getPathInfo();

        Action action = null;
        switch (pth) {
            case "/search":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {

                        List<Long> tracks = catalogStorage.getCustomerBasket(user.getCustomerId());
                        if (!tracks.isEmpty()) {
                            req.setAttribute("customer_tracks", tracks);
                        }

                        List<Platform> platforms = catalogStorage.getAllPlatforms();

                        req.setAttribute("platforms", platforms);
                        req.setAttribute("query", ses.getAttribute("query"));
                        req.setAttribute("tracks", ses.getAttribute("tracks"));
//                        req.setAttribute("from", from);
                        req.setAttribute("pageSize", TRACKS_PER_PAGE);

                        return "search";
                    }
                };
                break;
            case "/report-upload-result":
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
                break;

            case "/index":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {


                        List<Platform> platforms = catalogStorage.getAllPlatforms();
                        req.setAttribute("platforms", platforms);

                        List<CustomerReport> reports = catalogStorage.getAllCustomerReports();
                        req.setAttribute("reports", reports);

                        return "index";
                    }
                };
                break;

            case "/basket":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {
                        List<Long> idList = catalogStorage.getCustomerBasket(user.getCustomerId());
                        List<Track> trackList = catalogStorage.getTracks(idList);

                        req.setAttribute("tracks", trackList);
                        return "basket";
                    }
                };
                break;
        }


        if (action != null) {
            String view = action.execute(req, resp);
            req.getRequestDispatcher("/WEB-INF/views/customer/" + view + ".jsp").forward(req, resp);

        } else {
            resp.sendRedirect("/404.html");
        }


    }


    public static interface Action {


        String execute(HttpServletRequest req, HttpServletResponse resp);

    }
}
