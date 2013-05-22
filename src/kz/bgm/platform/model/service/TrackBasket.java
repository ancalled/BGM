package kz.bgm.platform.model.service;

import kz.bgm.platform.model.domain.Track;

import java.util.ArrayList;
import java.util.List;

public class TrackBasket {

    private List<Track> trackList;

    public TrackBasket() {
        trackList = new ArrayList<>();
    }

    public void addTrack(Track track) {
        trackList.add(track);
    }

    public void addTrack(List<Track> tracks) {
        this.trackList.addAll(tracks);
    }

    public List<Track> getTracks() {
        return trackList;
    }

    public void removeTrack(long trackId) {
        for (Track t : trackList) {
            if (t.getId() == trackId) {
                trackList.remove(t);
                break;
            }
        }
    }
}
