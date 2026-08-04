package com.hospital.uitls;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DrugsUtilsTest {
    @Test
    public void vaildReturnsEmptyStringWhenNoDrugQuantitySelected() {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("5_number", "");
        form.put("patientid", "12");

        assertEquals("", DrugsUtils.vaild(form));
    }

    @Test
    public void vaildBuildsDrugQuantityPairsInFormOrder() {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("3_number", "2");
        form.put("5_number", "1");

        assertEquals("3@2,5@1", DrugsUtils.vaild(form));
    }

    @Test
    public void vaild2ReturnsEmptyStringWhenNoOptionsSelected() {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("days", "2");

        assertEquals("", DrugsUtils.vaild2(form));
    }

    @Test
    public void vaild2BuildsCommaSeparatedOptionIds() {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("option_1", "4");
        form.put("option_2", "7");

        assertEquals("4,7", DrugsUtils.vaild2(form));
    }
}
