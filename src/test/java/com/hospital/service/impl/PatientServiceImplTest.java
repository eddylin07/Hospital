package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PatientServiceImplTest {

    @Test
    public void seekDispensesAllRequestedDrugsAndAddsTotalPriceToLatestSeek() {
        PatientServiceImpl service = serviceWithMocks();
        Patient patient = patientWithDrugs("1@2,2@1");

        when(service.drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "5.50", 10));
        when(service.drugsMapper.selectByPrimaryKey(2)).thenReturn(drug(2, "2.25", 3));
        when(service.drugsMapper.updateNumber(any(Drugs.class))).thenReturn(1);
        when(service.patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(service.seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);

        ArgumentCaptor<Drugs> drugsCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(service.drugsMapper, times(2)).updateNumber(drugsCaptor.capture());
        List<Drugs> dispensedDrugs = drugsCaptor.getAllValues();
        assertEquals(Integer.valueOf(2), dispensedDrugs.get(0).getNumber());
        assertEquals(Integer.valueOf(1), dispensedDrugs.get(1).getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(service.seekMapper).updateDrugs(seekCaptor.capture());
        Seek seek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(7), seek.getPatientid());
        assertEquals("1@2,2@1", seek.getDrugs());
        assertTrue(new BigDecimal("13.25").compareTo(seek.getPrice()) == 0);
    }

    @Test
    public void seekDoesNotPartiallyWriteWhenAnyRequestedDrugExceedsStock() {
        PatientServiceImpl service = serviceWithMocks();
        Patient patient = patientWithDrugs("1@1,2@3");

        when(service.drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "5.50", 10));
        when(service.drugsMapper.selectByPrimaryKey(2)).thenReturn(drug(2, "2.25", 2));

        String message = service.seek(patient);

        assertEquals("对不起2数量不足", message);
        verify(service.drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(service.patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(service.seekMapper, never()).updateDrugs(any(Seek.class));
    }

    private PatientServiceImpl serviceWithMocks() {
        PatientServiceImpl service = new PatientServiceImpl();
        service.drugsMapper = mock(DrugsMapper.class);
        service.patientMapper = mock(PatientMapper.class);
        service.seekMapper = mock(SeekMapper.class);
        return service;
    }

    private Patient patientWithDrugs(String drugsids) {
        Patient patient = new Patient();
        patient.setId(7);
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
