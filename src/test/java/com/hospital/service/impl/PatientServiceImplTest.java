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
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PatientServiceImplTest {

    private PatientServiceImpl service;
    private PatientMapper patientMapper;
    private DrugsMapper drugsMapper;
    private SeekMapper seekMapper;

    @Before
    public void setUp() {
        service = new PatientServiceImpl();
        patientMapper = mock(PatientMapper.class);
        drugsMapper = mock(DrugsMapper.class);
        seekMapper = mock(SeekMapper.class);
        service.patientMapper = patientMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;
    }

    @Test
    public void seekDispensesAllRequestedDrugsAndAddsTotalPriceToLatestSeek() {
        Patient patient = patientWithDrugs("1@2,2@3");
        Drugs first = drug(1, "10.00", 5);
        Drugs second = drug(2, "2.50", 4);
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(first);
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(second);
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        ArgumentCaptor<Drugs> drugsCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper, times(2)).updateNumber(drugsCaptor.capture());
        List<Drugs> updatedDrugs = drugsCaptor.getAllValues();
        assertSame(first, updatedDrugs.get(0));
        assertEquals(Integer.valueOf(2), updatedDrugs.get(0).getNumber());
        assertSame(second, updatedDrugs.get(1));
        assertEquals(Integer.valueOf(3), updatedDrugs.get(1).getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek seek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(99), seek.getPatientid());
        assertEquals("1@2,2@3", seek.getDrugs());
        assertEquals(0, new BigDecimal("27.50").compareTo(seek.getPrice()));
    }

    @Test
    public void seekRejectsOverstockedPrescriptionWithoutPartialWrites() {
        Patient patient = patientWithDrugs("1@2,2@10");
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "10.00", 5));
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(drug(2, "2.50", 3));

        String message = service.seek(patient);

        assertEquals("对不起3数量不足", message);
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
    }

    @Test
    public void seekRejectsNonPositiveQuantityWithoutWrites() {
        Patient patient = patientWithDrugs("1@0");
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "10.00", 5));

        String message = service.seek(patient);

        assertTrue(message.contains("数量不足"));
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
    }

    private Patient patientWithDrugs(String drugsids) {
        Patient patient = new Patient();
        patient.setId(99);
        patient.setDrugsids(drugsids);
        return patient;
    }

    private Drugs drug(Integer id, String price, Integer number) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(number);
        return drugs;
    }
}
