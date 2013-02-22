package kz.bgm.parsers;

import kz.bgm.items.Track;
import org.apache.poi.ss.usermodel.Row;


public interface RowParser {

   void parse(Row row, Track comp);
}
