package kz.bgm.platform.web.admin.action;

import kz.bgm.platform.model.domain.CalculatedReportItem;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CalculateReportServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(CalculateReportServlet.class);

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
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        JSONObject jsonObj = new JSONObject();
        try {

            String platform = req.getParameter("platform");
            String type = req.getParameter("type");
            String fromDateStr = req.getParameter("from");
            String toDateStr = req.getParameter("to");

            log.info(" Got request with params \n" +
                    "platform  "+platform+"\n" +
                    "type      "+type+"\n" +
                    "from date "+fromDateStr+"\n" +
                    "to date   "+toDateStr);

            Date from = FORMAT.parse(fromDateStr);
            Date to = FORMAT.parse(toDateStr);

            log.info("Calculating report");

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

            log.info("Calculating done report items size "+items.size());

            jsonObj.put("type", type);
            jsonObj.put("status", "ok");

            JSONArray array = makeJsonArray(items);

            jsonObj.put("report_items", array);
            jsonObj.writeJSONString(out);


        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
//            resp.sendRedirect(RESULT_URL + "?er=" + e.getMessage());
            jsonObj.put("status", "error");
            jsonObj.put("er", e.getMessage());
            jsonObj.writeJSONString(out);

        }
    }


    @SuppressWarnings("unchecked")
    private JSONArray makeJsonArray(List<CalculatedReportItem> reports) {
        if (reports == null) return null;
        JSONArray mainArray = new JSONArray();

        for (CalculatedReportItem rp : reports) {
            JSONObject jsonReport = new JSONObject();
            jsonReport.put("qty", rp.getQty());
            jsonReport.put("cust_royal", rp.getCustomerRoyalty());  //&??
            jsonReport.put("content_type", rp.getContentType());
            jsonReport.put("vol", rp.getVol());
            jsonReport.put("artist", rp.getArtist());
            jsonReport.put("price", rp.getPrice());
            jsonReport.put("revenue", rp.getRevenue());
            jsonReport.put("catalog", rp.getCatalog());
            jsonReport.put("cat_royal", rp.getCatalogRoyalty());
            jsonReport.put("composer", rp.getComposer());
            jsonReport.put("code", rp.getCompositionCode());
            jsonReport.put("name", rp.getCompositionName());
            jsonReport.put("copyright", rp.getCopyright());
            jsonReport.put("share_mobile", rp.getShareMobile());
            jsonReport.put("share_public", rp.getSharePublic());
            jsonReport.put("report_id", rp.getReportItemId());

            mainArray.add(jsonReport);
        }
        return mainArray;
    }


}
