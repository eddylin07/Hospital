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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PatientServiceImplSeekTest {

    @InjectMocks
    private PatientServiceImpl patientService;

    @Mock
    private PatientMapper patientMapper;

    @Mock
    private DrugsMapper drugsMapper;

    @Mock
    private SeekMapper seekMapper;

    @Test
    public void seekDeductsRequestedDrugQuantitiesAndAddsTotalPriceToSeek() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("10@3,12@2");

        when(drugsMapper.selectByPrimaryKey(10)).thenReturn(drug(10, "2.50", 20));
        when(drugsMapper.selectByPrimaryKey(12)).thenReturn(drug(12, "4.00", 5));
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = patientService.seek(patient);

        assertEquals(CommonService.upd_message_success, message);

        ArgumentCaptor<Drugs> drugsCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper, times(2)).updateNumber(drugsCaptor.capture());
        List<Drugs> updatedDrugs = drugsCaptor.getAllValues();
        assertEquals(Integer.valueOf(10), updatedDrugs.get(0).getId());
        assertEquals(Integer.valueOf(3), updatedDrugs.get(0).getNumber());
        assertEquals(Integer.valueOf(12), updatedDrugs.get(1).getId());
        assertEquals(Integer.valueOf(2), updatedDrugs.get(1).getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek updatedSeek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(7), updatedSeek.getPatientid());
        assertEquals("10@3,12@2", updatedSeek.getDrugs());
        assertEquals(new BigDecimal("15.50"), updatedSeek.getPrice());
        verify(patientMapper).updateByPrimaryKeySelective(patient);
    }

    @Test
    public void seekDoesNotDeductInventoryWhenDrugStockIsZero() {
        Patient patient = new Patient();
        patient.setId(9);
        patient.setDrugsids("20@4");

        when(drugsMapper.selectByPrimaryKey(20)).thenReturn(drug(20, "6.00", 0));
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        patientService.seek(patient);

        verify(drugsMapper, never()).updateNumber(any(Drugs.class));

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        assertEquals(new BigDecimal("0.0"), seekCaptor.getValue().getPrice());
    }

    @Test
    public void seekReturnsUpdateFailureWhenSeekDrugUpdateFails() {
        Patient patient = new Patient();
        patient.setId(11);
        patient.setDrugsids("30@1");

        when(drugsMapper.selectByPrimaryKey(30)).thenReturn(drug(30, "8.25", 2));
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(0);

        String message = patientService.seek(patient);

        assertEquals(CommonService.upd_message_error, message);
    }

    private Drugs drug(Integer id, String price, Integer number) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(number);
        return drugs;
    }
}
