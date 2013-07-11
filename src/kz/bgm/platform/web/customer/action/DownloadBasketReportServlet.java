package kz.bgm.platform.web.customer.action;

import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.utils.jasperreports.BasketReport;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

public class DownloadBasketReportServlet extends HttpServlet {


    private static final Logger log = Logger.getLogger(DownloadBasketReportServlet.class);

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
                List<Track> tracks = catalogService.getTracks(tracksId);
                List<BasketReport> reportsList = getBasketReports(tracks);

                JasperReport jasperReport;
                jasperReport = JasperCompileManager
                        .compileReport("data/jasper-reports-skeletons/basket-report.jrxml");


                JRDataSource dataSource =
                        new JRBeanCollectionDataSource(reportsList);

                JasperPrint print = JasperFillManager.fillReport(jasperReport,
                        new HashMap<String,Object>(), dataSource);

                JasperExportManager.exportReportToPdfFile(print,
                        "Example.pdf"); //todo поправить путь

            } catch (JRException e) {
                e.printStackTrace();
            }
        } else {

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
