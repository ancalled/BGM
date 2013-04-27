package kz.bgm.platform.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;

public class FileUtils {

    public static String readToString(String filename) throws IOException {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(filename);
            FileChannel fc = fin.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            return Charset.defaultCharset().decode(bb).toString();
        } finally {

            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String[] readByLines(String filename) throws IOException {
        String contents = readToString(filename);
        return contents.split("\n");
    }

    public static void writeLinesToFile(String filename, List<String> list) throws IOException {
        StringBuilder buf = new StringBuilder();
        for (String line : list) {
            buf.append(line).append("\n");
        }

        final byte[] mesBytes = buf.toString().getBytes(Charset.forName("UTF-8"));

        final RandomAccessFile raf = new RandomAccessFile(filename, "rw");
        raf.seek(raf.length());
        final FileChannel fc = raf.getChannel();
        final MappedByteBuffer mbf = fc.map(FileChannel.MapMode.READ_WRITE, fc.
                position(), mesBytes.length);
        mbf.put(mesBytes);

        raf.close();
    }
}
