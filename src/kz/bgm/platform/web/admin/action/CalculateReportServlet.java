package kz.bgm.platform.web.admin.action;

import kz.bgm.platform.model.domain.CalculatedReportItem;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.utils.JsonUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CalculateReportServlet extends HttpServlet {


    private CatalogStorage storage;
    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

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
        try {

            String platform = req.getParameter("platform");
            String type = req.getParameter("type");
            String fromDateStr = req.getParameter("from");
            String toDateStr = req.getParameter("to");

            Date from = FORMAT.parse(fromDateStr);
            Date to = FORMAT.parse(toDateStr);

            List<CalculatedReportItem> items;
            switch (type) {
                case "mobile":
                    items = storage.calculateMobileReport(platform, from, to);
                    break;
                case "public":
                    items = storage.calculatePublicReport(platform, from, to);
                    break;
                default:
                    jsonObj.put("status", "error");
                    jsonObj.put("er", "Unknown report type: " + type);
                    jsonObj.writeJSONString(out);
                    return;
            }
            jsonObj.put("status", "ok");
            jsonObj.put("report-items", items); //todo fill inti json array
            jsonObj.writeJSONString(out);


        } catch (Exception e) {
            e.printStackTrace();
//            resp.sendRedirect(RESULT_URL + "?er=" + e.getMessage());
            jsonObj.put("status", "error");
            jsonObj.put("er", e.getMessage());
            jsonObj.writeJSONString(out);

        }
    }


}
