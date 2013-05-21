package kz.bgm.platform.web.admin;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        String pth = req.getPathInfo();

        Action action = null;
        switch (pth) {
            case "/index":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {

                        List<Platform> platforms = catalogStorage.getAllPlatforms();
                        req.setAttribute("platforms", platforms);

                        List<CustomerReport> reports = catalogStorage.getAllCustomerReports();

                        List<CustomerReportStatistic> reportStatistics = new ArrayList<>();

                        for (CustomerReport rep : reports) {
                            Customer customer = catalogStorage.getCustomer(rep.getCustomerId());
                            List<CustomerReportItem> repItemList = catalogStorage.
                                    getCustomerReportsItems(rep.getId());
                            CustomerReportStatistic crs = new CustomerReportStatistic();
                            crs.setReportDate(rep.getStartDate());
                            crs.setSendDate(rep.getUploadDate());
                            crs.setReportPeriod(rep.getPeriodOrdinal());
                            crs.setReportType(rep.getTypeOrdinal());

                            crs.setCustomerId(rep.getCustomerId());
                            if (customer != null) {
                                crs.setCustomer(customer.getName());
                            }
                            if (repItemList.size() > 0) {
                                crs.setCalculated(true);
                            } else {
                                crs.setCalculated(false);
                            }
                            reportStatistics.add(crs);
                        }

                        req.setAttribute("reports", reportStatistics);

                        return "index";
                    }
                };
                break;

            case "/search-result":
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

            case "/customers":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {
                        List<Customer> customers = catalogStorage.getAllCustomers();
                        req.setAttribute("customers", customers);

                        return "customers";
                    }
                };
                break;

            case "/catalog":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {

                        String catIdStr = req.getParameter("catId");
                        if (catIdStr != null) {
                            long catId = Long.parseLong(catIdStr);
                            Catalog catalog = catalogStorage.getCatalog(catId);
                            req.setAttribute("catalog", catalog);
                        }

                        return "catalog";
                    }
                };
                break;


            case "/catalog-update":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {

                        String erCode = req.getParameter("er");
                        if (erCode != null) {
                            req.setAttribute("erCode", erCode);
                        }

                        HttpSession ses = req.getSession();
                        if (ses != null) {
                            CatalogUpdate update = (CatalogUpdate) ses.getAttribute("catalog-update");
                            if (update != null) {
                                req.setAttribute("update", update);

                                Catalog catalog = catalogStorage.getCatalog(update.getCatalogId());
                                req.setAttribute("catalog", catalog);

                                if (update.getStatus() == CatalogUpdate.Status.OK) {
                                    String fromStr =  req.getParameter("from");
                                    int from = fromStr != null ? Integer.parseInt(fromStr) : 0;
                                    if (from < 0) from = 0;
                                    if (from > update.getCrossing()) from = update.getCrossing();

                                    List<TrackDiff> diffs =  catalogStorage.getCatalogUpdateDiff(from, TRACKS_PER_PAGE);
                                    req.setAttribute("diffs", diffs);
                                    req.setAttribute("from", from);
                                    req.setAttribute("pageSize", TRACKS_PER_PAGE);
                                }
                            }
                        }

                        return "catalog-update";
                    }
                };
                break;


            case "/all-customer-reports":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {

                        String from = req.getParameter("from");
                        String to = req.getParameter("to");

                        Date fromDate, toDate;
                        try {
                            fromDate = from == null ? new Date() : FORMAT.parse(from);
                            toDate = to == null ? new Date() : FORMAT.parse(to);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return null;
                        }

                        User user = (User) req.getSession().getAttribute("user");
                        if (user == null) {
                            return null;
                        }

                        List<CustomerReport> reports = catalogStorage.
                                getCustomerReports(user.getCustomerId(), fromDate, toDate);

                        req.setAttribute("reports", reports);

                        return "all-customer-reports";

                    }
                };
                break;

            case "/customer-detail":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {
                        String strCustId = req.getParameter("customer_id");

                        long customerId = Long.parseLong(strCustId);
                        Customer customer = catalogStorage.getCustomer(customerId);

                        List<User> userList = catalogStorage.getUsersByCustomerId(customerId);

                        req.setAttribute("customer", customer);
                        req.setAttribute("users", userList);

                        return "customer-detail";
                    }
                };
                break;

            case "/create-user-form":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {

                        return "create-user-form";
                    }
                };
                break;

            case "/create-customer-form":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {

                        return "create-customer-form";
                    }
                };
                break;

            case "/reports":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {

                        return "reports";
                    }
                };
                break;


        }


        if (action != null) {
            String view = action.execute(req, resp);
            req.getRequestDispatcher("/WEB-INF/views/admin/" + view + ".jsp").forward(req, resp);

        } else {
            resp.sendRedirect("/404.html");
        }


    }


    public static interface Action {
        String execute(HttpServletRequest req, HttpServletResponse resp);

    }
}
