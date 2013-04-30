package kz.bgm.research.radio;

import kz.bgm.platform.utils.FileUtils;
import kz.bgm.platform.model.domain.ReportItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MoskvafmParser {



    public static List<String> parseStations() throws IOException {
        List<String> names = new ArrayList<String>();

        String url = "http://www.moskva.fm/stations";
        Document doc = Jsoup.connect(url).get();
        Elements els = doc.select("div.msk-thumbnail-station");
        for (Element e : els) {
            Element aEl = e.select("a.station").first();
            if (aEl != null) {
                String href = aEl.attr("href");
                String name = href.replace("http://www.moskva.fm/stations/", "");
                names.add(name);
            }
        }
        return names;
    }


    public static List<Song> parseStationHits(String station) throws IOException {
        List<Song> songs = new ArrayList<>();
        String urlTempl = "http://www.moskva.fm/stations/%station%/top100";
        String url = urlTempl.replace("%station%", station);
        Document doc = Jsoup.connect(url).get();
        Elements els = doc.select("ul.msk-colorlist-charts > li");
        int place = 1;
        for (Element e : els) {
            String artist = getText(e, "span.artist");
            String track = getText(e, "span.song");
            Song song = new Song();

            song.setArtist(artist);
            song.setTrack(track);
            song.setStation(station);
            song.setPlace(place++);
            songs.add(song);
        }
        return songs;
    }

    public static void parseHits() throws IOException {
        String url = "http://www.moskva.fm/charts/top100";
        Document doc = Jsoup.connect(url).get();
        String title = doc.title();
        System.out.println("title = " + title);
        Elements els = doc.select("ul.msk-colorlist-charts > li");
        for (Element e : els) {
            String artist = getText(e, "span.artist");
            String song = getText(e, "span.song");
            System.out.println(artist + ": " + song);
        }
    }

    public static String getText(Element parent, String selector) {
        Elements els = parent.select(selector);
        if (els != null) {
            Element first = els.first();
            if (first != null) {
                return first.text().trim();
            }
        }
        return null;
    }

    public static List<ReportItem> parseReport(String filename) throws IOException {

        String[] lines = FileUtils.readByLines(filename);
        List<ReportItem> result = new ArrayList<>();
        for (String line : lines) {
            String[] splitted = line.split(": ");
            String author = splitted[0].trim();
            String song = splitted[1].trim();
            ReportItem ri = new ReportItem();
            ri.setArtist(author);
            ri.setCompisition(song);
            result.add(ri);
        }

        return result;
    }


    public static void harvestData() throws IOException {
        List<Song> result = new ArrayList<>();

        List<String> names = parseStations();
        for (String name : names) {
            List<Song> songs = parseStationHits(name);
            for (Song song : songs) {
                if (!result.contains(song)) {
                    result.add(song);
                }
            }
        }
    }

    public static class Song {
        private String track;
        private String artist;
        private String station;
        private int place;

        public String getTrack() {
            return track;
        }

        public void setTrack(String track) {
            this.track = track;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getStation() {
            return station;
        }

        public void setStation(String station) {
            this.station = station;
        }

        public int getPlace() {
            return place;
        }

        public void setPlace(int place) {
            this.place = place;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Song)) return false;
            Song song = (Song) obj;

            return song.getArtist().equalsIgnoreCase(artist) &&
                    song.getTrack().equalsIgnoreCase(track);
        }
    }

    public static void main(String[] args) throws IOException {
//        parseHits();

        List<Song> result = new ArrayList<>();

        List<String> names = parseStations();
        for (String name : names) {
            List<Song> songs = parseStationHits(name);
            for (Song song : songs) {
                if (!result.contains(song)) {
                    result.add(song);
                }
            }
        }

        Random r = new Random(System.currentTimeMillis());

        for (Song song : result) {

            int qty = (100 / song.getPlace()) * r.nextInt(10);
            System.out.println(song.getArtist() + "|" + song.getTrack() + "|" + qty);
        }

    }
}
