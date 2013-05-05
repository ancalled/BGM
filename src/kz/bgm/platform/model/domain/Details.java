package kz.bgm.platform.model.domain;

public class Details {
    private long id;
    private long rnn;
    private String address;
    private String boss;

    public long getId() {
        return id;
    }

    public long getRnn() {
        return rnn;
    }

    public String getAddress() {
        return address;
    }

    public String getBoss() {
        return boss;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRnn(long rnn) {
        this.rnn = rnn;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }
}
