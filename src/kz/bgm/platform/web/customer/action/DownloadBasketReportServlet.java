package kz.bgm.platform.web.customer.action;

import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.utils.jasperreports.BasketReport;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

//import net.sf.jasperreports.engine.*;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class DownloadBasketReportServlet extends HttpServlet {


    private static final Logger log = Logger.getLogger(DownloadBasketReportServlet.class);

    private static final String appDir = System.getProperty("user.dir");
    public static final String REPORT_DIR_PATH = appDir + "/data/basket-reports";
    public static final String REPORT_FILE_PATH = REPORT_DIR_PATH +
            "/basket-report.pdf";
    public static final String BASKET_PATH = "/WEB-INF/views/customer/basket.jsp";

    private CatalogStorage catalogService;


    @Override
    public void init() throws ServletException {
        catalogService = CatalogFactory.getStorage();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Long> tracksId = getTracksId(req);

        if (!tracksId.isEmpty()) {
            try {
                log.info("Creating report with " + tracksId.size() + " tracks");

                List<Track> tracks = catalogService.getTracks(tracksId);
                List<BasketReport> reportsList = getBasketReports(tracks);

                JasperReport jasperReport = JasperCompileManager
                        .compileReport("data/jasper-reports-skeletons/basket-report.jrxml");

                JRDataSource dataSource =
                        new JRBeanCollectionDataSource(reportsList);

                JasperPrint print = JasperFillManager.fillReport(jasperReport,
                        new HashMap<String, Object>(), dataSource);

                JasperExportManager.exportReportToPdfFile(print,
                        REPORT_FILE_PATH);

                sendFileToClient(resp);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("Cant save file " + REPORT_FILE_PATH);
                RequestDispatcher rd =
                        req.getRequestDispatcher(BASKET_PATH);
                rd.forward(req, resp);
            }
        } else {
            log.info("No tracks was in request");
            RequestDispatcher rd =
                    req.getRequestDispatcher(BASKET_PATH);
            rd.forward(req, resp);
        }


    }

    private void sendFileToClient(HttpServletResponse resp) {
        FileInputStream fis = null;
        OutputStream out = null;

        try {
            resp.setContentType("application/pdf");

            File reportFile = new File(REPORT_FILE_PATH);
            if (!reportFile.exists()) {
                boolean created = reportFile.createNewFile();
                if (!created) {
                    log.warn("Cant create file " + REPORT_FILE_PATH);
                    return;
                }
            }

            fis = new FileInputStream(reportFile);
            out = resp.getOutputStream();

            int read;
            byte[] bytes = new byte[1024];

            while ((read = fis.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

        } catch (IOException ie) {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private List<BasketReport> getBasketReports(List<Track> tracks) {
        BasketReport rep = new BasketReport();
        //noinspection unchecked
        rep.setData(tracks);
        List<BasketReport> reportsList = new ArrayList<>();
        reportsList.add(rep);
        return reportsList;
    }

    private List<Long> getTracksId(HttpServletRequest req) {
        Enumeration<String> paramNames = req.getParameterNames();
        List<Long> trackIds = new ArrayList<>();

        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();

            if (name.contains("track")) {
                String valStr = req.getParameter(name);

                try {
                    Long val = Long.parseLong(valStr);
                    trackIds.add(val);
                } catch (NumberFormatException ne) {
                    log.warn("Parameter with name " + name + " cud not parse Long");
                }

            }
        }
        return trackIds;
    }


}
