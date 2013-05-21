package kz.bgm.platform.model.domain;


import java.util.ArrayList;
import java.util.List;

public class CatalogUpdate {

    public static enum Status {OK, HAS_ERRORS}

    private Long catalogId;
    private Status status;
    private Integer tracks;
    private String file;

    private final List<String> errors = new ArrayList<>();


    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public List<String> getErrors() {
        return errors;
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

    public void addError(String er) {
        errors.add(er);
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
