package kz.bgm.platform.web;


import kz.bgm.platform.items.CalculatedReportItem;
import kz.bgm.platform.parsers.ReportBuilder;
import kz.bgm.platform.service.CatalogFactory;
import kz.bgm.platform.service.CatalogStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProcessReportServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(ProcessReportServlet.class);
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


        String catalogName = (String) req.getAttribute("catalog");
        //todo finish gui part of calculated catalog downloading

        log.info("request to build reports");

        List<CalculatedReportItem> reportsList = storage.getCalculatedReports("Sony ATV");

        ReportBuilder.buildReportExcelFile("./reports/SONY.xlsx", reportsList);

        req.setAttribute("reports",reportsList);

        //todo make try catch
        req.getRequestDispatcher("/reports.jsp").forward(req, resp);


    }


}
