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
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
        service = new PatientServiceImpl();
        service.patientMapper = patientMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;
    }

    @Test
    public void seekUpdatesInventoryAndSeekRecordWithCalculatedPrice() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("11@2,12@3");
        when(drugsMapper.selectByPrimaryKey(11)).thenReturn(drug(11, "5.50", 10));
        when(drugsMapper.selectByPrimaryKey(12)).thenReturn(drug(12, "3.25", 8));
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);

        ArgumentCaptor<Drugs> drugsCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper, times(2)).updateNumber(drugsCaptor.capture());
        List<Drugs> updatedDrugs = drugsCaptor.getAllValues();
        assertEquals(Integer.valueOf(11), updatedDrugs.get(0).getId());
        assertEquals(Integer.valueOf(2), updatedDrugs.get(0).getNumber());
        assertEquals(Integer.valueOf(12), updatedDrugs.get(1).getId());
        assertEquals(Integer.valueOf(3), updatedDrugs.get(1).getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek updatedSeek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(7), updatedSeek.getPatientid());
        assertEquals("11@2,12@3", updatedSeek.getDrugs());
        assertTrue(new BigDecimal("20.75").compareTo(updatedSeek.getPrice()) == 0);
    }

    private Drugs drug(Integer id, String price, Integer stock) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(stock);
        return drugs;
    }
}
