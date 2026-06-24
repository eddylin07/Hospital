package com.hospital.uitls;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DrugsUtilsTest {

    @Test
    public void vaildBuildsDrugQuantityPairsFromNumberFields() {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("ignored", "value");
        form.put("5_number", "2");
        form.put("6_number", "");

        assertEquals("5@2", DrugsUtils.vaild(form));
    }

    @Test
    public void vaild2BuildsCommaSeparatedOptionIds() {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("patientid", "12");
        form.put("option_1", "8");
        form.put("option_2", "13");

        assertEquals("8,13", DrugsUtils.vaild2(form));
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void vaildThrowsWhenNoDrugQuantitiesAreSubmitted() {
        DrugsUtils.vaild(new LinkedHashMap<String, String>());
    }
}
