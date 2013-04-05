package kz.bgm.platform.items;

public class Customer {

    int id;
    String name;
    float royalty;
    String rightType;

    public Customer() {

    }

    public void setId(int id) {
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getRoyalty() {
        return royalty;
    }

    public String getRightType() {
        return rightType;
    }
}
