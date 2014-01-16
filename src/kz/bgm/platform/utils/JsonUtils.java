package kz.bgm.platform.utils;


import org.apache.commons.fileupload.FileItem;

import java.util.List;

public class JsonUtils {


    public static String getParam(List<FileItem> fields, String name) {
        return getParam(fields, name, null);
    }


    public static String getParam(List<FileItem> fields, String name, String defaultValue) {
        for (FileItem item : fields) {
            if (item.isFormField()) {
                if (name.equals(item.getFieldName())) {
                    return item.getString();
                }
            }
        }

        return defaultValue;
    }

}
