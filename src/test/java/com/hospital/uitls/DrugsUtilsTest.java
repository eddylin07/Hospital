package com.hospital.uitls;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DrugsUtilsTest {
    @Test
    public void vaildReturnsEmptyStringWhenNoDrugQuantitiesSelected() {
        assertEquals("", DrugsUtils.vaild(Collections.emptyMap()));
    }

    @Test
    public void vaild2ReturnsEmptyStringWhenNoOptionsSelected() {
        assertEquals("", DrugsUtils.vaild2(Collections.emptyMap()));
    }

    @Test
    public void vaildKeepsSelectedDrugQuantities() {
        Map<String, String> map = new HashMap<>();
        map.put("1_number", "2");
        map.put("2_number", "");

        assertEquals("1@2", DrugsUtils.vaild(map));
    }
}
