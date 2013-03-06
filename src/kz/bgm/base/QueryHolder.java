package kz.bgm.base;


public class QueryHolder {

    public static final String COLUMN_SONG_NAME = "song_name";
    public static final String COLUMN_UID = "uid";
    public static final String COLUMN_MUSIC_AUTORS = "music_autors";
    public static final String COLUMN_LIRICS_AUTORS = "lirics_autors";
    public static final String COLUMN_ARTIST = "artist";
    public static final String COLUMN_MOBILE_RATE = "mobile_rate";
    public static final String COLUMN_PUBLIC_RATE = "public_rate";

    public static final String TABLE_NMI_SONGS = "nmi_songs";
    public static final String TABLE_PMI_SONGS = "pmi_songs";
    public static final String TABLE_PMI_SONGS_COM = "pmi_songs_com";
    public static final String TABLE_NMI_SONGS_COM = "nmi_songs_com";
    public static final String TABLE_WRCH_SONGS = "wrch_songs";
    public static final String COLUMN_ALL_MUSIC_COMPOSER = "composer";
    public static final String COLUMN_ALL_MUSIC_ARTIST = "artist";
    public static final String CONTROLLED_MECH_SHARE = "controlled_mech_share";
    public static final String COLLECT_MECH_SHARE = "collect_mech_share";
    public static final String COLUMN_PUBLISHER = "publisher";


    public static final String TABLE_ALL_MUSIC = "allmusic";
    public static final String CONTROLLED_PUB1 = "controlled_pub";
    public static final String COMMENT = "comment";


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

    public static String queryInsertToSongTable(String table/*, Track t*/) {

        return " insert into " + table + "(" +
                COLUMN_UID + "," +
                COLUMN_SONG_NAME + "," +
                COLUMN_MUSIC_AUTORS + "," +
                COLUMN_LIRICS_AUTORS + "," +
                COLUMN_ARTIST + "," +
                COLUMN_MOBILE_RATE + "," +
                COLUMN_PUBLIC_RATE +
                ") values (?,?,?,?,?,?,?)";



    }

    public static String queryInsertToAllMusic(String table) {

        String ask =  " insert into " + table + "(" +
                COLUMN_UID + "," +
                COLUMN_SONG_NAME + "," +
                COLUMN_ALL_MUSIC_COMPOSER + "," +
                COLUMN_ARTIST + "," +
                CONTROLLED_MECH_SHARE + "," +
                COLLECT_MECH_SHARE + "," +
                COLUMN_PUBLISHER+","+
                COMMENT +
                ") values (?,?,?,?,?,?,?,?)";
        System.out.println(ask);
        return ask;
    }




    public static String makeQueryAuthorsByTitleFromAllBase() {
        return "(" + queryAuthors(TABLE_NMI_SONGS) + ") UNION " +
                "(" + queryAuthors(TABLE_PMI_SONGS) + ") UNION " +
                "(" + queryAuthors(TABLE_NMI_SONGS_COM) + ") UNION " +
                "(" + queryAuthors(TABLE_PMI_SONGS_COM) + ") UNION " +
                "(" + queryAuthors(TABLE_WRCH_SONGS) + ")";
    }

    public static String queryAuthors(String table) {
        return "select " + COLUMN_LIRICS_AUTORS + "," + COLUMN_MUSIC_AUTORS + " from " + table +
                " where " + COLUMN_SONG_NAME + " like " + "?";
    }

    public static String makeQueryAuthorsByUidFromAllBase() {
        return "(" + queryAuthorsByUid(TABLE_NMI_SONGS) + ") UNION " +
                "(" + queryAuthorsByUid(TABLE_PMI_SONGS) + ") UNION " +
                "(" + queryAuthorsByUid(TABLE_NMI_SONGS_COM) + ") UNION " +
                "(" + queryAuthorsByUid(TABLE_PMI_SONGS_COM) + ") UNION " +
                "(" + queryAuthorsByUid(TABLE_WRCH_SONGS) + ")";
    }


    public static String queryAuthorsByUid(String table) {
        return "select " + COLUMN_LIRICS_AUTORS + "," + COLUMN_MUSIC_AUTORS + " from " + table +
                " where " + COLUMN_UID + "= ?";
    }


}
