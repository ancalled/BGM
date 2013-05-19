package kz.bgm.platform.test.lucene;

import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.LuceneSearch;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LuceneSearchTest {

    private LuceneSearch luceneSearch;

    @Before
    public void init() throws IOException {
        luceneSearch = LuceneSearch.getInstance();
        String indexDir = "./out/test_index_2";

        Files.deleteIfExists(Paths.get(indexDir));

        List<Track> tracks = loadTracks("./data/test-tracks.tsv");

        luceneSearch.index(tracks, indexDir);
        luceneSearch.initSearcher(indexDir);

    }

    @Test
    public void test() throws Exception {
//        Stayin Alive	Bee Gees
//        List<Long> ids = luceneSearch.search("Братья Грим", "Кустурица");
        List<Long> ids = luceneSearch.search("Bee Gees", "Stayin Alive");
//        List<Long> ids = luceneSearch.search("Братья Грим, Кустрица");

        System.out.println(Arrays.toString(ids.toArray()));
    }


    public static List<Track> loadTracks(String filename) {
        List<Track> tracks = new ArrayList<>();

        Path file = Paths.get(filename);
        try (InputStream in = Files.newInputStream(file);
             BufferedReader reader =
                     new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split("\t");

                if (row.length == 5) {
                    Track track = new Track();
                    track.setId(Long.parseLong(row[0]));
                    track.setCode(row[1]);
                    track.setName(row[2]);
                    track.setArtist(row[3]);
                    track.setComposer(row[4]);

                    tracks.add(track);
                }
            }

        } catch (IOException e) {
            System.err.println(e);
        }

        return tracks;
    }
}
