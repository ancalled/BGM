package kz.bgm.platform.model.domain;


import java.util.ArrayList;
import java.util.List;

public class CatalogUpdate {

    public static enum Status {OK, HAS_WARNINGS}

    private Long catalogId;
    private Status status;
    private Integer tracks;
    private Integer crossing;
    private String file;
    private boolean applied = false;

    private final List<UpdateWarning> warnings = new ArrayList<>();


    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public List<UpdateWarning> getWarnings() {
        return warnings;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getTracks() {
        return tracks;
    }

    public void setTracks(Integer tracks) {
        this.tracks = tracks;
    }

    public Integer getCrossing() {
        return crossing;
    }

    public void setCrossing(Integer crossing) {
        this.crossing = crossing;
    }

    public void addWarning(UpdateWarning w) {
        warnings.add(w);
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public boolean isApplied() {
        return applied;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
    }
}
