package kz.bgm.platform.service;

import kz.bgm.platform.items.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemorycatalog implements CatalogStorage {

    public static final boolean DEBUG = false;

    private Map<String, List<Track>> authItemsMap;
    private Map<String, List<Track>> commonItemsMap;


    public InMemorycatalog() {
        authItemsMap = new HashMap<String, List<Track>>();
        commonItemsMap = new HashMap<String, List<Track>>();
    }

    public void storeInCatalog(List<Track> trackList, boolean common) {
        for (Track i : trackList) {
            addItem(i, common);
        }
    }

    @Override
    public void storeInCatalog(List<Track> trackList, String catalog) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addItem(Track track, boolean common) {
        String artist = track.getArtist();
        if (artist == null || "".equals(artist)) {
            return;
        }

        artist = artist.toLowerCase();

        Map<String, List<Track>> map = common ? commonItemsMap : authItemsMap;

        List<Track> items = map.get(artist);
        if (items == null) {
            items = new ArrayList<Track>();
            map.put(artist, items);
        }

        items.add(track);
    }

    public Track search(String author, String song) {
        return this.search(author, song, false);
    }

    public Track search(String author, String song, boolean common) {
        if (DEBUG) {
            System.out.println("Searching for: '" + author + "': '" + song + "'");
        }

        Map<String, List<Track>> map = common ? commonItemsMap : authItemsMap;
        List<Track> comps = map.get(author.toLowerCase());

        if (comps != null) {
            if (DEBUG) {
                StringBuilder buf = new StringBuilder();
                for (int i = 0; i < comps.size(); i++) {
                    Track comp = comps.get(i);
                    buf.append(comp.getName());
                    if (i < comps.size() - 1) {
                        buf.append(", ");
                    }
                }
                System.out.println("Found author '" + author + "': [" + buf + "]");
            }

            for (Track comp : comps) {
                if (comp.getName().equalsIgnoreCase(song)) {
                    return comp;
                }
            }
        }

        return null;
    }

    @Override
    public Float getRoyalty(int catalogId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public List<Track> searchBySongName(String songName) {
        throw new IllegalStateException("Not implemented!");
    }

    @Override
    public List<Track> searchByArtistLike(String songName) {
        throw new IllegalStateException("Not implemented!");
    }

    @Override
    public List<Track> searchByArtist(String songName) {
        throw new IllegalStateException("Not implemented!");
    }


    @Override
    public List<Track> search(String value) {
        throw new IllegalStateException("Not implemented!");
    }


}
