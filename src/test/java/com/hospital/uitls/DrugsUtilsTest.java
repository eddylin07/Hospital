package com.hospital.uitls;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DrugsUtilsTest {

    @Test
    public void vaildEncodesOnlyNonEmptyDrugQuantities() {
        Map<String, String> form = new LinkedHashMap<String, String>();
        form.put("11_number", "2");
        form.put("11_name", "ignored");
        form.put("12_number", "");
        form.put("13_number", "5");

        String encoded = DrugsUtils.vaild(form);

        assertEquals("11@2,13@5", encoded);
    }

    @Test
    public void vaild2EncodesOnlyOptionValues() {
        Map<String, String> form = new LinkedHashMap<String, String>();
        form.put("option_1", "21");
        form.put("patient", "ignored");
        form.put("option_2", "34");

        String encoded = DrugsUtils.vaild2(form);

        assertEquals("21,34", encoded);
    }
}
