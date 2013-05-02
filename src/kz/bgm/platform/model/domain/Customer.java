package kz.bgm.platform.model.domain;

public class Customer {

    private long id;
    private String name;
    private float royalty;
    private String rightType;
    private long detailsId;


    public void setDetailsId(long detailsId) {
        this.detailsId = detailsId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoyalty(float royalty) {
        this.royalty = royalty;
    }

    public void setRightType(String rightType) {
        this.rightType = rightType;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getDetailsId() {
        return detailsId;
    }


    public float getRoyalty() {
        return royalty;
    }

    public String getRightType() {
        return rightType;
    }
}
