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

    private PatientServiceImpl service;

    @Before
    public void setUp() {
        service = new PatientServiceImpl();
        service.patientMapper = patientMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;
    }

    @Test
    public void seekDispensesDrugsAndUpdatesLatestSeekWithCalculatedPrice() {
        Patient patient = new Patient();
        patient.setId(42);
        patient.setDrugsids("7@2,9@3");
        Drugs firstDrug = drug(7, "12.50", 10);
        Drugs secondDrug = drug(9, "3.00", 20);
        when(drugsMapper.selectByPrimaryKey(7)).thenReturn(firstDrug);
        when(drugsMapper.selectByPrimaryKey(9)).thenReturn(secondDrug);
        when(drugsMapper.updateNumber(any(Drugs.class))).thenReturn(1);
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(Integer.valueOf(2), firstDrug.getNumber());
        assertEquals(Integer.valueOf(3), secondDrug.getNumber());
        verify(drugsMapper).updateNumber(firstDrug);
        verify(drugsMapper).updateNumber(secondDrug);
        verify(patientMapper).updateByPrimaryKeySelective(patient);

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek seek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(42), seek.getPatientid());
        assertEquals("7@2,9@3", seek.getDrugs());
        assertEquals(new BigDecimal("34.00"), seek.getPrice());
    }

    private Drugs drug(Integer id, String price, Integer stockNumber) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(stockNumber);
        return drugs;
    }
}
