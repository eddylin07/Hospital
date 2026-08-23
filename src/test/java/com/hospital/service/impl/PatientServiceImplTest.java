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

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    public void seekDispensesInventoryAndUpdatesLatestVisitPrice() {
        Patient patient = patient(42, "7@2");
        Drugs aspirin = drug(7, "12.50", 5);
        when(drugsMapper.selectByPrimaryKey(7)).thenReturn(aspirin);
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String result = patientService.seek(patient);

        assertEquals(CommonService.upd_message_success, result);
        ArgumentCaptor<Drugs> drugsCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper).updateNumber(drugsCaptor.capture());
        assertEquals(Integer.valueOf(7), drugsCaptor.getValue().getId());
        assertEquals(Integer.valueOf(2), drugsCaptor.getValue().getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        assertEquals(Integer.valueOf(42), seekCaptor.getValue().getPatientid());
        assertEquals("7@2", seekCaptor.getValue().getDrugs());
        assertTrue(new BigDecimal("25.00").compareTo(seekCaptor.getValue().getPrice()) == 0);
    }

    @Test
    public void seekDoesNotPartiallyDispenseWhenLaterDrugHasInsufficientStock() {
        Patient patient = patient(42, "7@2,8@5");
        when(drugsMapper.selectByPrimaryKey(7)).thenReturn(drug(7, "12.50", 10));
        when(drugsMapper.selectByPrimaryKey(8)).thenReturn(drug(8, "3.00", 1));

        String result = patientService.seek(patient);

        assertEquals("对不起1数量不足", result);
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
    }

    private Patient patient(Integer id, String drugsids) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setDrugsids(drugsids);
        return patient;
    }

    private Drugs drug(Integer id, String price, Integer number) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(number);
        return drugs;
    }
}
