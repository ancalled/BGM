package kz.bgm.platform.model.domain;


import java.util.Date;

public class CustomerReport {

    public static enum Type {
        MOBILE, PUBLIC
    }

    public static enum Period {
        MONTH, QUARTER
    }

    private long id;
    private long customerId;
    private Date startDate;
    private Date uploadDate;
    private Period period;
    private Type type;

    private int tracks;
    private int detected;

    public CustomerReport() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
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

    public int getTracks() {
        return tracks;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
    }

    public int getDetected() {
        return detected;
    }

    public void setDetected(int detected) {
        this.detected = detected;
    }

    public int getTypeOrdinal() {
        return type.ordinal();
    }

    public int getPeriodOrdinal() {
        return period.ordinal();
    }
}
