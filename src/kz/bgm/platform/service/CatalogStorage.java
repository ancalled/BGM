package kz.bgm.platform.service;

import kz.bgm.platform.items.Track;

import java.util.List;

public interface CatalogStorage {

   void storeInCatalog(List<Track> trackList, boolean common);

   void addItem(Track track, boolean common);

   Track search(String author, String song);

   Track search(String author, String song, boolean common);


   List<Track> searchBySongName(String songName);

   List<Track> searchByArtistLike(String artist);

   List<Track> searchByArtist(String artist);

   List<Track> search(String value);

}
