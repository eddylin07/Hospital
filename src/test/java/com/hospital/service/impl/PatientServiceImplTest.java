package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DoctorMapper;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.IllnessMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PatientServiceImplTest {

    private final PatientMapper patientMapper = mock(PatientMapper.class);
    private final LoginMapper loginMapper = mock(LoginMapper.class);
    private final DoctorMapper doctorMapper = mock(DoctorMapper.class);
    private final IllnessMapper illnessMapper = mock(IllnessMapper.class);
    private final DrugsMapper drugsMapper = mock(DrugsMapper.class);
    private final SeekMapper seekMapper = mock(SeekMapper.class);

    private PatientServiceImpl service;

    @Before
    public void setUp() {
        service = new PatientServiceImpl();
        ReflectionTestUtils.setField(service, "patientMapper", patientMapper);
        ReflectionTestUtils.setField(service, "loginMapper", loginMapper);
        ReflectionTestUtils.setField(service, "doctorMapper", doctorMapper);
        ReflectionTestUtils.setField(service, "illnessMapper", illnessMapper);
        ReflectionTestUtils.setField(service, "drugsMapper", drugsMapper);
        ReflectionTestUtils.setField(service, "seekMapper", seekMapper);
    }

    @Test
    public void seekWritesRequestedDrugsAndTotalPriceToLatestPrescription() {
        Patient patient = new Patient();
        patient.setId(42);
        patient.setDrugsids("7@2");

        Drugs aspirin = new Drugs();
        aspirin.setId(7);
        aspirin.setNumber(10);
        aspirin.setPrice(new BigDecimal("5.50"));

        when(drugsMapper.selectByPrimaryKey(7)).thenReturn(aspirin);
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);

        ArgumentCaptor<Drugs> drugCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper).updateNumber(drugCaptor.capture());
        assertEquals(Integer.valueOf(2), drugCaptor.getValue().getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        assertEquals(Integer.valueOf(42), seekCaptor.getValue().getPatientid());
        assertEquals("7@2", seekCaptor.getValue().getDrugs());
        assertEquals(new BigDecimal("11.00"), seekCaptor.getValue().getPrice());
    }
}
