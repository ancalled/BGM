package kz.bgm.platform.model.domain;

public class User {

    private long id;
    private String login;
    private String pass;
    private long customerId;

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

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
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

    public long getCustomerId() {
        return customerId;
    }
}
