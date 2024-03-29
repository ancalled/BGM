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
import java.util.Collection;
import java.util.Date;
import java.util.List;


public class DispatcherServlet extends HttpServlet {


    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static final int TRACKS_PER_PAGE = 50;
    public static final int DEFAULT_REPORTS_PER_PAGE = 100;
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

            case "/index":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {


                        Collection<Platform> platforms = catalogStorage.getOwnPlatforms();
                        req.setAttribute("platforms", platforms);

                        Collection<CustomerReport> reports = catalogStorage
                                .getCustomerReports(user.getCustomerId(), new Date(0), new Date());
                        req.setAttribute("reports", reports);

                        return "index";
                    }
                };
                break;

            case "/search":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {

                        Collection<Long> tracks = catalogStorage.getCustomerBasket(user.getCustomerId());
                        if (!tracks.isEmpty()) {
                            req.setAttribute("customer_tracks", tracks);
                        }

                        Collection<Platform> platforms = catalogStorage.getOwnPlatforms();

                        req.setAttribute("platforms", platforms);
                        req.setAttribute("query", ses.getAttribute("query"));
                        req.setAttribute("tracks", ses.getAttribute("tracks"));
//                        req.setAttribute("from", from);
                        req.setAttribute("pageSize", TRACKS_PER_PAGE);

                        return "search";
                    }
                };
                break;

            case "/send-report":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {
                        return "send-report";
                    }
                };
                break;

            case "/reports":


                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {
                        List<CustomerReport> reports = catalogStorage
                                .getCustomerReports(user.getCustomerId(), new Date(0), new Date());
                        req.setAttribute("reports", reports);
                        return "reports";
                    }
                };
                break;

            case "/report":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {

                        String repIdStr = req.getParameter("id");
                        if (repIdStr == null) return null;
                        long reportId = Long.parseLong(repIdStr);

                        String fromStr = req.getParameter("from");

                        int from = fromStr != null ? Integer.parseInt(fromStr) : 0;

                        String sizeStr = req.getParameter("size");
                        int size = sizeStr != null ? Integer.parseInt(sizeStr) : DEFAULT_REPORTS_PER_PAGE;

                        CustomerReport report = catalogStorage.getCustomerReport(reportId);
                        List<CustomerReportItem> items = catalogStorage.getCustomerReportsItems(reportId, from, size);


                        String page = req.getParameter("page");
                        if (page == null) page = "0";

                        req.setAttribute("report", report);
                        req.setAttribute("items", items);
                        req.setAttribute("from", from);
                        req.setAttribute("size", size);
                        req.setAttribute("page", page);

                        return "report";
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
