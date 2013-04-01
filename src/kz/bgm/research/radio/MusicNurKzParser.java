package kz.bgm.research.radio;

import kz.bgm.platform.parsers.utils.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicNurKzParser {

    public static final int MAX_PAGES = 5000;

    public static List<String> parseLetters() throws IOException {
        List<String> pages = new ArrayList<String>();

        String url = "http://music.nur.kz/artists-T";
        Document doc = Jsoup.connect(url).get();
        Elements els = doc.select("div.letters > a");
        for (Element e : els) {
//            String title = e.attr("title");
            String href = e.attr("href");
//            System.out.println(title + "\t" + href);
            pages.add(href);
        }
        return pages;
    }


    public static List<String> parseAuthors(String url) throws IOException {
        List<String> authors = new ArrayList<String>();


        for (int i = 0; i < MAX_PAGES; i ++) {
            Document doc = Jsoup.connect(url).get();

            Element navMenu = doc.select("ul.prev-next").first();
            String pageNo = navMenu.select("li.active > a > span").text();
            System.out.println("Page no: " + pageNo);

            Elements els = doc.select("ul.list-genre > li > div.text > h3 > a");
            for (Element e : els) {
//            String href = e.attr("href");
                String author = e.text();
                authors.add(author);
            }

            Elements lis = navMenu.select("li");
            boolean foundActive = false;
            String nextPage = null;
            for (Element li : lis) {
                if (foundActive) {
                    nextPage = li.select("a").first().attr("href");
                    break;
                }
                if (li.classNames().contains("active")) {
                    foundActive = true;
                }
            }

            if (nextPage == null) break;
            url = "http://music.nur.kz" + nextPage;
        }


        return authors;
    }


    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            System.err.println("Outfile required!");
            return;
        }

        String outfile = args[0];
//        String outfile = "/Users/ancalled/Documents/tmp/1/MusicNurKz3.txt";


        List<String> pages = parseLetters();

        List<String> authors = new ArrayList<String>();
//        authors.addAll(parseAuthors("http://music.nur.kz/artists-A"));

//        for (String page : pages) {
//            authors.addAll(parseAuthors(page));
//        }

        System.out.println("Got " + authors.size() + " authors");
        FileUtils.writeLinesToFile(outfile, authors);
    }
}
