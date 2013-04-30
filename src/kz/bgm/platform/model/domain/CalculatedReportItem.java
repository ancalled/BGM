package kz.bgm.platform.model.domain;

public class CalculatedReportItem {

    private long reportItemId = 0;
    private String compositionCode = "";
    private String compositionName = "";
    private String artist = "";
    private String composer = "";
    private String contentType = "";
    private float price = 0f;
    private int qty = 0;
    private float vol = 0f;
    private float shareMobile = 0f;
    private float sharePublic = 0f;
    private float customerRoyalty = 0f;
    private float catalogRoyalty = 0f;
    private float revenue = 0f;
    private String catalog = "";
    private String copyright = "";


    public void setSharePublic(float sharePublic) {
        this.sharePublic = sharePublic;
    }

    public float getSharePublic() {
        return sharePublic;
    }

    public CalculatedReportItem() {
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setReportItemId(long reportItemId) {
        this.reportItemId = reportItemId;
    }

    public void setCompositionCode(String compositionCode) {
        this.compositionCode = compositionCode;
    }

    public void setCompositionName(String compositionName) {
        this.compositionName = compositionName;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setVol(float vol) {
        this.vol = vol;
    }

    public void setShareMobile(float shareMobile) {
        this.shareMobile = shareMobile;
    }

    public void setCustomerRoyalty(float customerRoyalty) {
        this.customerRoyalty = customerRoyalty;
    }

    public void setCatalogRoyalty(float catalogRoyalty) {
        this.catalogRoyalty = catalogRoyalty;
    }

    public void setRevenue(float revenue) {
        this.revenue = revenue;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public long getReportItemId() {
        return reportItemId;
    }

    public String getCompositionCode() {
        return compositionCode;
    }

    public String getCompositionName() {
        return compositionName;
    }

    public String getArtist() {
        return artist;
    }

    public String getComposer() {
        return composer;
    }

    public float getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }

    public float getVol() {
        return vol;
    }

    public float getShareMobile() {
        return shareMobile;
    }

    public float getCustomerRoyalty() {
        return customerRoyalty;
    }

    public float getCatalogRoyalty() {
        return catalogRoyalty;
    }

    public float getRevenue() {
        return revenue;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getCopyright() {
        return copyright;
    }
}
