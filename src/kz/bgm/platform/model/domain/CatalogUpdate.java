package kz.bgm.platform.model.domain;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CatalogUpdate {

    public static final String DEFAULT_ENCODING = "utf8";
    public static final String DEFAULT_SEPATRATOR = ";";
    public static final String DEFAULT_NEWLINE = "\n";
    public static final String NOT_ENCLOSED = "";
    public static final int DEFAULT_FROM_LINE = 0;

    public static enum Status {NONE, OK, HAS_WARNINGS}

    private Long id;
    private Date whenUpdated;
    private String filePath;
    private String fileName;

    private String encoding = DEFAULT_ENCODING;
    private String separator = DEFAULT_SEPATRATOR;
    private String enclosedBy = NOT_ENCLOSED;
    private String newline = DEFAULT_NEWLINE;
    private Integer fromLine = DEFAULT_FROM_LINE;

    private Long catalogId;
    private Status status = Status.NONE;
    private int tracks = 0;
    private int crossing;
    private int newTracks = 0;
    private int changedTracks = 0;
    private int rateChangedTracks = 0;
    private boolean applied = false;

    private final List<UpdateWarning> warnings = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Date getWhenUpdated() {
        return whenUpdated;
    }

    public void setWhenUpdated(Date whenUpdated) {
        this.whenUpdated = whenUpdated;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getEnclosedBy() {
        return enclosedBy;
    }

    public void setEnclosedBy(String enclosedBy) {
        this.enclosedBy = enclosedBy;
    }

    public String getNewline() {
        return newline;
    }

    public void setNewline(String newline) {
        this.newline = newline;
    }

    public Integer getFromLine() {
        return fromLine;
    }

    public void setFromLine(Integer fromLine) {
        this.fromLine = fromLine;
    }

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

    public int getTracks() {
        return tracks;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
    }

    public int getCrossing() {
        return crossing;
    }

    public void setCrossing(int crossing) {
        this.crossing = crossing;
    }

    public int getNewTracks() {
        return newTracks;
    }

    public void setNewTracks(int newTracks) {
        this.newTracks = newTracks;
    }

    public int getChangedTracks() {
        return changedTracks;
    }

    public void setChangedTracks(int changedTracks) {
        this.changedTracks = changedTracks;
    }

    public int getRateChangedTracks() {
        return rateChangedTracks;
    }

    public void setRateChangedTracks(int rateChangedTracks) {
        this.rateChangedTracks = rateChangedTracks;
    }

    public void addWarning(UpdateWarning w) {
        warnings.add(w);
    }



    public boolean isApplied() {
        return applied;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
    }
}
