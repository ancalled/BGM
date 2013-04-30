package kz.bgm.platform.test.builder;


import kz.bgm.platform.model.domain.CalculatedReportItem;
import kz.bgm.platform.utils.ReportBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ReportBuilderTest {


    @Test
    public void builderTest() {

        CalculatedReportItem report = new CalculatedReportItem();
        report.setReportItemId(1);
        report.setCompositionCode("777");
        report.setCompositionName("name111");
        report.setArtist("artist111");
        report.setQty(34);
        report.setCatalog("WRch");
        report.setSharePublic(344);

        List<CalculatedReportItem> reps = new ArrayList<>();
        reps.add(report);
        ReportBuilder.buildReportExcelFile("./data/report-templates/public.xlsx", reps);




    }


}
