package kz.bgm.platform.web.admin;

import kz.bgm.platform.model.domain.Catalog;
import kz.bgm.platform.model.domain.Customer;
import kz.bgm.platform.model.domain.CustomerReport;
import kz.bgm.platform.model.domain.User;
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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class DispatcherServlet extends HttpServlet {


    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");


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
                        List<Customer> customers = catalogStorage.getCustomers();
                        req.setAttribute("customers", customers);

                        return "customers";
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
            case "/index":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {
                        HttpSession session = req.getSession();

                        String catalogsAttr = "catalogs";
                        String reportsAttr = "reports";
                        String statisticAttr = "cat_stat";

                        @SuppressWarnings("unchecked")
                        List<Catalog> catalogList = (List<Catalog>)
                                session.getAttribute(catalogsAttr);
                        if (catalogList == null) {
                            catalogList = catalogStorage.getAllCatalogs();
                            session.setAttribute(catalogsAttr, catalogList);
                        }


                        @SuppressWarnings("unchecked")
                        List<CustomerReport> customerReportList = (List<CustomerReport>)
                                session.getAttribute(reportsAttr);

                        if (customerReportList == null) {
                            customerReportList = catalogStorage.getAllCustomerReports();
                            session.setAttribute(reportsAttr, customerReportList);
                        }

                        @SuppressWarnings("unchecked")
                        Map<String, Integer> catalogStatMap = (Map<String, Integer>)
                                session.getAttribute(statisticAttr);

                        if (catalogStatMap == null) {
                            catalogStatMap = new LinkedHashMap<>();

                            for (Catalog cat : catalogList) {
                                int compCount = catalogStorage.getCompositionCount(cat.getId());
                                catalogStatMap.put(cat.getName(), compCount);
                            }
                            session.setAttribute(statisticAttr, catalogStatMap);
                        }
                        //todo навреное стоит запихнуть это в сессию, а потом проверять на наличие
                        // todo чтобы не делать много лишних запросов
                        req.setAttribute("cat_stat", catalogStatMap);
                        req.setAttribute("catalogs", catalogList);
                        req.setAttribute("reports", customerReportList);

                        return "index";
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
