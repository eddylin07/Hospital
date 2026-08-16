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
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
    private PatientServiceImpl service;

    @Test
    public void seekDispensesAllDrugsAndAddsTotalPriceToLatestSeek() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@2,2@3");
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "5.00", 10));
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(drug(2, "1.50", 3));
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        ArgumentCaptor<Drugs> drugsCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper, times(2)).updateNumber(drugsCaptor.capture());
        List<Drugs> updatedDrugs = drugsCaptor.getAllValues();
        assertEquals(Integer.valueOf(1), updatedDrugs.get(0).getId());
        assertEquals(Integer.valueOf(2), updatedDrugs.get(0).getNumber());
        assertEquals(Integer.valueOf(2), updatedDrugs.get(1).getId());
        assertEquals(Integer.valueOf(3), updatedDrugs.get(1).getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek seek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(7), seek.getPatientid());
        assertEquals("1@2,2@3", seek.getDrugs());
        assertEquals(0, new BigDecimal("14.50").compareTo(seek.getPrice()));
    }

    @Test
    public void seekRejectsInsufficientStockWithoutPartialWrites() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@2,2@3");
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "5.00", 10));
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(drug(2, "1.50", 2));

        String message = service.seek(patient);

        assertNotEquals(CommonService.upd_message_success, message);
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
        verifyNoMoreInteractions(patientMapper, seekMapper);
    }

    private Drugs drug(Integer id, String price, Integer stock) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(stock);
        return drugs;
    }
}
