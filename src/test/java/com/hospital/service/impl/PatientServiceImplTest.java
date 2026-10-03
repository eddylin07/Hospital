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
import static org.junit.Assert.assertTrue;
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
    public void seekDispensesDrugsAndAddsTotalPriceToLatestSeek() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@3,2@2");
        Drugs firstDrug = drug(1, "4.00", 10);
        Drugs secondDrug = drug(2, "1.50", 5);
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(firstDrug);
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(secondDrug);
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        verify(drugsMapper).updateNumber(firstDrug);
        verify(drugsMapper).updateNumber(secondDrug);
        assertEquals(Integer.valueOf(3), firstDrug.getNumber());
        assertEquals(Integer.valueOf(2), secondDrug.getNumber());
        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek seek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(7), seek.getPatientid());
        assertEquals("1@3,2@2", seek.getDrugs());
        assertEquals(0, new BigDecimal("15.00").compareTo(seek.getPrice()));
    }

    @Test
    public void seekRejectsOverStockRequestWithoutWritingAnything() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@5");
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "4.00", 2));

        String message = service.seek(patient);

        assertTrue(message.contains("数量不足"));
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
