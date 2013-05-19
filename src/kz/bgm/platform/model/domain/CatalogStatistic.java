package kz.bgm.platform.model.domain;

public class CatalogStatistic {

    String catalog;
    String rights;
    int trackCount;
    int artistCount;
    float royalty;

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public void setArtistCount(int artistCount) {
        this.artistCount = artistCount;
    }

    public void setRoyalty(float share) {
        this.royalty = share;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getRights() {
        return rights;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public int getArtistCount() {
        return artistCount;
    }

    public float getRoyalty
            () {
        return royalty;
    }
}
