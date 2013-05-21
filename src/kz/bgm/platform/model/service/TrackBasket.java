package kz.bgm.platform.model.service;

import java.util.ArrayList;
import java.util.List;

public class TrackBasket {

    private List<Long> trackList;

    public TrackBasket() {
        trackList = new ArrayList<>();
    }

    public void addTrack(long trackId) {
        trackList.add(trackId);
    }

    public void addTrack(List<Long> idList) {
        trackList.addAll(idList);
    }

    public List<Long> getTracks() {
        return trackList;
    }

    public void removeTrack(long trackId) {
        trackList.remove(trackId);

    }
}
