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
    public void seekDeductsInventoryAndAddsDrugPriceWhenStockIsAvailable() {
        Patient patient = patientWithDrugs("3@2");
        when(drugsMapper.selectByPrimaryKey(3)).thenReturn(drug(3, 5, "4.50"));
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = patientService.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        ArgumentCaptor<Drugs> drugCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper).updateNumber(drugCaptor.capture());
        assertEquals(Integer.valueOf(3), drugCaptor.getValue().getId());
        assertEquals(Integer.valueOf(2), drugCaptor.getValue().getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        assertEquals(Integer.valueOf(7), seekCaptor.getValue().getPatientid());
        assertEquals("3@2", seekCaptor.getValue().getDrugs());
        assertEquals(0, new BigDecimal("9.00").compareTo(seekCaptor.getValue().getPrice()));
    }

    @Test
    public void seekRejectsRequestedQuantityGreaterThanStockWithoutWrites() {
        Patient patient = patientWithDrugs("3@5");
        when(drugsMapper.selectByPrimaryKey(3)).thenReturn(drug(3, 2, "4.50"));

        String message = patientService.seek(patient);

        assertTrue(message.contains("数量不足"));
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
    }

    @Test
    public void seekDoesNotPartiallyDeductEarlierDrugsWhenLaterDrugFailsStockCheck() {
        Patient patient = patientWithDrugs("1@1,2@1");
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, 5, "1.00"));
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(drug(2, 0, "1.00"));

        String message = patientService.seek(patient);

        assertTrue(message.contains("数量不足"));
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
    }

    private Patient patientWithDrugs(String drugsids) {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids(drugsids);
        return patient;
    }

    private Drugs drug(Integer id, Integer number, String price) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setNumber(number);
        drugs.setPrice(new BigDecimal(price));
        return drugs;
    }
}
