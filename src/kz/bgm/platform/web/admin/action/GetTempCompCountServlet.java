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

public class GetTempCompCountServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(GetTempCompCountServlet.class);

    private CatalogStorage storage;

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
    }


    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject jsonObj = new JSONObject();
//        long lastId = storage.getLastCatalogUpdateId();
        int rowsCount = storage.getTempCompCount();
        jsonObj.put("rows", rowsCount);

        jsonObj.writeJSONString(out);
    }
}
