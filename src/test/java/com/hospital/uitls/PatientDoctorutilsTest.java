package com.hospital.uitls;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class PatientDoctorutilsTest {
    @Test
    public void getOptionIdsIgnoresBlankTokens() {
        Assert.assertEquals(Collections.emptyList(), PatientDoctorutils.getOptionIds(null));
        Assert.assertEquals(Collections.emptyList(), PatientDoctorutils.getOptionIds(""));
        Assert.assertEquals(Arrays.asList(1, 2), PatientDoctorutils.getOptionIds("1,2,"));
    }

    @Test(expected = NumberFormatException.class)
    public void getOptionIdsRejectsMalformedTokens() {
        PatientDoctorutils.getOptionIds("1,abc");
    }
}
