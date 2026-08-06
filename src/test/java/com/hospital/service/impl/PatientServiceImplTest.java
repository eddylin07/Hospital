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
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PatientServiceImplTest {
    @Mock
    private PatientMapper patientMapper;
    @Mock
    private LoginMapper loginMapper;
    @Mock
    private DoctorMapper doctorMapper;
    @Mock
    private IllnessMapper illnessMapper;
    @Mock
    private DrugsMapper drugsMapper;
    @Mock
    private SeekMapper seekMapper;

    private PatientServiceImpl service;

    @Before
    public void setUp() {
        service = new PatientServiceImpl();
        service.patientMapper = patientMapper;
        service.loginMapper = loginMapper;
        service.doctorMapper = doctorMapper;
        service.illnessMapper = illnessMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;
    }

    @Test
    public void seekUpdatesInventoryAndLatestSeekForValidPrescription() {
        Patient patient = new Patient();
        patient.setId(42);
        patient.setDrugsids("7@3,8@2");
        when(drugsMapper.selectByPrimaryKey(7)).thenReturn(drug(7, "A", "10.50", 5));
        when(drugsMapper.selectByPrimaryKey(8)).thenReturn(drug(8, "B", "2.00", 10));
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        ArgumentCaptor<Drugs> drugsCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper, times(2)).updateNumber(drugsCaptor.capture());
        List<Drugs> updatedDrugs = drugsCaptor.getAllValues();
        assertEquals(Integer.valueOf(7), updatedDrugs.get(0).getId());
        assertEquals(Integer.valueOf(3), updatedDrugs.get(0).getNumber());
        assertEquals(Integer.valueOf(8), updatedDrugs.get(1).getId());
        assertEquals(Integer.valueOf(2), updatedDrugs.get(1).getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek seek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(42), seek.getPatientid());
        assertEquals("7@3,8@2", seek.getDrugs());
        assertEquals(0, new BigDecimal("35.50").compareTo(seek.getPrice()));
    }

    @Test
    public void seekRejectsPrescriptionWhenRequestedQuantityExceedsStock() {
        Patient patient = new Patient();
        patient.setId(42);
        patient.setDrugsids("7@6");
        when(drugsMapper.selectByPrimaryKey(7)).thenReturn(drug(7, "A", "10.50", 5));

        String message = service.seek(patient);

        assertTrue(message.contains("数量不足"));
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
    }

    private Drugs drug(Integer id, String name, String price, Integer number) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setName(name);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(number);
        return drugs;
    }
}
