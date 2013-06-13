package kz.bgm.platform.model.domain;

public class Customer {

    private long id;
    private String name;

    private CustomerType customerType;
    private RightType rightType;

    private float royalty;
    private String contract;


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

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public RightType getRightType() {
        return rightType;
    }

    public void setRightType(RightType rightType) {
        this.rightType = rightType;
    }

    public float getRoyalty() {
        return royalty;
    }

    public void setRoyalty(float royalty) {
        this.royalty = royalty;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }
}
