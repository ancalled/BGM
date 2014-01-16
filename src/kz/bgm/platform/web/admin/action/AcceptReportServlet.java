package kz.bgm.platform.web.admin.action;

import kz.bgm.platform.model.domain.AdminUser;
import kz.bgm.platform.model.domain.CustomerReport;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AcceptReportServlet extends HttpServlet {

    public static final Logger log = Logger.getLogger(AcceptReportServlet.class);


    private CatalogStorage catalogService;


    @Override
    public void init() throws ServletException {
        catalogService = CatalogFactory.getStorage();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


        String reportIdStr = req.getParameter("reportId");

        long reportId = Long.parseLong(reportIdStr);


        HttpSession session = req.getSession();
        AdminUser admin = (AdminUser) session.getAttribute("admin");

        if (admin == null) {
            System.err.println("User not found!");
            resp.sendRedirect("/admin/view/report?id=" + reportId + "&er=user-not-found");
            return;
        }

        CustomerReport report = catalogService.getCustomerReport(reportId);

        if (report == null) {
            System.err.println("Report not found!");
            resp.sendRedirect("/admin/view/report?id=" + reportId + "&er=report-not-found");
            return;
        }

//        if (report.getCustomerId() != admin.getCustomerId()) {
//            System.err.println("Report is not of user firm!");
//            resp.sendRedirect("/admin/view/report?id=" + reportId + "&er=report-is-not-of-user-firm");
//            return;
//        }


        catalogService.acceptReport(reportId);

        resp.sendRedirect("/admin/view/report?id=" + reportId);


    }


}
