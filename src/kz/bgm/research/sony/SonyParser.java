package kz.bgm.research.sony;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.Map;


public class SonyParser {

    public static final String APP_HOME = System.getProperty("user.dir");

    public SonyParser() {
        String jksPath = APP_HOME + "/data/research/www.sonyatv.com.jks";
        System.out.println(jksPath);


        System.setProperty("javax.net.ssl.trustStore", jksPath);
        System.setProperty("javax.net.ssl.trustStorePassword", "Ufd234");
    }


    public void parse() throws IOException {
        String url = "https://www.sonyatv.com/en-ru/";

        Connection.Response res = Jsoup
                .connect(url)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://www.google.com")
                .method(Connection.Method.GET)
                .execute();

        Document doc = res.parse();

        System.out.println(doc.baseUri());
        System.out.println("---------------");
//        System.out.println(doc.text());
        System.out.println(doc.html());
        System.out.println("---------------");
        System.out.println();

        Map<String, String> cookies = res.cookies();
        if (cookies != null) {
            System.out.println("Cookies: ");
            System.out.println("---------------");
            for (String key: cookies.keySet()) {
                System.out.println(key + ": " + cookies.get(key));
            }
            System.out.println("---------------");
            System.out.println();
        }



        Elements meta = doc.select("html head meta");
        if (meta.attr("http-equiv").contains("REFRESH")) {
            String redirUrl = meta.attr("content").split("=")[1];
            System.out.println("Following redirect: " + redirUrl);

            doc = Jsoup.connect(redirUrl).get();
            System.out.println(doc.baseUri());
        }

        Elements els = doc.select("dib.home_top_r_form_field_l");
        System.out.println("Found " + els.size() + " login blocks");
    }

    public void logon(String login, String pass) throws IOException {
        String url = "https://www.sonyatv.com/dampinterop/userLogin.php";

        Connection.Response res = Jsoup
                .connect(url)
                .data("email", login, "passField", pass)
                .method(Connection.Method.POST)
                .execute();

        Map<String, String> cookies = res.cookies();

    }


    public static void main(String[] args) throws IOException {
        SonyParser parser = new SonyParser();
        parser.parse();

    }


}
