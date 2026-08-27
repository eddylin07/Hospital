package com.hospital.uitls;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class DrugsUtilsTest {

    @Test
    public void validReturnsEmptyStringWhenNoDrugQuantitiesSelected() {
        assertEquals("", DrugsUtils.vaild(Collections.singletonMap("patientid", "7")));
    }

    @Test
    public void valid2ReturnsEmptyStringWhenNoOptionsSelected() {
        assertEquals("", DrugsUtils.vaild2(Collections.singletonMap("patientid", "7")));
    }
}
