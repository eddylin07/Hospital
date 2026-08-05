package com.hospital.uitls;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DrugsUtilsTest {

    @Test
    public void vaildBuildsDrugIdAndQuantityPairsFromNumberFields() {
        Map<String, String> form = new LinkedHashMap<String, String>();
        form.put("csrf", "ignored");
        form.put("1_number", "2");
        form.put("1_name", "ignored");
        form.put("5_number", "3");
        form.put("8_number", "");

        assertEquals("1@2,5@3", DrugsUtils.vaild(form));
    }

    @Test
    public void vaild2BuildsCommaSeparatedOptionIds() {
        Map<String, String> form = new LinkedHashMap<String, String>();
        form.put("patient_id", "ignored");
        form.put("option_0", "12");
        form.put("option_1", "14");

        assertEquals("12,14", DrugsUtils.vaild2(form));
    }
}
