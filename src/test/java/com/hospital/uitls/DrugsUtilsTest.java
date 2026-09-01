package com.hospital.uitls;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DrugsUtilsTest {
    @Test
    public void emptyDrugPayloadReturnsEmptyString() {
        Map<String, String> map = new HashMap<>();
        map.put("patientid", "1");

        assertEquals("", DrugsUtils.vaild(map));
    }

    @Test
    public void emptyOptionPayloadReturnsEmptyString() {
        Map<String, String> map = new HashMap<>();
        map.put("patientid", "1");
        map.put("days", "1");

        assertEquals("", DrugsUtils.vaild2(map));
    }
}
