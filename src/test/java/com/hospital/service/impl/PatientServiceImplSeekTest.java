package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class PatientServiceImplSeekTest {

    private final PatientServiceImpl service = new PatientServiceImpl();
    private final PatientMapper patientMapper = mock(PatientMapper.class);
    private final DrugsMapper drugsMapper = mock(DrugsMapper.class);
    private final SeekMapper seekMapper = mock(SeekMapper.class);

    @Test
    public void seekUpdatesInventoryAndSeekPriceForSelectedDrugs() {
        service.patientMapper = patientMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;

        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@2,2@3");

        Drugs firstDrug = drug(new BigDecimal("10.50"), 20);
        Drugs secondDrug = drug(new BigDecimal("3.25"), 15);
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(firstDrug);
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(secondDrug);
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(org.mockito.Matchers.any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(Integer.valueOf(2), firstDrug.getNumber());
        assertEquals(Integer.valueOf(3), secondDrug.getNumber());
        verify(drugsMapper).updateNumber(firstDrug);
        verify(drugsMapper).updateNumber(secondDrug);

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek seek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(7), seek.getPatientid());
        assertEquals("1@2,2@3", seek.getDrugs());
        assertEquals(0, new BigDecimal("30.75").compareTo(seek.getPrice()));
    }

    @Test
    public void seekDoesNotDecrementInventoryWhenDrugHasNoStock() {
        service.patientMapper = patientMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;

        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@2");

        Drugs emptyDrug = drug(new BigDecimal("10.50"), 0);
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(emptyDrug);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_error, message);
        assertEquals(Integer.valueOf(0), emptyDrug.getNumber());
        verify(drugsMapper, never()).updateNumber(emptyDrug);
        verify(patientMapper).updateByPrimaryKeySelective(patient);
        verifyNoMoreInteractions(seekMapper);
    }

    private Drugs drug(BigDecimal price, Integer number) {
        Drugs drugs = new Drugs();
        drugs.setPrice(price);
        drugs.setNumber(number);
        return drugs;
    }
}

