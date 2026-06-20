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
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class PatientServiceImplTest {

    @Test
    public void seekUpdatesOnlyLatestSeekRecordForPatient() {
        PatientServiceImpl service = new PatientServiceImpl();
        service.patientMapper = mock(PatientMapper.class);
        service.drugsMapper = mock(DrugsMapper.class);
        service.seekMapper = mock(SeekMapper.class);

        Patient patient = new Patient();
        patient.setId(5);
        patient.setDrugsids("7@2");
        Seek latestSeek = new Seek();
        latestSeek.setId(42L);
        Drugs drug = new Drugs();
        drug.setId(7);
        drug.setPrice(new BigDecimal("10.00"));
        drug.setNumber(50);

        when(service.seekMapper.getSeekByPatientId(5)).thenReturn(latestSeek);
        when(service.drugsMapper.selectByPrimaryKey(7)).thenReturn(drug);
        when(service.patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(service.seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        assertEquals(CommonService.upd_message_success, service.seek(patient));

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(service.seekMapper).updateDrugs(seekCaptor.capture());
        Seek updatedSeek = seekCaptor.getValue();
        assertEquals(42L, updatedSeek.getId());
        assertEquals(Integer.valueOf(5), updatedSeek.getPatientid());
        assertEquals("7@2", updatedSeek.getDrugs());
        assertEquals(new BigDecimal("20.00"), updatedSeek.getPrice());
    }

    @Test
    public void seekDoesNotChangeInventoryWhenPatientHasNoSeekRecord() {
        PatientServiceImpl service = new PatientServiceImpl();
        service.patientMapper = mock(PatientMapper.class);
        service.drugsMapper = mock(DrugsMapper.class);
        service.seekMapper = mock(SeekMapper.class);

        Patient patient = new Patient();
        patient.setId(5);
        patient.setDrugsids("7@2");
        when(service.seekMapper.getSeekByPatientId(5)).thenReturn(null);

        assertEquals(CommonService.upd_message_error, service.seek(patient));

        verifyZeroInteractions(service.drugsMapper);
        verify(service.patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(service.seekMapper, never()).updateDrugs(any(Seek.class));
    }
}
