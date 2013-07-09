package kz.bgm.platform.test.jasper;


import kz.bgm.platform.model.domain.Track;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuildReportTest {


    public static void main(String[] args)
            throws JRException {
        JasperReport jasperReport;

        HashMap<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("user_name","Maximus");
        jasperReport = JasperCompileManager
                .compileReport("basket-report.jrxml");

        JRField[] fields = jasperReport.getFields();
        Track track =new Track();
        track.setCode("343443");
        List<Track> tracks= new ArrayList();
        tracks.add(track);
        JRDataSource datasource = new JRBeanCollectionDataSource(tracks, true);

        for(JRField f :fields){

        }
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters,
               datasource);
        JasperExportManager.exportReportToPdfFile(print,
                "Example.pdf");

        JasperViewer.viewReport(print);


    }


}
