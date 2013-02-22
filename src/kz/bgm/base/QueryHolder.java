package kz.bgm.base;


import kz.bgm.items.Track;

public class QueryHolder {

    private static final String COLUMN_SONG_NAME = "song_name";
    private static final String COLUMN_UID = "uid";
    private static final String COLUMN_MUSIC_AUTORS = "music_autors";
    private static final String COLUMN_LIRICS_AUTORS = "lirics_autors";
    private static final String COLUMN_ARTIST = "artist";
    private static final String COLUMN_MOBILE_RATE = "mobile_rate";
    private static final String COLUMN_PUBLIC_RATE = "public_rate";

    public static final String TABLE_NMI_SONGS = "nmi_songs";
    public static final String TABLE_PMI_SONGS = "pmi_songs";
    public static final String TABLE_PMI_SONGS_COM = "pmi_songs_com";
    public static final String TABLE_NMI_SONGS_COM = "nmi_songs_com";
    public static final String TABLE_WRCH_SONGS = "wrch_songs";


    public static String makeQueryListOfSongs(String songName) {
        if (songName == null) {
            return null;
        }
        String bigQuery = "(" + querySelectBySong(TABLE_NMI_SONGS, songName) + ") UNION " +
                "(" + querySelectBySong(TABLE_PMI_SONGS, songName) + ") UNION " +
                "(" + querySelectBySong(TABLE_NMI_SONGS_COM, songName) + ") UNION " +
                "(" + querySelectBySong(TABLE_PMI_SONGS_COM, songName) + ") UNION " +
                "(" + querySelectBySong(TABLE_WRCH_SONGS, songName) + ")";

        return bigQuery;
    }

    public static String querySelectBySong(String table, String songName) {
        return querySelectSongTable(table, songName, COLUMN_SONG_NAME);
    }

    public static String querySelectSongTable(String table, String content, String column) {
        if (column == null || content == null || table == null) {
            throw new IllegalArgumentException("Cant make query with null arguments");
        }
        return "select * from " + table +
                " where " + column + "='" + content + "'";
    }

    public static String queryAllSongsByTable(String tableName) {
        return "select * from " + tableName;
    }

    public static String queryInsertToSongTable(String table, Track t) {

        String songName;
        if (t.getComposition() != null) {
            songName = "'" + normalize(t.getComposition()) + "'";
        } else {
            songName = "' '";
        }

        String musAut;
        if (t.getMusicAuthors() != null) {
            musAut = "'" + normalize(t.getMusicAuthors()) + "'";
        } else {
            musAut = "' '";
        }

        String lirAuth;
        if (t.getLyricsAuthors() != null) {
            lirAuth = "'" + normalize(t.getLyricsAuthors()) + "'";
        } else {
            lirAuth = "' '";
        }

        String art;
        if (t.getLyricsAuthors() != null) {
            art = "'" + normalize(t.getArtist()) + "'";
        } else {
            art = "' '";
        }

        return "insert into " + table + "(" +
                COLUMN_UID + "," +
                COLUMN_SONG_NAME + "," +
                COLUMN_MUSIC_AUTORS + "," +
                COLUMN_LIRICS_AUTORS + "," +
                COLUMN_ARTIST + "," +
                COLUMN_MOBILE_RATE + "," +
                COLUMN_PUBLIC_RATE +
                ")" +
                "values (" +
                t.getCode() + "," +
                songName + "," +
                musAut + "," +
                lirAuth + "," +
                art + "," +
                t.getMobileRate() + "," +
                t.getPublicRate() + ")";
    }

    //todo изменить нормализацию запроса а то из зп нее пропадают артисты !
    private static String normalize(String s) {
        s = s.replaceAll("'", "");
        s = s.replaceAll("\\\\", "|");
        s = s.replaceAll("/", "|");
        return s;
    }

}
