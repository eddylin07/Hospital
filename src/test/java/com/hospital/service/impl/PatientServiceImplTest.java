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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
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
    public void seekUpdatesDrugInventoryAndTreatmentPriceForAllPrescribedDrugs() {
        Patient patient = new Patient();
        patient.setId(42);
        patient.setDrugsids("7@2,9@3");

        when(drugsMapper.selectByPrimaryKey(7)).thenReturn(drug(7, "12.50", 5));
        when(drugsMapper.selectByPrimaryKey(9)).thenReturn(drug(9, "3.20", 10));
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = patientService.seek(patient);

        assertEquals(CommonService.upd_message_success, message);

        ArgumentCaptor<Drugs> drugCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper, times(2)).updateNumber(drugCaptor.capture());
        assertEquals(Integer.valueOf(7), drugCaptor.getAllValues().get(0).getId());
        assertEquals(Integer.valueOf(2), drugCaptor.getAllValues().get(0).getNumber());
        assertEquals(Integer.valueOf(9), drugCaptor.getAllValues().get(1).getId());
        assertEquals(Integer.valueOf(3), drugCaptor.getAllValues().get(1).getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek seek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(42), seek.getPatientid());
        assertEquals("7@2,9@3", seek.getDrugs());
        assertEquals(0, new BigDecimal("34.60").compareTo(seek.getPrice()));

        verify(patientMapper).updateByPrimaryKeySelective(patient);
    }

    private Drugs drug(Integer id, String price, Integer number) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(number);
        return drugs;
    }
}
