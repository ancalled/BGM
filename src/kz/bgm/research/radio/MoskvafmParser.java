package kz.bgm.research.radio;

import kz.bgm.platform.parsers.utils.FileUtils;
import kz.bgm.platform.items.ReportItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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


    public static List<String> parseStationHits(String station) throws IOException {
        List<String> songs = new ArrayList<String>();
        String urlTempl = "http://www.moskva.fm/stations/%station%/top100";
        String url = urlTempl.replace("%station%", station);
        Document doc = Jsoup.connect(url).get();
        Elements els = doc.select("ul.msk-colorlist-charts > li");
        for (Element e : els) {
            String artist = getText(e, "span.artist");
            String song = getText(e, "span.song");
            songs.add(artist + ": " + song);
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
        List<ReportItem> result = new ArrayList<ReportItem>();
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
        List<String> result = new ArrayList<String>();

        List<String> names = parseStations();
        for (String name : names) {
            List<String> songs = parseStationHits(name);
            for (String song : songs) {
                if (!result.contains(song)) {
                    result.add(song);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
//        parseHits();

//        List<String> result = new ArrayList<String>();
//
//        List<String> names = parseStations();
//        for (String name : names) {
//            List<String> songs = parseStationHits(name);
//            for (String song : songs) {
//                if (!result.contains(song)) {
//                    result.add(song);
//                }
//            }
//        }
//
//        for (String song : result) {
//            System.out.println(song);
//        }

        System.out.println("Test!");
    }
}
