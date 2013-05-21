package kz.bgm.platform.web.customer;

import kz.bgm.platform.model.domain.Customer;
import kz.bgm.platform.model.domain.CustomerReport;
import kz.bgm.platform.model.domain.CustomerReportItem;
import kz.bgm.platform.model.domain.CustomerReportStatistic;
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
import java.util.ArrayList;
import java.util.List;


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
            case "/index":
                action = new Action() {
                    @Override
                    public String execute(HttpServletRequest req, HttpServletResponse resp) {
                        HttpSession session = req.getSession();

//                                   String catalogsAttr = "catalogs";
                        String reportsAttr = "reports";
//                                   String statisticAttr = "cat_stat";

//                                   @SuppressWarnings("unchecked")
//                                   List<Catalog> catalogList = (List<Catalog>)
//                                           session.getAttribute(catalogsAttr);
//                                   if (catalogList == null) {
//                                       catalogList = catalogStorage.getAllCatalogs();
//                                       session.setAttribute(catalogsAttr, catalogList);
//                                   }


                        @SuppressWarnings("unchecked")
                        List<CustomerReportStatistic> customerReportList = (List<CustomerReportStatistic>)
                                session.getAttribute(reportsAttr);

                        if (customerReportList == null) {
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

                            session.setAttribute(reportsAttr, reportStatistics);
                        }

//                                   @SuppressWarnings("unchecked")
//                                   List<CatalogStatistic> catalogStatList = (List<CatalogStatistic>)
//                                           session.getAttribute(statisticAttr);
//
//                                   if (catalogStatList == null) {
//                                       catalogStatList = new ArrayList<>();
//
//                                       for (Catalog cat : catalogList) {
//                                           int trackCount = catalogStorage.getCompositionCount(cat.getId());
//                                           int artistCount = catalogStorage.getArtistCount(cat.getId());
//
//                                           CatalogStatistic catStat = new CatalogStatistic();
//
//                                           catStat.setArtistCount(artistCount);
//                                           catStat.setTrackCount(trackCount);
//                                           catStat.setCatalog(cat.getName());
//                                           catStat.setRoyalty(cat.getRoyalty());
//                                           catStat.setRights(cat.getCopyright());
//                                           catalogStatList.add(catStat);
//                                       }
//                                       session.setAttribute(statisticAttr, catalogStatList);
//                                   }

//                                   req.setAttribute(statisticAttr, catalogStatList);
//                                   req.setAttribute(catalogsAttr, catalogList);
                        req.setAttribute(reportsAttr, customerReportList);

                        return "index";
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
