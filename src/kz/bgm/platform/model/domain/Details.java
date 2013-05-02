package kz.bgm.platform.model.domain;

public class Details {
    private long id;
    private int rnn;
    private String address;
    private String boss;

    public long getId() {
        return id;
    }

    public int getRnn() {
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

    public void setRnn(int rnn) {
        this.rnn = rnn;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }
}
