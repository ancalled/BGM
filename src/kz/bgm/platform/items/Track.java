package kz.bgm.platform.items;


import java.io.Serializable;

public class Track implements Serializable {

    private long id = 0L;
    private int catalogID = 0;
    private String code = "";
    private String artist = "";
    private String name = "";
    private String composer = "";
    private float publicShare = 0F;
    private float mobileShare = 0F;
    private float royalty = 0F;


    public void setMobileShare(float mobileShare) {
        this.mobileShare = mobileShare;
    }

    public void setRoyalty(float royalty) {
        this.royalty = royalty;
    }

    public void setPublicShare(float publicShare) {
        this.publicShare = publicShare;
    }

    public float getRoyalty() {
        return royalty;
    }

    public Float getPublicShare() {
        return publicShare;
    }

    public Track() {
    }

    public Track(String code, String name, String artist) {
        this.code = code;
        this.name = name;
        this.artist = artist;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String composition) {
        this.name = composition;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Float sharePublic() {
        return publicShare;
    }

    public Float getMobileShare() {
        return mobileShare;
    }


    public int getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(int catalogID) {
        this.catalogID = catalogID;
    }

    public void setComposer(String compositor) {
        this.composer = compositor;
    }

    public String getComposer() {
        return composer;
    }

    @Override
    public String toString() {
        return "code: " + code +
                ", name: " + name +
                ", artist: " + artist +
                ", composer: " + composer +
                ", mobileShare: " + mobileShare +
                ", publicShare: " + publicShare +
                ", catalogID: " + catalogID;
    }
}
