package kz.bgm.platform.test.lucene;


import kz.bgm.platform.model.domain.SearchResult;
import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.model.service.LuceneSearch;
import kz.bgm.platform.utils.LuceneUtil;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LuceneTest {


    public static List<String> readByLines(String filename) {
        List<String> lines = new ArrayList<>();
        Charset charset = Charset.forName("UTF-8");
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename), charset)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

        return lines;
    }

    public static final List<Character> REPLACE_CHARS = Arrays.asList('!', '\'', '/', '\\');

    public static String normalize(String text) {
        StringBuilder b = new StringBuilder();
        for (char c: text.toCharArray()) {
            if (REPLACE_CHARS.contains(c)) {
                b.append("\\").append(c);
            } else if (Character.isAlphabetic(c) || Character.isDigit(c) || Character.isSpaceChar(c)) {
                b.append(c);
            }
        }
        return b.toString();
    }


    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Not enough params! Expected csv file!");
            return;
        }

        String reportFile = args[0];
        List<String> lines = readByLines(reportFile);

        LuceneSearch luceneSearch = LuceneSearch.getInstance();
        luceneSearch.initSearcher(LuceneUtil.INDEX_DIR);

        try {
            LuceneUtil.initDatabase("db.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        CatalogStorage catalogStorage = CatalogFactory.getStorage();

        int processed = 0;
        int foundUnique = 0;
        int found = 0;

        for (String line : lines) {
            if (!line.contains(":")) continue;

            String[] splitted = line.split(":[\\s]?");
            if (splitted.length < 2) continue;

            String artist = splitted[0].trim();
            String track = splitted[1].trim();

            if (artist.isEmpty() || track.isEmpty()) continue;

            artist = normalize(artist);
            track = normalize(track);

            StringBuilder buf = new StringBuilder();
            buf.append(artist).append(": ").append(track).append("\t\t");

            try {
                List<SearchResult> res = luceneSearch.search(artist, null, track, 100);
                if (res.isEmpty()) {
                    buf.append("[Not Found]");

                } else {
                    for (SearchResult r : res) {
                        buf.append("[score: " + r.getScore() + ", id: " + r.getTrackId());
                        Track t = catalogStorage.getTrack(r.getTrackId());
                        if (t != null) {
                            buf.append(", artist: '" + t.getArtist() + "'" +
                                    ", track: '" + t.getName() + "'" +
                                    ", composer: '" + t.getComposer() + "'" +
                                    ", catalog: '" + t.getCatalog() + "'" +
                                    ", code: '" + t.getCode() + "'" +
                                    ", mobileShare: '" + t.getMobileShare() + "'"
                            );
                        }

                        buf.append("] ");
                        found++;
                    }

                    foundUnique++;
                }

                System.out.println(buf.toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }

            processed++;

        }

        System.out.println();
        System.out.println("-------------------------------------------------");
        System.out.println("Processed: " + processed);
        System.out.println("Found: " + found);
        System.out.println("Found unique: " + foundUnique);

    }


}
