package kz.bgm.platform.web.customer.action;

import kz.bgm.platform.model.domain.CustomerReport;
import kz.bgm.platform.model.domain.CustomerReportItem;
import kz.bgm.platform.model.domain.User;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RemoveFromReportServlet extends HttpServlet {

    private CatalogStorage service;
    private static final Logger log = Logger.getLogger(RemoveFromReportServlet.class);

    @Override
    public void init() throws ServletException {
        service = CatalogFactory.getStorage();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String reportIdStr = req.getParameter("report_id");
        String itemIdStr = req.getParameter("item_id");
        String fromStr = req.getParameter("from");

        if (reportIdStr == null || itemIdStr == null) return;

        long reportId = Long.parseLong(reportIdStr);
        long itemId = Long.parseLong(itemIdStr);

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            log.warn("Could not find user!");
            resp.sendRedirect("/customer/view/report?id=" + reportId + "&from=" + fromStr + "&er=user-not-found");
            return;
        }

        CustomerReport report = service.getCustomerReport(reportId);
        if (report == null) {
            log.warn("Could not find report!");
            resp.sendRedirect("/customer/view/report?id=" + reportId + "&from=" + fromStr+ "&er=report-not-found");
            return;
        }

        if (report.isAccepted()) {
            log.warn("Report is already sent!");
            resp.sendRedirect("/customer/view/report?id=" + reportId + "&from=" + fromStr+ "&er=report-is-accepted");
            return;
        }

        CustomerReportItem item = service.getCustomerReportsItem(itemId);
        if (item == null) {
            log.warn("Could not find item!");
            resp.sendRedirect("/customer/view/report?id=" + reportId + "&from=" + fromStr+ "&er=item-not-found");
            return;
        }

        if (item.getReportId() != reportId) {
            log.warn("Wrong report item!");
            resp.sendRedirect("/customer/view/report?id=" + reportId + "&from=" + fromStr+ "&er=wrong-item");
            return;
        }

        log.info("\nRemove item " + itemId + " from report " + reportId);

        service.removeItemFromReport(itemId);

        log.info("Item has been removed");
        resp.sendRedirect("/customer/view/report?id=" + reportId + "&from=" + fromStr);
    }
}
