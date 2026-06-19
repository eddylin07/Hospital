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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class PatientServiceImplTest {

    @Test
    public void seekUpdatesLatestSeekRecordOnly() {
        PatientServiceImpl service=new PatientServiceImpl();
        service.patientMapper=mock(PatientMapper.class);
        service.drugsMapper=mock(DrugsMapper.class);
        service.seekMapper=mock(SeekMapper.class);
        Patient patient=new Patient();
        patient.setId(7);
        patient.setDrugsids("3@2");
        Seek latestSeek=new Seek();
        latestSeek.setId(42);
        Drugs drug=new Drugs();
        drug.setPrice(new BigDecimal("5.00"));
        drug.setNumber(10);
        when(service.seekMapper.getSeekByPatientId(7)).thenReturn(latestSeek);
        when(service.drugsMapper.selectByPrimaryKey(3)).thenReturn(drug);
        when(service.patientMapper.updateByPrimaryKeySelective(any(Patient.class))).thenReturn(1);
        when(service.seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message=service.seek(patient);

        ArgumentCaptor<Seek> seekCaptor=ArgumentCaptor.forClass(Seek.class);
        verify(service.seekMapper).updateDrugs(seekCaptor.capture());
        assertEquals(CommonService.upd_message_success,message);
        assertEquals(42,seekCaptor.getValue().getId());
        assertEquals(Integer.valueOf(7),seekCaptor.getValue().getPatientid());
        assertEquals("3@2",seekCaptor.getValue().getDrugs());
        assertEquals(new BigDecimal("10.00"),seekCaptor.getValue().getPrice());
    }

    @Test
    public void seekDoesNotDecrementStockWhenNoSeekExists() {
        PatientServiceImpl service=new PatientServiceImpl();
        service.patientMapper=mock(PatientMapper.class);
        service.drugsMapper=mock(DrugsMapper.class);
        service.seekMapper=mock(SeekMapper.class);
        Patient patient=new Patient();
        patient.setId(7);
        patient.setDrugsids("3@2");
        when(service.seekMapper.getSeekByPatientId(7)).thenReturn(null);

        String message=service.seek(patient);

        assertEquals(CommonService.upd_message_error,message);
        verify(service.drugsMapper,never()).selectByPrimaryKey(any(Integer.class));
        verify(service.patientMapper,never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(service.seekMapper,never()).updateDrugs(any(Seek.class));
        verifyNoMoreInteractions(service.drugsMapper,service.patientMapper);
    }
}
