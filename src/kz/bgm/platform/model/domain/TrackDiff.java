package kz.bgm.platform.model.domain;


import java.io.Serializable;

public class TrackDiff implements Serializable {

    private Integer number;
    private String code;
    private Track oldTrack;
    private Track newTrack;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Track getOldTrack() {
        return oldTrack;
    }

    public void setOldTrack(Track oldTrack) {
        this.oldTrack = oldTrack;
    }

    public Track getNewTrack() {
        return newTrack;
    }

    public void setNewTrack(Track newTrack) {
        this.newTrack = newTrack;
    }
}
