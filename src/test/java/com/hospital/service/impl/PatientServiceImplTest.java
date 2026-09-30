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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class PatientServiceImplTest {
    private PatientServiceImpl service;

    @Mock
    private PatientMapper patientMapper;
    @Mock
    private DrugsMapper drugsMapper;
    @Mock
    private SeekMapper seekMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new PatientServiceImpl();
        service.patientMapper = patientMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;
    }

    @Test
    public void seekDispensesRequestedDrugsAndUpdatesLatestVisitPrice() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@2,2@3");
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "2.50", 10));
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(drug(2, "1.00", 3));
        when(drugsMapper.updateNumber(any(Drugs.class))).thenReturn(1);
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);

        ArgumentCaptor<Drugs> drugsCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper, times(2)).updateNumber(drugsCaptor.capture());
        List<Drugs> dispensed = drugsCaptor.getAllValues();
        assertEquals(Integer.valueOf(1), dispensed.get(0).getId());
        assertEquals(Integer.valueOf(2), dispensed.get(0).getNumber());
        assertEquals(Integer.valueOf(2), dispensed.get(1).getId());
        assertEquals(Integer.valueOf(3), dispensed.get(1).getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek updatedSeek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(7), updatedSeek.getPatientid());
        assertEquals("1@2,2@3", updatedSeek.getDrugs());
        assertTrue(new BigDecimal("8.00").compareTo(updatedSeek.getPrice()) == 0);
        verify(patientMapper).updateByPrimaryKeySelective(patient);
    }

    @Test
    public void seekRejectsRequestedQuantityGreaterThanStockWithoutWrites() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@2");
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "2.50", 1));

        String message = service.seek(patient);

        assertEquals("对不起1数量不足", message);
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
    }

    @Test
    public void seekDoesNotPartiallyDispenseWhenLaterDrugIsShort() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@2,2@5");
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "2.50", 10));
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(drug(2, "1.00", 3));

        String message = service.seek(patient);

        assertEquals("对不起3数量不足", message);
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
    }

    @Test
    public void seekRejectsNonPositiveQuantityWithoutWrites() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@0");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_error, message);
        verifyNoMoreInteractions(drugsMapper);
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
