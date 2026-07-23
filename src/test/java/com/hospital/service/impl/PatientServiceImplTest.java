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
    public void seekCalculatesPrescriptionPriceAndUpdatesInventory() {
        Patient patient = new Patient();
        patient.setId(42);
        patient.setDrugsids("5@2,7@3");

        Drugs firstDrug = drug(5, "2.50", 10);
        Drugs secondDrug = drug(7, "3.75", 3);
        when(drugsMapper.selectByPrimaryKey(5)).thenReturn(firstDrug);
        when(drugsMapper.selectByPrimaryKey(7)).thenReturn(secondDrug);
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(Integer.valueOf(2), firstDrug.getNumber());
        assertEquals(Integer.valueOf(3), secondDrug.getNumber());
        verify(drugsMapper).updateNumber(firstDrug);
        verify(drugsMapper).updateNumber(secondDrug);

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek updatedSeek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(42), updatedSeek.getPatientid());
        assertEquals("5@2,7@3", updatedSeek.getDrugs());
        assertEquals(0, new BigDecimal("16.25").compareTo(updatedSeek.getPrice()));
    }

    @Test
    public void seekDoesNotUpdateRecordsWhenRequestedQuantityExceedsStock() {
        Patient patient = new Patient();
        patient.setId(42);
        patient.setDrugsids("5@3");

        when(drugsMapper.selectByPrimaryKey(5)).thenReturn(drug(5, "2.50", 2));

        String message = service.seek(patient);

        assertTrue(message.contains("数量不足"));
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
    }

    private Drugs drug(Integer id, String price, Integer number) {
        Drugs drug = new Drugs();
        drug.setId(id);
        drug.setPrice(new BigDecimal(price));
        drug.setNumber(number);
        return drug;
    }
}
