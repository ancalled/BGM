package kz.bgm.platform.model.domain;

import java.io.Serializable;

public class ReportItemTrack implements Serializable {

    private long id;
    private long itemId;
    private long trackId;

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
}
