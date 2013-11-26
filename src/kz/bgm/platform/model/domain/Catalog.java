package kz.bgm.platform.model.domain;


public class Catalog {

    private long id;
    private String name;
    private float royalty;
    private RightType rightType;
    private long platformId;
    private int tracks;
    private int artists;
    private String color;


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getPlatformId() {
        return platformId;
    }

    public void setPlatformId(long platformId) {
        this.platformId = platformId;
    }

    public void setRightType(RightType rightType) {
        this.rightType = rightType;
    }

    public void setRoyalty(float royalty) {
        this.royalty = royalty;
    }

    public float getRoyalty() {
        return royalty;
    }

    public RightType getRightType() {
        return rightType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTracks() {
        return tracks;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
    }

    public int getArtists() {
        return artists;
    }

    public void setArtists(int artists) {
        this.artists = artists;
    }
}
