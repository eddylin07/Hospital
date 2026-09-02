package com.hospital.uitls;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DrugsUtilsTest {

    @Test
    public void emptyDrugSelectionReturnsEmptyString() {
        Map<String, String> form = new HashMap<>();
        form.put("patientid", "1");

        Assert.assertEquals("", DrugsUtils.vaild(form));
    }

    @Test
    public void emptyOptionSelectionReturnsEmptyString() {
        Map<String, String> form = new HashMap<>();
        form.put("patientid", "1");

        Assert.assertEquals("", DrugsUtils.vaild2(form));
    }
}
