package kz.bgm.platform.web.admin.action;


import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DBActivityServlet extends HttpServlet {

    private CatalogStorage storage;
    private static final Logger log = Logger.getLogger(DBActivityServlet.class);

    long queryProcessID;

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
        queryProcessID = storage.getUpdateCatalogQueryId();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String queryTime = storage.getQueryProcessTime(queryProcessID);

        resp.setContentType("application/json;charset=utf-8");

        JSONObject json = new JSONObject();
        json.put("status", queryTime);

        PrintWriter pw = resp.getWriter();
        pw.print(json.toString());
        pw.close();
//        SELECT * FROM INFORMATION_SCHEMA.PROCESSLIST   -!!!
    }


}
