package kz.bgm.platform.web.customer.api;

import kz.bgm.platform.model.domain.CustomerReport;
import kz.bgm.platform.model.domain.CustomerReportItem;
import kz.bgm.platform.model.domain.User;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonRemoveFromReportServlet extends HttpServlet {

    private CatalogStorage service;
    private static final Logger log = Logger.getLogger(JsonRemoveFromReportServlet.class);

    @Override
    public void init() throws ServletException {
        service = CatalogFactory.getStorage();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String reportIdStr = req.getParameter("report_id");
        String itemIdStr = req.getParameter("item_id");
        String trackFound = req.getParameter("found_track");

        if (reportIdStr == null || itemIdStr == null) return;

        long reportId = Long.parseLong(reportIdStr);
        long itemId = Long.parseLong(itemIdStr);

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        resp.setContentType("application/json");
        JSONObject obj = new JSONObject();


        if (user != null) {

            CustomerReportItem item = service.getCustomerReportsItem(itemId);
            if (item == null) {
                log.warn("Could not find item!");
                obj.put("status", "error");
                obj.put("message", "Item not found!");
                return;
            }

            if (item.getReportId() != reportId) {
                obj.put("status", "error");
                obj.put("message", "Wrong report item!");
                return;
            }

            log.info("\nRemove item " + itemId + " from user catalog \n" +
                    "user id    : " + user.getId() + "\n" +
                    "user login : " + user.getLogin());

            service.removeItemFromReport(itemId);


            CustomerReport report = service.getCustomerReport(reportId);
            if (report != null) {


                StringBuilder logBuff = new StringBuilder();
                logBuff.append("Remove one ");
                if ("same-track".equalsIgnoreCase(trackFound.trim())) {
                    logBuff.append("detected ");
                    service.updtDetectedTracksInCustomerReport(reportId, report.getDetected() - 1);
                } else if ("not-found".equalsIgnoreCase(trackFound.trim())) {
                    service.updtTracksInCustomerReport(reportId, report.getTracks() - 1);
                }
                logBuff.append("track count from Customer report with id:").append(reportId);
                log.info(logBuff.toString());

            }

            //todo save to session history

            log.info("Item has been removed");
            obj.put("status", "ok");
        }

        PrintWriter out = resp.getWriter();
        out.print(obj);
        out.flush();

    }
}
