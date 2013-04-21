package kz.bgm.platform.web;


import kz.bgm.platform.items.CalculatedReportItem;
import kz.bgm.platform.parsers.BlankReportParser;
import kz.bgm.platform.service.CatalogFactory;
import kz.bgm.platform.service.CatalogStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProcessReportServlet extends HttpServlet {


    CatalogStorage storage;

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {



        List<CalculatedReportItem> reportsList = storage.getCalculatedReports();

        BlankReportParser.createDoneReportExcel("./reports/SONY.xlsx", reportsList);




        req.setAttribute("reports",reportsList);

        req.getRequestDispatcher("/result.jsp").forward(req, resp);


    }


}
