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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
    public void seekUpdatesInventoryAndLatestSeekWhenAllDrugsAreAvailable() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@2,2@3");
        Drugs first = drug(1, "4.50", 10);
        Drugs second = drug(2, "1.25", 3);
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(first);
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(second);
        when(drugsMapper.updateNumber(any(Drugs.class))).thenReturn(1);
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(Integer.valueOf(2), first.getNumber());
        assertEquals(Integer.valueOf(3), second.getNumber());
        verify(drugsMapper).updateNumber(first);
        verify(drugsMapper).updateNumber(second);
        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        assertEquals(Integer.valueOf(7), seekCaptor.getValue().getPatientid());
        assertEquals("1@2,2@3", seekCaptor.getValue().getDrugs());
        assertEquals(new BigDecimal("12.75"), seekCaptor.getValue().getPrice());
    }

    @Test
    public void seekDoesNotPartiallyWriteWhenAnyRequestedDrugExceedsStock() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@2,2@4");
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "4.50", 10));
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(drug(2, "1.25", 3));

        String message = service.seek(patient);

        assertEquals("对不起3数量不足", message);
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
    }

    private Drugs drug(Integer id, String price, Integer number) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(number);
        return drugs;
    }
}
