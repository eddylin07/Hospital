package com.hospital.uitls;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DrugsUtilsTest {
    @Test
    public void validReturnsEmptyWhenNoDrugQuantityWasSubmitted() {
        assertEquals("", DrugsUtils.vaild(Collections.singletonMap("patientid", "1")));
    }

    @Test
    public void valid2ReturnsEmptyWhenNoOptionsWereSubmitted() {
        assertEquals("", DrugsUtils.vaild2(Collections.singletonMap("patientid", "1")));
    }

    @Test
    public void getOptionIdsHandlesEmptyInput() {
        assertTrue(PatientDoctorutils.getOptionIds("").isEmpty());
        assertTrue(PatientDoctorutils.getOptionIds(null).isEmpty());
    }
}
