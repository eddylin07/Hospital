package com.hospital.uitls;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DrugsUtilsTest {

    @Test
    public void returnsEmptyStringWhenNoDrugQuantityProvided() {
        Map<String,String> map=new HashMap<>();
        map.put("patientid","7");

        assertEquals("",DrugsUtils.vaild(map));
    }

    @Test
    public void returnsEmptyStringWhenNoOptionProvided() {
        Map<String,String> map=new HashMap<>();
        map.put("patientid","7");

        assertEquals("",DrugsUtils.vaild2(map));
    }
}

