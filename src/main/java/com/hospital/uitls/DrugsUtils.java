package com.hospital.uitls;

import java.util.Iterator;
import java.util.Map;

public class DrugsUtils {
    public static String vaild(Map map) {
        Iterator entries = map.entrySet().iterator();
        String ids = "";
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            String[] _key = key.split("_");
            if (_key.length > 1) {
                if (_key[1].equals("number") && value != null && !value.equals("")) {
                    ids += (_key[0] + "@" + value) + ",";
                }
            }
        }
        if (ids.equals("")) {
            return "";
        }
        return ids.substring(0, ids.length() - 1);
    }

    public static String vaild2(Map map) {
        Iterator entries = map.entrySet().iterator();
        String ids = "";
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (key.split("_")[0].equals("option") && value != null && !value.equals("")) {
                ids += value + ",";
            }
        }
        if (ids.equals("")) {
            return "";
        }
        return ids.substring(0, ids.length() - 1);
    }
}
