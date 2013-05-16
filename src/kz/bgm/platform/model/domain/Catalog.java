package kz.bgm.platform.model.domain;


public class Catalog {

    private long id;
    private String name;
    private float royalty;
    private String copyright;

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public void setRoyalty(float royalty) {
        this.royalty = royalty;
    }

    public float getRoyalty() {
        return royalty;
    }

    public String getCopyright() {
        return copyright;
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
}
