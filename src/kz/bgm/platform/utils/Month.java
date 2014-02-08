package kz.bgm.platform.utils;

import kz.bgm.platform.model.domain.CustomerReport;
import kz.bgm.platform.model.domain.Quarter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Month {

    private Date date;

    private List<CustomerReport> reports = new ArrayList<>();

    public Month(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }


    public List<CustomerReport> getReports() {
        return reports;
    }

    public void addReport(CustomerReport r) {
        reports.add(r);
    }
}
