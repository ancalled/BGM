package kz.bgm.platform.web.admin;

import kz.bgm.platform.model.domain.*;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.utils.DateUtils;
import kz.bgm.platform.utils.Month;
import kz.bgm.platform.utils.Year;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;


public class DispatcherServlet extends HttpServlet {


    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final int TRACKS_PER_PAGE = 50;
    public static final String CUSTOMER = "customer";
    public static final String USERS = "users";
    public static final String CUSTOMER_ID = "customer_id";

    public static final int RANDOM_TRACKS_ON_INDEX_PAGE = 10;
    public static final int RANDOM_TRACK_ON_CATALOG_PAGE = 5;
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

        String pth = req.getPathInfo();

        final HttpSession ses = req.getSession();


        Action action = null;
        switch (pth) {
            case "/index":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {

                        Collection<Platform> platforms = catalogStorage.getAllPlatforms();
                        req.setAttribute("platforms", platforms);

                        int totalTracks = 0;
                        for (Platform p : platforms) {
                            if (p.getCatalogs() != null) {
                                for (Catalog c : p.getCatalogs()) {
                                    totalTracks += c.getTracks();
                                }
                            }
                        }

                        req.setAttribute("totalTracks", totalTracks);

                        return "index";
                    }
                };
                break;

            case "/search":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {
                        Collection<Platform> platforms = catalogStorage.getAllPlatforms();

                        req.setAttribute("platforms", platforms);
                        req.setAttribute("query", ses.getAttribute("query"));
                        req.setAttribute("tracks", ses.getAttribute("tracks"));
//                        req.setAttribute("from", from);
                        req.setAttribute("pageSize", TRACKS_PER_PAGE);
                        return "search";
                    }
                };
                break;

            case "/mass-search":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {
                        return "mass-search";
                    }
                };
                break;

            case "/report-calculator":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {
                        Collection<Platform> platforms = catalogStorage.getAllPlatforms();
                        req.setAttribute("platforms", platforms);

                        return "report-calculator";
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


            case "/catalog":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {

                        String catIdStr = req.getParameter("catId");
                        if (catIdStr != null) {
                            long catId = Long.parseLong(catIdStr);
                            Catalog catalog = catalogStorage.getCatalog(catId);
                            req.setAttribute("catalog", catalog);


                            List<CatalogUpdate> updates = catalogStorage.getAllCatalogUpdates(catId);
                            req.setAttribute("updates", updates);

//                            if (updates == null || updates.isEmpty()) {
//                            List<Track> randomTracks = catalogStorage.getRandomTracks(catId, RANDOM_TRACK_ON_CATALOG_PAGE);
//                            req.setAttribute("randomTracks", randomTracks);
//                            }

                        }

                        return "catalog";
                    }
                };
                break;

            case "/add-catalog":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {

                        String platformIdStr = req.getParameter("platformId");
                        if (platformIdStr != null) {
                            long platId = Long.parseLong(platformIdStr);
                            Collection<Platform> platforms = catalogStorage.getAllPlatforms();
                            req.setAttribute("platforms", platforms);
                            req.setAttribute("platformId", platId);


                        }
                        return "add-catalog";
                    }
                };
                break;

            case "/add-platform":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {
                        return "add-platform";
                    }
                };
                break;


            case "/catalog-update":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {

                        String idStr = req.getParameter("id");
                        if (idStr != null) {
                            long updateId = Long.parseLong(idStr);

                            String erCode = req.getParameter("er");
                            if (erCode != null) {
                                req.setAttribute("erCode", erCode);
                            }

                            CatalogUpdate update = catalogStorage.getCatalogUpdate(updateId);
                            if (update != null) {
                                req.setAttribute("update", update);

                                Catalog catalog = catalogStorage.getCatalog(update.getCatalogId());
                                req.setAttribute("catalog", catalog);

                                String fromStr = req.getParameter("from");
                                String fromNewStr = req.getParameter("from-new");

                                int fromNew = fromNewStr != null ? Integer.parseInt(fromNewStr) : 0;
                                int from = fromStr != null ? Integer.parseInt(fromStr) : 0;


                                if (fromNew < 0) {
                                    fromNew = 0;
                                }

                                if (from < 0) {
                                    from = 0;
                                }
                                if (from > update.getCrossing()) {
                                    from = update.getCrossing();
                                }

                                String activeTabStr = req.getParameter("active-tab");

                                if ("tab1".equals(activeTabStr)) {

                                    req.setAttribute("tab1", "active");

                                    List<TrackDiff> diffs =
                                            catalogStorage.
                                                    geChangedTracksOfCatalogUpdate(updateId, from, TRACKS_PER_PAGE);
                                    req.setAttribute("diffs", diffs);

                                } else if ("tab2".equals(activeTabStr)) {

                                    req.setAttribute("tab2", "active");

                                    List<Track> allNewTracks =
                                            catalogStorage.
                                                    getNewTracksOfCatalogUpdate(updateId, fromNew, TRACKS_PER_PAGE);
//                                                    getTempTracks(catalog.getId(), fromNew, TRACKS_PER_PAGE);
                                    req.setAttribute("tracks", allNewTracks);

                                } else {

                                    req.setAttribute("tab1", "active");

                                    List<TrackDiff> diffs =
                                            catalogStorage.
                                                    geChangedTracksOfCatalogUpdate(updateId, from, TRACKS_PER_PAGE);
                                    req.setAttribute("diffs", diffs);

                                    List<Track> allNewTracks =
                                            catalogStorage.
                                                    getNewTracksOfCatalogUpdate(updateId, fromNew, TRACKS_PER_PAGE);
//                                                    getTempTracks(catalog.getId(), fromNew, TRACKS_PER_PAGE);
                                    req.setAttribute("tracks", allNewTracks);

                                }

                                String page = req.getParameter("page");
                                if (page == null) page = "0";

                                req.setAttribute("fromNew", fromNew);
                                req.setAttribute("from", from);
                                req.setAttribute("page", page);
                                req.setAttribute("pageSize", TRACKS_PER_PAGE);
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
                        String strCustId = req.getParameter(CUSTOMER_ID);

                        long customerId = Long.parseLong(strCustId);
                        Customer customer = catalogStorage.getCustomer(customerId);

                        List<User> userList = catalogStorage.getUsersByCustomerId(customerId);

                        req.setAttribute(CUSTOMER, customer);
                        req.setAttribute(USERS, userList);

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

                        List<Customer> customers = catalogStorage.getAllCustomers();
                        req.setAttribute("customers", customers);

                        String nowStr = req.getParameter("from");

                        Date from = new Date();
                        if (nowStr != null) {
                            try {
                                from = new SimpleDateFormat("yyyy-MM").parse(nowStr);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        int quartersAgo = 6;
                        String quartersAgoStr = req.getParameter("quarters");
                        if (quartersAgoStr != null) {
                            quartersAgo = Integer.parseInt(quartersAgoStr);
                        }

                        String showNonActiveStr = req.getParameter("non-active");
                        boolean showNonAccepted = "yes".equals(showNonActiveStr);

                        int monthsAgo = quartersAgo * 3;

                        Date notLaterThen = DateUtils.getPreviousMonth(from, monthsAgo);

                        List<CustomerReport> reports = catalogStorage.getAllCustomerReports(notLaterThen);

                        List<Year> years = DateUtils.getQuartersBefore(from, quartersAgo);
                        for (CustomerReport r : reports) {
                            if (!showNonAccepted && !r.isAccepted()) continue;

                            Date reportDate = r.getStartDate();

                            for (Year y : years) {
                                for (Quarter q : y.getQuarters()) {
                                    for (Month m : q.getMonths()) {
                                        Date monthStart = m.getDate();
                                        Date monthEnd = DateUtils.getNextMonth(monthStart, 1);

                                        if (!reportDate.before(monthStart)  &&
                                                reportDate.before(monthEnd)) {
                                            m.addReport(r);
                                        }
                                    }
                                }
                            }
                        }

                        req.setAttribute("now", from);
                        req.setAttribute("years", years);

                        return "reports";
                    }
                };
                break;
            case "/edit-track":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {
                        String idStr = req.getParameter("id");
//                        String catalog = req.getParameter("catalog");
                        long id = 0;
                        if (idStr != null) {

                            try {
                                id = Long.parseLong(idStr);
                            } catch (NumberFormatException ne) {
                                ne.printStackTrace();
                                return "edit-track";
                            }

                            Track track = catalogStorage.getTrack(id);
                            Collection<Platform> platforms = catalogStorage.getAllPlatforms();

                            req.setAttribute("track", track);
                            req.setAttribute("platforms", platforms);
                        }
                        return "edit-track";
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
