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
        patientMapper = mock(PatientMapper.class);
        drugsMapper = mock(DrugsMapper.class);
        seekMapper = mock(SeekMapper.class);

        service = new PatientServiceImpl();
        service.patientMapper = patientMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;
    }

    @Test
    public void seekUpdatesInventoryAndPrescriptionPriceForValidDispensing() {
        Patient patient = new Patient();
        patient.setId(9);
        patient.setDrugsids("1@2,2@1");

        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "3.50", 5));
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(drug(2, "2.00", 1));
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        ArgumentCaptor<Drugs> drugsCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper, org.mockito.Mockito.times(2)).updateNumber(drugsCaptor.capture());
        assertEquals(Integer.valueOf(2), drugsCaptor.getAllValues().get(0).getNumber());
        assertEquals(Integer.valueOf(1), drugsCaptor.getAllValues().get(1).getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        assertEquals(Integer.valueOf(9), seekCaptor.getValue().getPatientid());
        assertEquals("1@2,2@1", seekCaptor.getValue().getDrugs());
        assertEquals(0, new BigDecimal("9.00").compareTo(seekCaptor.getValue().getPrice()));
    }

    @Test
    public void seekDoesNotWriteAnythingWhenRequestedQuantityExceedsStock() {
        Patient patient = new Patient();
        patient.setId(9);
        patient.setDrugsids("1@3");

        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "3.50", 2));

        String message = service.seek(patient);

        assertEquals("对不起2数量不足", message);
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
    }

    private Drugs drug(Integer id, String price, Integer stock) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(stock);
        return drugs;
    }
}
