package kz.bgm.platform.model.domain;


public class CustomerReportItem {

    private Long id;
    private Long reportId;
    private Long compositionId;
    private String name;
    private String artist;
    private String contentType;
    private int qty;
    private float price;

    public Long getId() {
        return id;
    }

    public Long getReportId() {
        return reportId;
    }

    public Long getCompositionId() {
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public void setCompositionId(Long compositionId) {
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
