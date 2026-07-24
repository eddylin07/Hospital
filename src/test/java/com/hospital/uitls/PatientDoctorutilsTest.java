package com.hospital.uitls;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PatientDoctorutilsTest {

    @Test
    public void emptyOptionIdsReturnEmptyList() {
        assertTrue(PatientDoctorutils.getOptionIds(null).isEmpty());
        assertTrue(PatientDoctorutils.getOptionIds("").isEmpty());
        assertTrue(PatientDoctorutils.getOptionIds(" ").isEmpty());
    }
}

