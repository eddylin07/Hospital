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
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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

    @Test
    public void seekCalculatesPrescriptionPriceAndUpdatesInventory() {
        PatientServiceImpl service = new PatientServiceImpl();
        service.patientMapper = patientMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;

        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("5@2,8@3");

        when(drugsMapper.selectByPrimaryKey(5)).thenReturn(drug(5, "3.25", 10));
        when(drugsMapper.selectByPrimaryKey(8)).thenReturn(drug(8, "4.00", 5));
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        ArgumentCaptor<Drugs> drugsCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper, times(2)).updateNumber(drugsCaptor.capture());
        assertEquals(Integer.valueOf(2), drugsCaptor.getAllValues().get(0).getNumber());
        assertEquals(Integer.valueOf(3), drugsCaptor.getAllValues().get(1).getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek updatedSeek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(7), updatedSeek.getPatientid());
        assertEquals("5@2,8@3", updatedSeek.getDrugs());
        assertEquals(0, new BigDecimal("18.50").compareTo(updatedSeek.getPrice()));
    }

    @Test
    public void seekDoesNotUpdateRecordsWhenRequestedQuantityExceedsStock() {
        PatientServiceImpl service = new PatientServiceImpl();
        service.patientMapper = patientMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;

        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("5@3");

        when(drugsMapper.selectByPrimaryKey(5)).thenReturn(drug(5, "3.25", 2));

        String message = service.seek(patient);

        assertTrue(message.contains("数量不足"));
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
    }

    private Drugs drug(Integer id, String price, Integer number) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(number);
        return drugs;
    }
}
