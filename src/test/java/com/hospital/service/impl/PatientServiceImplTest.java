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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class PatientServiceImplTest {
    private PatientServiceImpl service;
    private DrugsMapper drugsMapper;
    private PatientMapper patientMapper;
    private SeekMapper seekMapper;

    @Before
    public void setUp() {
        service = new PatientServiceImpl();
        drugsMapper = mock(DrugsMapper.class);
        patientMapper = mock(PatientMapper.class);
        seekMapper = mock(SeekMapper.class);
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
    }

    @Test
    public void seekDeductsRequestedStockAndAddsPrescriptionPrice() {
        Patient patient = patient(42, "7@2");
        Drugs aspirin = drugs(7, "3.50", 5);
        when(drugsMapper.selectByPrimaryKey(7)).thenReturn(aspirin);
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        ArgumentCaptor<Drugs> drugsCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper).updateNumber(drugsCaptor.capture());
        assertEquals(Integer.valueOf(7), drugsCaptor.getValue().getId());
        assertEquals(Integer.valueOf(2), drugsCaptor.getValue().getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        assertEquals(Integer.valueOf(42), seekCaptor.getValue().getPatientid());
        assertEquals("7@2", seekCaptor.getValue().getDrugs());
        assertEquals(0, new BigDecimal("7.00").compareTo(seekCaptor.getValue().getPrice()));
    }

    @Test
    public void seekRejectsInsufficientStockBeforeAnyWrites() {
        Patient patient = patient(42, "7@2");
        when(drugsMapper.selectByPrimaryKey(7)).thenReturn(drugs(7, "3.50", 1));

        String message = service.seek(patient);

        assertTrue(message.contains("数量不足"));
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verifyNoMoreInteractions(patientMapper, seekMapper);
    }

    @Test
    public void seekRejectsNonPositiveQuantityBeforeAnyWrites() {
        Patient patient = patient(42, "7@0");
        when(drugsMapper.selectByPrimaryKey(7)).thenReturn(drugs(7, "3.50", 5));

        String message = service.seek(patient);

        assertTrue(message.contains("数量不足"));
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verifyNoMoreInteractions(patientMapper, seekMapper);
    }

    private Patient patient(Integer id, String drugsids) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setDrugsids(drugsids);
        return patient;
    }

    private Drugs drugs(Integer id, String price, Integer number) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(number);
        return drugs;
    }
}

