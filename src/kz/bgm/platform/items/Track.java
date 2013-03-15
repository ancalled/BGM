package kz.bgm.platform.items;


import java.io.Serializable;

public class Track implements Serializable {

    private long id = 0L;
    private String catalog = "";
    private String code = "";
    private String artist = "";
    private String composition = "";
    private String musicAuthors = "";
    private String lyricsAuthors = "";
    private String authors = "";
    private String comment = "";
    private String publisher = "";
    private Float controlled_metch = 0F;
    private Float collect_metch = 0F;
    private Float royalty = 0F;
    private Float publicRate = 0F;
    private Float mobileRate = 0F;


    public Track() {
    }

    public Track(String code, String composition, String artist) {
        this.code = code;
        this.composition = composition;
        this.artist = artist;
    }

    public void setControlled_metch(Float controlled_metch) {
        this.controlled_metch = controlled_metch;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setCollect_metch(Float collect_metch) {
        this.collect_metch = collect_metch;
    }

    public Float getControlled_metch() {
        return controlled_metch;
    }

    public Float getCollect_metch() {
        return collect_metch;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getAuthors() {
        return authors;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getMusicAuthors() {
        return musicAuthors;
    }

    public void setMusicAuthors(String musicAuthors) {
        this.musicAuthors = musicAuthors;
    }

    public String getLyricsAuthors() {
        return lyricsAuthors;
    }

    public void setLyricsAuthors(String lyricsAuthors) {
        this.lyricsAuthors = lyricsAuthors;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Float getRoyalty() {
        return royalty;
    }

    public void setRoyalty(Float royalty) {
        this.royalty = royalty;
    }

    public Float getPublicRate() {
        return publicRate;
    }

    public void setPublicRate(Float publicRate) {
        this.publicRate = publicRate;
    }

    public Float getMobileRate() {
        return mobileRate;
    }

    public void setMobileRate(Float mobileRate) {
        this.mobileRate = mobileRate;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }


    @Override
    public String toString() {
        return "code: " + code +
                ", composition: " + composition +
                ", authors: " + authors +
                ", artist: " + artist +
                ", mobileRate: " + mobileRate +
                ", publicRate: " + publicRate +
                ", publisher: " + publisher +
                ", catalog: " + catalog;
    }
}
