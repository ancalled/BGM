package kz.bgm.platform.model.domain;

import java.io.Serializable;

public class ReportItem implements Serializable {

    private String artist = "";
    private String composer = "";
    private String compisition = "";
    private float price;
    private int qty;
    private float rate;
    private String contentType = "";
    private float authRate;
    private int authorRevenue;
    private int publisherAuthRevenue;
    private String code = "";
    private String catalog = "";


    public String getCatalog() {
        return catalog;

    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getComposer() {
        return composer;
    }

    public String getArtist() {
        return artist;
    }

    public void setAuthRate(float authRate) {
        this.authRate = authRate;
    }

    public void setAuthorRevenue(int authorRevenue) {
        this.authorRevenue = authorRevenue;
    }

    public void setPublisherAuthRevenue(int publisherAuthRevenue) {
        this.publisherAuthRevenue = publisherAuthRevenue;
    }

    public float getAuthRate() {
        return authRate;
    }

    public int getAuthorRevenue() {
        return authorRevenue;
    }

    public int getPublisherAuthRevenue() {
        return publisherAuthRevenue;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCompisition() {
        return compisition;
    }

    public void setCompisition(String compisition) {
        this.compisition = compisition;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
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
