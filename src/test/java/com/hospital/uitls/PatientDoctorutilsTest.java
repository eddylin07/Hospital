package com.hospital.uitls;

import com.hospital.dao.DoctorMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PatientDoctorutilsTest {

    @Test
    public void getDoctorIdsAndPatientIdsReturnsNullFiltersWhenNamesAreOmitted() {
        DoctorMapper doctorMapper = mock(DoctorMapper.class);
        PatientMapper patientMapper = mock(PatientMapper.class);

        Map<String, List<Integer>> filters = PatientDoctorutils.getDoctorIdsAndPatientIds(
                null, doctorMapper, null, patientMapper);

        assertNull(filters.get("doctorids"));
        assertNull(filters.get("patientids"));
        verify(doctorMapper, never()).getDoctorByName(null);
        verify(patientMapper, never()).getPatientByName(null);
    }

    @Test
    public void getDoctorIdsAndPatientIdsAddsSentinelAndMatchedIdsForNameFilters() {
        DoctorMapper doctorMapper = mock(DoctorMapper.class);
        PatientMapper patientMapper = mock(PatientMapper.class);
        when(doctorMapper.getDoctorByName("Li")).thenReturn(Arrays.asList(doctor(3), doctor(4)));
        when(patientMapper.getPatientByName("Wang")).thenReturn(Arrays.asList(patient(9)));

        Map<String, List<Integer>> filters = PatientDoctorutils.getDoctorIdsAndPatientIds(
                " Li ", doctorMapper, " Wang ", patientMapper);

        assertEquals(Arrays.asList(99999999, 3, 4), filters.get("doctorids"));
        assertEquals(Arrays.asList(99999999, 9), filters.get("patientids"));
        verify(doctorMapper).getDoctorByName("Li");
        verify(patientMapper).getPatientByName("Wang");
    }

    @Test
    public void getPatientIdsReturnsSentinelOnlyWhenNameHasNoMatches() {
        PatientMapper patientMapper = mock(PatientMapper.class);
        when(patientMapper.getPatientByName("Missing")).thenReturn(Arrays.<Patient>asList());

        List<Integer> patientIds = PatientDoctorutils.getPatientIds("Missing", patientMapper);

        assertEquals(Arrays.asList(99999999), patientIds);
    }

    @Test
    public void getOptionIdsParsesCommaSeparatedIds() {
        assertEquals(Arrays.asList(1, 2, 3), PatientDoctorutils.getOptionIds("1,2,3"));
    }

    private Doctor doctor(Integer id) {
        Doctor doctor = new Doctor();
        doctor.setId(id);
        return doctor;
    }

    private Patient patient(Integer id) {
        Patient patient = new Patient();
        patient.setId(id);
        return patient;
    }
}
