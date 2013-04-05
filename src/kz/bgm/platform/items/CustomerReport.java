package kz.bgm.platform.items;


import java.sql.Date;

public class CustomerReport {

    int id;
    int customerId;
    Date orderDate;
    Date downloadDate;

    public CustomerReport() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setDownloadDate(Date downloadDate) {
        this.downloadDate = downloadDate;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public Date getDownloadDate() {
        return downloadDate;
    }
}
