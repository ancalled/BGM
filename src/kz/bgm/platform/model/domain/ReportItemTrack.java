package kz.bgm.platform.model.domain;

import kz.bgm.platform.model.service.LuceneSearch;

import java.io.Serializable;

public class ReportItemTrack implements Serializable {

    private long id;
    private long itemId;
    private long trackId;
    private float score;
    private boolean matched;

    public ReportItemTrack() {
    }


    public ReportItemTrack(long itemId, long trackId, float score) {
        this.itemId = itemId;
        this.trackId = trackId;
        this.score = score;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }
}
