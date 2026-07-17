package com.hospital.uitls;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DrugsUtilsTest {

    @Test
    public void vaildBuildsDrugIdAndQuantityPairsFromNumberFields() {
        Map<String, String> formValues = new LinkedHashMap<String, String>();
        formValues.put("1_number", "2");
        formValues.put("1_name", "ignored");
        formValues.put("2_number", "");
        formValues.put("3_number", "4");

        String result = DrugsUtils.vaild(formValues);

        assertEquals("1@2,3@4", result);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void vaildThrowsWhenNoDrugQuantityIsSubmitted() {
        Map<String, String> formValues = new LinkedHashMap<String, String>();
        formValues.put("1_name", "ignored");
        formValues.put("2_number", "");

        DrugsUtils.vaild(formValues);
    }

    @Test
    public void vaild2BuildsCommaSeparatedOptionIds() {
        Map<String, String> formValues = new LinkedHashMap<String, String>();
        formValues.put("option_1", "12");
        formValues.put("doctor_1", "ignored");
        formValues.put("option_2", "18");

        String result = DrugsUtils.vaild2(formValues);

        assertEquals("12,18", result);
    }
}

