package kz.bgm.items;


public class Track {

    private String catalog;
    private String code;
    private String artist;
    private String composition;
    private String musicAuthors;
    private String lyricsAuthors;
    private float royalty;
    private float publicRate;
    private float mobileRate;


    public Track() {
    }

    public Track(String code, String composition, String artist) {
        this.code = code;
        this.composition = composition;
        this.artist = artist;
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

    public float getRoyalty() {
        return royalty;
    }

    public void setRoyalty(float royalty) {
        this.royalty = royalty;
    }

    public float getPublicRate() {
        return publicRate;
    }

    public void setPublicRate(float publicRate) {
        this.publicRate = publicRate;
    }

    public float getMobileRate() {
        return mobileRate;
    }

    public void setMobileRate(float mobileRate) {
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
                ", music authors: " + musicAuthors +
                ", lyrics authors: " + lyricsAuthors +
                ", artist: " + artist +
                ", mobileRate: " + mobileRate +
                ", publicRate: " + publicRate +
                ", catalog: " + catalog;
    }
}
