package com.hospital.uitls;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DrugsUtilsTest {
    @Test
    public void emptyDrugSelectionReturnsEmptyString() {
        Map<String, String> map = new HashMap<>();
        map.put("1_number", "");

        assertEquals("", DrugsUtils.vaild(map));
    }

    @Test
    public void emptyOptionSelectionReturnsEmptyStringAndParsesToEmptyIds() {
        Map<String, String> map = new HashMap<>();
        map.put("option_1", "");

        assertEquals("", DrugsUtils.vaild2(map));
        assertTrue(PatientDoctorutils.getOptionIds("").isEmpty());
    }
}
