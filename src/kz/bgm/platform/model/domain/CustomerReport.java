package kz.bgm.platform.model.domain;


import java.util.Date;

public class CustomerReport {

    public static enum Type {
        MOBILE, PUBLIC
    }

    public static enum Period {
        MONTH, QUARTER
    }

    private int id;
    private int customerId;
    private Date startDate;
    private Date uploadDate;
    private Period period;
    private Type type;

    public CustomerReport() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
