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
import static org.junit.Assert.assertFalse;
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

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    public void seekUpdatesInventoryAndLatestSeekWithCalculatedPrice() {
        Patient patient = patient(7, "1@2,2@1");
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "3.50", 5));
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(drug(2, "10.00", 1));
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);
        when(drugsMapper.updateNumber(any(Drugs.class))).thenReturn(1);

        String message = patientService.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        verify(patientMapper).updateByPrimaryKeySelective(patient);

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek seek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(7), seek.getPatientid());
        assertEquals("1@2,2@1", seek.getDrugs());
        assertEquals(0, new BigDecimal("17.00").compareTo(seek.getPrice()));

        ArgumentCaptor<Drugs> drugsCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper, times(2)).updateNumber(drugsCaptor.capture());
        List<Drugs> updatedDrugs = drugsCaptor.getAllValues();
        assertEquals(Integer.valueOf(1), updatedDrugs.get(0).getId());
        assertEquals(Integer.valueOf(2), updatedDrugs.get(0).getNumber());
        assertEquals(Integer.valueOf(2), updatedDrugs.get(1).getId());
        assertEquals(Integer.valueOf(1), updatedDrugs.get(1).getNumber());
    }

    @Test
    public void seekRejectsRequestedQuantityGreaterThanStockWithoutWrites() {
        Patient patient = patient(7, "1@6");
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "3.50", 5));

        String message = patientService.seek(patient);

        assertFalse(CommonService.upd_message_success.equals(message));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
    }

    @Test
    public void seekRejectsNonPositiveQuantityWithoutWrites() {
        Patient patient = patient(7, "1@0");

        String message = patientService.seek(patient);

        assertFalse(CommonService.upd_message_success.equals(message));
        verify(drugsMapper, never()).selectByPrimaryKey(1);
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
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
