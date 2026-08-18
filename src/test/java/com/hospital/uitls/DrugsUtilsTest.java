package com.hospital.uitls;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DrugsUtilsTest {
    @Test
    public void emptyDrugSelectionReturnsEmptyStringInsteadOfThrowing() {
        Map<String, String> map = new HashMap<>();
        map.put("patientid", "1");

        assertEquals("", DrugsUtils.vaild(map));
    }

    @Test
    public void emptyOptionSelectionReturnsEmptyStringInsteadOfThrowing() {
        Map<String, String> map = new HashMap<>();
        map.put("days", "3");

        assertEquals("", DrugsUtils.vaild2(map));
    }
}
