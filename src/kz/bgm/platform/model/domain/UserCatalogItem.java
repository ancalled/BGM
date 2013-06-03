package kz.bgm.platform.model.domain;

public class UserCatalogItem {
    private long id;
    private long userId;
    private long trackId;

    public UserCatalogItem() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }
}
