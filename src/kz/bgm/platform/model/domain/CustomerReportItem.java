package kz.bgm.platform.model.domain;


public class CustomerReportItem {

    private long id;
    private long reportId;
    private long compositionId;
    private String name;
    private String artist;
    private String contentType;
    private int qty;
    private float price;

    public long getId() {
        return id;
    }

    public long getReportId() {
        return reportId;
    }

    public long getCompositionId() {
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

    public void setId(long id) {
        this.id = id;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public void setCompositionId(long compositionId) {
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
