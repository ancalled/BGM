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
import java.util.Date;
import java.util.List;

public class DownloadReportMobileServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(DownloadReportMobileServlet.class);

    public static final String MOBILE_SONY_ATV_TEMPLATE = "./data/report-templates/sony-atv.xlsx";

    //    public static final String COMP_REPS_PATH = System.getProperty("user.dir") + "/computed-reports";

    public static final String MOBILE_OUTPUTNAME = "./data/report-templates/sony-atv-%s.xlsx";

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


//        String catalogName = req.getParameter("catalog");
//        //todo finish gui part of calculated catalog downloading
//
//        log.info("LoadMobileReportServlet.doPost building reports");
//
//        List<CalculatedReportItem> reportsList = storage.calculateMobileReport(catalogName);
//
//        log.info("Calculated reports built at size " + reportsList.size());
//
//        String output = String.format(MOBILE_OUTPUTNAME, new Date());
//
//        ReportBuilder.buildReport(MOBILE_SONY_ATV_TEMPLATE, output, reportsList);
//
//        req.setAttribute("reports", reportsList);
//
//        //todo make try catch
//        req.getRequestDispatcher("/admin/reports.html").forward(req, resp);


    }


}
