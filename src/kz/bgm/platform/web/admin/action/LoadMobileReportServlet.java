package kz.bgm.platform.web.admin.action;


import kz.bgm.platform.model.domain.CalculatedReportItem;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.utils.ReportBuilder;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class LoadMobileReportServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(LoadMobileReportServlet.class);
    private CatalogStorage storage;

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String catalogName = req.getParameter("catalog");
        //todo finish gui part of calculated catalog downloading

        log.info("LoadMobileReportServlet.doPost building reports");

        List<CalculatedReportItem> reportsList = storage.calculateMobileReport(catalogName);

        log.info("Calculated reports built at size " + reportsList.size());

        ReportBuilder.buildReportExcelFile("./data/report-templates/SONY.xlsx", reportsList);

        req.setAttribute("reports", reportsList);

        //todo make try catch
        req.getRequestDispatcher("/admin/reports.html").forward(req, resp);


    }


}
