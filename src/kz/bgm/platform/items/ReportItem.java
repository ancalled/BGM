package kz.bgm.platform.items;

import java.io.Serializable;

public class ReportItem implements Serializable {

    private String author;
    private String compisition;
    private int price;
    private int qty;
    private float rate;
    private String contentType;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCompisition() {
        return compisition;
    }

    public void setCompisition(String compisition) {
        this.compisition = compisition;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
