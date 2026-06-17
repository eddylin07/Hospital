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
import java.util.List;

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

    private PatientServiceImpl service;

    @Before
    public void setUp() {
        service = new PatientServiceImpl();
        service.patientMapper = patientMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;
    }

    @Test
    public void seekParsesSelectedDrugsAndPersistsCalculatedTotal() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("10@2,11@3");

        when(drugsMapper.selectByPrimaryKey(10)).thenReturn(drug(10, "12.50", 20));
        when(drugsMapper.selectByPrimaryKey(11)).thenReturn(drug(11, "3.00", 5));
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);

        ArgumentCaptor<Drugs> updatedDrugs = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper, times(2)).updateNumber(updatedDrugs.capture());
        List<Drugs> updatedDrugValues = updatedDrugs.getAllValues();
        assertEquals(Integer.valueOf(10), updatedDrugValues.get(0).getId());
        assertEquals(Integer.valueOf(2), updatedDrugValues.get(0).getNumber());
        assertEquals(Integer.valueOf(11), updatedDrugValues.get(1).getId());
        assertEquals(Integer.valueOf(3), updatedDrugValues.get(1).getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek savedSeek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(7), savedSeek.getPatientid());
        assertEquals("10@2,11@3", savedSeek.getDrugs());
        assertEquals(0, new BigDecimal("34.00").compareTo(savedSeek.getPrice()));
    }

    private Drugs drug(Integer id, String price, Integer stock) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(stock);
        return drugs;
    }
}
