package kz.bgm.items;


public class ResultStats implements BgmResult{

    private float secs;

    public float getTime() {
        return secs;
    }

    public void setTime(float secs) {
        this.secs = secs;
    }
}
