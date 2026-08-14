package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    public void seekDispensesRequestedQuantityAndAddsPriceToLatestSeek() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("3@2");

        Drugs drugs = new Drugs();
        drugs.setId(3);
        drugs.setNumber(5);
        drugs.setPrice(new BigDecimal("12.50"));

        when(drugsMapper.selectByPrimaryKey(3)).thenReturn(drugs);
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = patientService.seek(patient);

        assertEquals(CommonService.upd_message_success, message);

        ArgumentCaptor<Drugs> drugsCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper).updateNumber(drugsCaptor.capture());
        assertEquals(Integer.valueOf(3), drugsCaptor.getValue().getId());
        assertEquals(Integer.valueOf(2), drugsCaptor.getValue().getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        assertEquals(Integer.valueOf(7), seekCaptor.getValue().getPatientid());
        assertEquals("3@2", seekCaptor.getValue().getDrugs());
        assertEquals(new BigDecimal("25.00"), seekCaptor.getValue().getPrice());
    }

    @Test
    public void seekRejectsQuantityGreaterThanStockWithoutWriting() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("3@2");

        Drugs drugs = new Drugs();
        drugs.setId(3);
        drugs.setNumber(1);
        drugs.setPrice(new BigDecimal("12.50"));

        when(drugsMapper.selectByPrimaryKey(3)).thenReturn(drugs);

        String message = patientService.seek(patient);

        assertEquals("对不起1数量不足", message);
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
    }

    @Test
    public void seekRejectsNonPositiveQuantityWithoutWriting() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("3@0");

        Drugs drugs = new Drugs();
        drugs.setId(3);
        drugs.setNumber(5);
        drugs.setPrice(new BigDecimal("12.50"));

        when(drugsMapper.selectByPrimaryKey(3)).thenReturn(drugs);

        String message = patientService.seek(patient);

        assertEquals("对不起5数量不足", message);
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
    }
}
