package com.hospital.uitls;

import java.util.Iterator;
import java.util.Map;

public class DrugsUtils {
    public static String vaild(Map map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        Iterator entries = map.entrySet().iterator();
        String ids = "";
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String) entry.getKey();
            Object rawValue = entry.getValue();
            if(rawValue==null){
                continue;
            }
            String value = String.valueOf(rawValue);
            String[] _key = key.split("_");
            if (_key.length > 1) {
                if (_key[1].equals("number") && !value.equals("")) {
                    ids += (_key[0] + "@" + value) + ",";
                }
            }
        }
        return ids.isEmpty() ? "" : ids.substring(0, ids.length() - 1);
    }

    public static String vaild2(Map map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        Iterator entries = map.entrySet().iterator();
        String ids = "";
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String) entry.getKey();
            Object rawValue = entry.getValue();
            if(rawValue==null){
                continue;
            }
            String value = String.valueOf(rawValue);
            if (key.split("_")[0].equals("option")) {
                ids += value + ",";
            }
        }
        return ids.isEmpty() ? "" : ids.substring(0, ids.length() - 1);
    }
}
