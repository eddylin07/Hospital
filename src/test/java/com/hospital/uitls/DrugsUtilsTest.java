package com.hospital.uitls;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DrugsUtilsTest {

    @Test
    public void vaildBuildsDrugQuantityPairsFromNumberFields() {
        Map<String, String> requestParams = new LinkedHashMap<>();
        requestParams.put("10_number", "3");
        requestParams.put("10_name", "Aspirin");
        requestParams.put("12_number", "2");

        assertEquals("10@3,12@2", DrugsUtils.vaild(requestParams));
    }

    @Test
    public void vaildSkipsBlankDrugQuantities() {
        Map<String, String> requestParams = new LinkedHashMap<>();
        requestParams.put("10_number", "");
        requestParams.put("12_number", "2");

        assertEquals("12@2", DrugsUtils.vaild(requestParams));
    }

    @Test
    public void vaild2BuildsCommaSeparatedOptionIdsFromOptionFields() {
        Map<String, String> requestParams = new LinkedHashMap<>();
        requestParams.put("option_1", "5");
        requestParams.put("ignored", "99");
        requestParams.put("option_2", "8");

        assertEquals("5,8", DrugsUtils.vaild2(requestParams));
    }
}
