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

        HashMap<String, Object> parameters = new HashMap<>();

        jasperReport = JasperCompileManager
                .compileReport("basket-report.jrxml");
//        .compileReport("report-basket-new.jrxml");

        Track track = new Track();
        track.setCode("343443");
        track.setArtist("ABBA");
        track.setCatalog("Wch");
        track.setComposer("EMINEM");
        track.setName("Camone");
        track.setMobileShare(344.4f);
        Track track2 = new Track();
        track2.setCode("99999");
        track2.setArtist("MADONNA");
        track2.setCatalog("SONY");
        track2.setComposer("HZ");
        track2.setName("Fuck you");
        track2.setMobileShare(464.4f);

        List<Track> tracks = new ArrayList<>();
        tracks.add(track);
        tracks.add(track2);

        MyBean bean = new MyBean();
        bean.setTraks(tracks);

        List<MyBean> beans = new ArrayList<>();
        beans.add(bean);

        JRDataSource dataSource = new JRBeanCollectionDataSource(beans);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters,
                dataSource);
        JasperExportManager.exportReportToPdfFile(print,
                "Example.pdf");

        JasperViewer.viewReport(print);


    }

    public static class MyBean {
        private List <Track> traks;
        private String code;
        public MyBean() {
        }



        public List<Track> getTraks() {
            return traks;
        }

        public void setTraks(List<Track> traks) {
            this.traks = traks;
        }
    }


}
