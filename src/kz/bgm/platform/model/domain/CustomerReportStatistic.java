package kz.bgm.platform.model.domain;

import java.util.Date;

public class CustomerReportStatistic {

    private Long customerId;
    private String customer;
    private Date sendDate;
    private Date reportDate;
    private int reportType;
    private int reportPeriod;
    private boolean calculated;


    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    public boolean getCalculated() {
        return calculated;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

    public void setReportPeriod(int reportPeriod) {
        this.reportPeriod = reportPeriod;
    }

    public String getCustomer() {
        return customer;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public int getReportType() {
        return reportType;
    }

    public int getReportPeriod() {
        return reportPeriod;
    }
}
