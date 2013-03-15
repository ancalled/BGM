package kz.bgm.platform.parsers;

import kz.bgm.platform.items.Track;
import org.apache.poi.ss.usermodel.Row;


public interface RowParser {

   void parse(Row row, Track comp);
}
