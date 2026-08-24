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
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

    private PatientServiceImpl patientService;

    @Before
    public void setUp() {
        patientService = new PatientServiceImpl();
        patientService.patientMapper = patientMapper;
        patientService.drugsMapper = drugsMapper;
        patientService.seekMapper = seekMapper;
    }

    @Test
    public void seekDeductsRequestedInventoryAndUpdatesTotalPrice() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@2,2@3");
        Drugs first = drug(1, "10.50", 5);
        Drugs second = drug(2, "2.00", 3);
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(first);
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(second);
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = patientService.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        verify(drugsMapper).updateNumber(first);
        verify(drugsMapper).updateNumber(second);
        assertEquals(Integer.valueOf(2), first.getNumber());
        assertEquals(Integer.valueOf(3), second.getNumber());
        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek seek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(7), seek.getPatientid());
        assertEquals("1@2,2@3", seek.getDrugs());
        assertEquals(0, new BigDecimal("27.00").compareTo(seek.getPrice()));
    }

    @Test
    public void seekRejectsInsufficientStockBeforeAnyWrite() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@2,2@4");
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "10.00", 5));
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(drug(2, "2.00", 3));

        String message = patientService.seek(patient);

        assertTrue(message.contains("数量不足"));
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
