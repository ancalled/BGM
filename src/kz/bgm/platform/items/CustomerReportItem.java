package kz.bgm.platform.items;


public class CustomerReportItem {

    int id;
    int reportId;
    int compositionId;
    String name;
    String artist;
    String contentType;
    int qty;
    float price;

    public int getId() {
        return id;
    }

    public int getReportId() {
        return reportId;
    }

    public int getCompositionId() {
        return compositionId;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getContentType() {
        return contentType;
    }

    public int getQty() {
        return qty;
    }

    public float getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public void setCompositionId(int compositionId) {
        this.compositionId = compositionId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
