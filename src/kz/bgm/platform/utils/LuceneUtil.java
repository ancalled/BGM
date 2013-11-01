package kz.bgm.platform.utils;


import kz.bgm.platform.model.domain.SearchResult;
import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.model.service.LuceneSearch;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class LuceneUtil {

    public static final String APP_DIR = System.getProperty("user.dir");
    public static final String INDEX_DIR = APP_DIR + "/lucen-indexes";

    public static final String BASE_NAME = "base.name";
    public static final String BASE_LOGIN = "base.login";
    public static final String BASE_PASS = "base.pass";
    public static final String BASE_HOST = "base.host";
    public static final String BASE_PORT = "base.port";

    public static final int RESULT_LIMIT = 3;
    public static final float RESULT_MIN_SCORE = 1.0f;
    public static final String IN_FIELD_SEP = ",";
    public static final String OUT_FIELD_SEP = ";";

    private final CatalogStorage catalogStorage;
    private final LuceneSearch luceneSearch;


    public LuceneUtil() throws IOException {
        try {
            initDatabase("db.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }

        catalogStorage = CatalogFactory.getStorage();
        luceneSearch = LuceneSearch.getInstance();
        luceneSearch.initSearcher(INDEX_DIR);
    }


    public void search(String artist, String authors, String track) throws IOException, ParseException {
        List<SearchResult> res = luceneSearch.search(artist, authors, track, 100);

        for (SearchResult r : res) {
            System.out.println("[" + r.getScore() + "] id: " + r.getTrackId());
            Track t = catalogStorage.getTrack(r.getTrackId());
            if (t != null) {
                System.out.println("\tartist: '" + t.getArtist() + "'" +
                        "\n\ttrack: '" + t.getName() + "'" +
                        "\n\tcomposer: '" + t.getComposer() + "'" +
                        "\n\tcatalog: '" + t.getCatalog() + "'" +
                        "\n\tcode: '" + t.getCode() + "'" +
                        "\n\tmobileShare: '" + t.getMobileShare() + "'"
                );
                System.out.println();
            }
        }
    }


    public void bulkSearch(String infile, String outfile, boolean withHeader, int artistRow, int trackRow)
            throws IOException {
        if (infile == null || outfile == null || artistRow < 0 || trackRow < 0) return;

        List<String> lines = readFilesByLines(Paths.get(infile));

        StringBuilder buf = new StringBuilder();

        int idx = 0;
        for (String line : lines) {

            String[] sourceFields = line.split(IN_FIELD_SEP);
            if (sourceFields.length <= trackRow || sourceFields.length <= artistRow) {
                continue;
            }

            String track = sourceFields[trackRow];
            String artist = sourceFields[artistRow];

            List<SearchResult> res = null;
            if (idx > 0 || !withHeader) {
                try {
                    res = luceneSearch.search(artist, artist, track, RESULT_LIMIT * 3);
                } catch (ParseException e) {
                    System.err.println(e.getMessage());
                }
            }

            if (withHeader && idx == 0) {
                buf.append(toRow(sourceFields))
                        .append(toRow(
                                "Detected Artist: Track",
                                "Code",
                                "Mobile Share",
                                "Public Share",
                                "Catalog"
                        ));

            } else {
                buf.append(toRowWrapped(sourceFields));
            }


            System.out.println(artist + ": " + track);

            if (res != null) {

                for (SearchResult r : filterByHighScore(res, RESULT_LIMIT)) {
                    if (r.getScore() < RESULT_MIN_SCORE) continue;

                    Track t = catalogStorage.getTrack(r.getTrackId());
                    if (t != null) {
//                        buf.append(toRowWrapped(
//                                t.getArtist() + ": " + t.getName(),
//                                t.getCode(),
//                                t.getMobileShare(),
//                                t.getPublicShare(),
//                                t.getCatalog()));
                        buf.append(toRowWrapped(
                                t.getCode(),
                                t.getCatalog()));

                        System.out.println("[" + r.getScore() + "] id: " + r.getTrackId());
                        System.out.println("\tartist: '" + t.getArtist() + "'" +
                                "\n\ttrack: '" + t.getName() + "'" +
                                "\n\tcomposer: '" + t.getComposer() + "'" +
                                "\n\tcatalog: '" + t.getCatalog() + "'" +
                                "\n\tcode: '" + t.getCode() + "'" +
                                "\n\tmobileShare: '" + t.getMobileShare() + "'"
                        );
                        System.out.println();
                    }
                }
            }

            System.out.println("----------------------------------------------------");

            buf.append("\n")/*.append("\r")*/;
            idx++;
        }


        writeToFile(outfile, buf.toString());

    }


    public static void initDatabase(String propsFile) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(propsFile));

        String dbHost = props.getProperty(BASE_HOST);
        String dbPort = props.getProperty(BASE_PORT);
        String dbName = props.getProperty(BASE_NAME);
        String dbLogin = props.getProperty(BASE_LOGIN);
        String dbPass = props.getProperty(BASE_PASS);

        System.out.println("Initializing data storage...");
        CatalogFactory.initDBStorage(dbHost, dbPort, dbName, dbLogin, dbPass);
    }


    public static List<String> readFilesByLines(Path path) throws IOException {

        List<String> res = new ArrayList<>();
        BufferedReader reader =
                Files.newBufferedReader(path, Charset.forName("utf-8"));
        String line;
        while ((line = reader.readLine()) != null) {
            res.add(line);
        }
        return res;
    }


    public static List<SearchResult> filterByHighScore(List<SearchResult> results, int limit) {
        if (results == null) return null;
        if (results.isEmpty()) return Collections.emptyList();

        List<SearchResult> filtered = new ArrayList<>();

        SearchResult first = results.get(0);

        filtered.add(first);

        for (int i = 1; i < Math.min(results.size(), limit); i++) {
            SearchResult sr = results.get(i);
            if (sr.getScore() < first.getScore()) {
                break;
            }
            filtered.add(sr);
        }

        return filtered;
    }

    public static void writeToFile(String oufile, String content) throws IOException {

        BufferedWriter writer = Files.newBufferedWriter(Paths.get(oufile), Charset.forName("utf-8"));
        writer.append(content);
        writer.flush();
    }

    public static String wrap(String value) {
        return "'" + value.replace("'", "").replace(OUT_FIELD_SEP, "") + "'";
    }

    public static String toRow(String... fields) {
        StringBuilder buf = new StringBuilder();
        for (String field : fields) {
            buf.append(field).append(OUT_FIELD_SEP);
        }
        return buf.toString();
    }

    public static String toRowWrapped(Object... fields) {
        StringBuilder buf = new StringBuilder();
        for (Object field : fields) {
            String val;
            if (field instanceof String) {
                val = wrap((String)field);
            } else if (field instanceof Number) {
                val = field.toString();
            } else {
                val = "";
            }

            buf.append(val).append(OUT_FIELD_SEP);
        }
        return buf.toString();
    }


    public static void main1(String[] args) throws IOException, ParseException {

        if (args.length < 1) {
            System.out.println("Expected:\nsearch-track.sh artist: composition");
            return;
        }

        StringBuilder buf = new StringBuilder();
        for (String a : args) {
            buf.append(a);
            buf.append(" ");
        }
        String query = buf.toString();
        query = query.substring(0, query.length() - 1);

        String[] splitted = query.split(":[\\s]?");
        String artist = splitted[0];
        String authors = splitted.length > 1 ? splitted[1] : null;
        String track = splitted.length > 2 ? splitted[2] : null;

        System.out.println("Queried artist: '" + artist + "'" + ", track: '" + track + "'");

        LuceneUtil util = new LuceneUtil();
        util.search(artist, authors, track);

//        new LuceneUtil().rebuildIndex();

    }


    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Not enough params!");
            System.out.println("Expected: $IN_FILE $OUT_FILE");
            return;
        }

        String infile = args[0];
        String outfile = args[1];
        boolean withHeader = true;
        int artistRow = 2;
        int trackRow = 1;


        LuceneUtil util = new LuceneUtil();
        util.bulkSearch(infile, outfile, withHeader, artistRow, trackRow);

    }
}
