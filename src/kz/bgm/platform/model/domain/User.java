package kz.bgm.platform.model.domain;

public class User {

    private long id;
    private String login;
    private String pass;
    private long customerID;

    public User() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setCustomerID(long customerID) {
        this.customerID = customerID;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public long getCustomerID() {
        return customerID;
    }
}
