package com.hospital.uitls;

import com.hospital.dao.PatientMapper;
import com.hospital.entity.Patient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PatientDoctorutilsTest {

    @Mock
    private PatientMapper patientMapper;

    @Test
    public void getPatientIdsPrependsSentinelBeforeMatchedPatientIds() {
        Patient patient = new Patient();
        patient.setId(21);
        when(patientMapper.getPatientByName("Alice")).thenReturn(Arrays.asList(patient));

        List<Integer> patientIds = PatientDoctorutils.getPatientIds(" Alice ", patientMapper);

        assertEquals(Arrays.asList(99999999, 21), patientIds);
    }

    @Test
    public void getPatientIdsReturnsNullWhenNameIsNull() {
        assertNull(PatientDoctorutils.getPatientIds(null, patientMapper));
    }

    @Test
    public void getOptionIdsParsesSingleAndMultipleIds() {
        assertEquals(Arrays.asList(42), PatientDoctorutils.getOptionIds("42"));
        assertEquals(Arrays.asList(1, 2, 3), PatientDoctorutils.getOptionIds("1,2,3"));
    }
}
