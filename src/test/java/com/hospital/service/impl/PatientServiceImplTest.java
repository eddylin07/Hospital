package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DrugsMapper;
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
    private DrugsMapper drugsMapper;
    @Mock
    private SeekMapper seekMapper;

    private PatientServiceImpl service;

    @Before
    public void setUp() {
        service = new PatientServiceImpl();
        service.patientMapper = patientMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;
    }

    @Test
    public void seekDispensesRequestedDrugsAndRecordsTotalPrice() {
        Patient patient = patient(42, "5@2,6@1");
        when(drugsMapper.selectByPrimaryKey(5)).thenReturn(drug(5, "3.50", 10));
        when(drugsMapper.selectByPrimaryKey(6)).thenReturn(drug(6, "2.00", 4));
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String result = service.seek(patient);

        assertEquals(CommonService.upd_message_success, result);

        ArgumentCaptor<Drugs> drugsCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper, times(2)).updateNumber(drugsCaptor.capture());
        List<Drugs> dispensedDrugs = drugsCaptor.getAllValues();
        assertEquals(Integer.valueOf(5), dispensedDrugs.get(0).getId());
        assertEquals(Integer.valueOf(2), dispensedDrugs.get(0).getNumber());
        assertEquals(Integer.valueOf(6), dispensedDrugs.get(1).getId());
        assertEquals(Integer.valueOf(1), dispensedDrugs.get(1).getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek recordedSeek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(42), recordedSeek.getPatientid());
        assertEquals("5@2,6@1", recordedSeek.getDrugs());
        assertEquals(0, new BigDecimal("9.00").compareTo(recordedSeek.getPrice()));
    }

    @Test
    public void seekRejectsQuantityGreaterThanStockWithoutWritingAnything() {
        Patient patient = patient(42, "5@3");
        when(drugsMapper.selectByPrimaryKey(5)).thenReturn(drug(5, "3.50", 2));

        String result = service.seek(patient);

        assertEquals("对不起2数量不足", result);
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
    }

    private static Patient patient(Integer id, String drugsids) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setDrugsids(drugsids);
        return patient;
    }

    private static Drugs drug(Integer id, String price, Integer number) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(number);
        return drugs;
    }
}
