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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
    public void seekUpdatesInventoryAndLatestPrescriptionPrice() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@2,2@3");
        Drugs firstDrug = drug(1, "4.50", 10);
        Drugs secondDrug = drug(2, "2.00", 5);
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(firstDrug);
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(secondDrug);
        when(drugsMapper.updateNumber(any(Drugs.class))).thenReturn(1);
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        verify(drugsMapper).updateNumber(firstDrug);
        verify(drugsMapper).updateNumber(secondDrug);
        assertEquals(Integer.valueOf(2), firstDrug.getNumber());
        assertEquals(Integer.valueOf(3), secondDrug.getNumber());
        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        assertEquals(Integer.valueOf(7), seekCaptor.getValue().getPatientid());
        assertEquals("1@2,2@3", seekCaptor.getValue().getDrugs());
        assertTrue(new BigDecimal("15.00").compareTo(seekCaptor.getValue().getPrice()) == 0);
    }

    @Test
    public void seekRejectsInsufficientStockBeforeAnyWrite() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@2");
        Drugs drug = drug(1, "4.50", 1);
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug);

        String message = service.seek(patient);

        assertEquals("对不起1数量不足", message);
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
    }

    private static Drugs drug(Integer id, String price, Integer number) {
        Drugs drug = new Drugs();
        drug.setId(id);
        drug.setPrice(new BigDecimal(price));
        drug.setNumber(number);
        return drug;
    }
}
