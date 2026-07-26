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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class PatientServiceImplTest {

    @Test
    public void seekDispensesRequestedDrugsAndAddsPriceToLatestSeek() {
        PatientServiceImpl service = new PatientServiceImpl();
        service.patientMapper = mock(PatientMapper.class);
        service.drugsMapper = mock(DrugsMapper.class);
        service.seekMapper = mock(SeekMapper.class);

        Patient patient = new Patient();
        patient.setId(42);
        patient.setDrugsids("7@2,8@3");

        when(service.drugsMapper.selectByPrimaryKey(7)).thenReturn(drug(7, "3.50", 10));
        when(service.drugsMapper.selectByPrimaryKey(8)).thenReturn(drug(8, "2.00", 5));
        when(service.patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(service.seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        ArgumentCaptor<Drugs> dispensedDrugs = ArgumentCaptor.forClass(Drugs.class);
        verify(service.drugsMapper, times(2)).updateNumber(dispensedDrugs.capture());
        assertEquals(Integer.valueOf(7), dispensedDrugs.getAllValues().get(0).getId());
        assertEquals(Integer.valueOf(2), dispensedDrugs.getAllValues().get(0).getNumber());
        assertEquals(Integer.valueOf(8), dispensedDrugs.getAllValues().get(1).getId());
        assertEquals(Integer.valueOf(3), dispensedDrugs.getAllValues().get(1).getNumber());

        ArgumentCaptor<Seek> seek = ArgumentCaptor.forClass(Seek.class);
        verify(service.seekMapper).updateDrugs(seek.capture());
        assertEquals(Integer.valueOf(42), seek.getValue().getPatientid());
        assertEquals("7@2,8@3", seek.getValue().getDrugs());
        assertEquals(0, new BigDecimal("13.00").compareTo(seek.getValue().getPrice()));
    }

    @Test
    public void seekRejectsDrugQuantityGreaterThanAvailableStockWithoutWrites() {
        PatientServiceImpl service = new PatientServiceImpl();
        service.patientMapper = mock(PatientMapper.class);
        service.drugsMapper = mock(DrugsMapper.class);
        service.seekMapper = mock(SeekMapper.class);

        Patient patient = new Patient();
        patient.setId(42);
        patient.setDrugsids("7@2");

        when(service.drugsMapper.selectByPrimaryKey(7)).thenReturn(drug(7, "3.50", 1));

        String message = service.seek(patient);

        assertTrue(message.contains("数量不足"));
        verify(service.drugsMapper, never()).updateNumber(any(Drugs.class));
        verifyNoMoreInteractions(service.patientMapper, service.seekMapper);
    }

    private Drugs drug(Integer id, String price, Integer stock) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(stock);
        return drugs;
    }
}
