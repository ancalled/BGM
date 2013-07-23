package kz.bgm.platform.model.domain;


public class CustomerReportItem {

    private Long id;
    private int number;

    private Long reportId;
    private Long compositionId;

    private String track;
    private String artist;
    private String authors;
    private String contentType;
    private int qty;
    private float price;
    private boolean detected = false;

    private Track foundTrack;


    public CustomerReportItem() {
    }

    public CustomerReportItem(CustomerReportItem item) {
        this.id = item.id;
        this.reportId = item.reportId;
        this.compositionId = item.compositionId;
        this.track = item.track;
        this.artist = item.artist;
        this.authors = item.authors;
        this.contentType = item.contentType;
        this.qty = item.qty;
        this.price = item.price;
        this.number = item.number;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(Long compositionId) {
        this.compositionId = compositionId;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isDetected() {
        return detected;
    }

    public void setDetected(boolean detected) {
        this.detected = detected;
    }

    public Track getFoundTrack() {
        return foundTrack;
    }

    public void setFoundTrack(Track foundTrack) {
        this.foundTrack = foundTrack;
    }
}
