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

        System.out.println("File exists: " + new File(jksPath).exists());

        System.setProperty("javax.net.ssl.trustStore", jksPath);
        System.setProperty("javax.net.ssl.trustStorePassword", "Ufd234");
    }


    public void parse() throws IOException {
        String url = "https://www.sonyatv.com";
        Document doc = Jsoup.connect(url).get();

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
