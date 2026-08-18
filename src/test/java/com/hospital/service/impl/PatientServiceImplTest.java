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

import static org.assertj.core.api.Assertions.assertThat;
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

    private PatientServiceImpl service;

    @Before
    public void setUp() {
        service = new PatientServiceImpl();
        service.patientMapper = patientMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;
    }

    @Test
    public void seekUpdatesInventoryAndLatestSeekPriceAfterValidatingAllDrugs() {
        Patient patient = patientWithDrugs("5@2,8@3");
        when(drugsMapper.selectByPrimaryKey(5)).thenReturn(drug(5, "4.50", 10));
        when(drugsMapper.selectByPrimaryKey(8)).thenReturn(drug(8, "3.00", 5));
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertThat(message).isEqualTo(CommonService.upd_message_success);

        ArgumentCaptor<Drugs> drugCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper, times(2)).updateNumber(drugCaptor.capture());
        List<Drugs> updatedDrugs = drugCaptor.getAllValues();
        assertThat(updatedDrugs).extracting(Drugs::getId).containsExactly(5, 8);
        assertThat(updatedDrugs).extracting(Drugs::getNumber).containsExactly(2, 3);

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        assertThat(seekCaptor.getValue().getPatientid()).isEqualTo(12);
        assertThat(seekCaptor.getValue().getDrugs()).isEqualTo("5@2,8@3");
        assertThat(seekCaptor.getValue().getPrice()).isEqualByComparingTo("18.00");
    }

    @Test
    public void seekDoesNotWriteWhenRequestedQuantityExceedsStock() {
        Patient patient = patientWithDrugs("5@2");
        when(drugsMapper.selectByPrimaryKey(5)).thenReturn(drug(5, "4.50", 1));

        String message = service.seek(patient);

        assertThat(message).contains("数量不足");
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
    }

    @Test
    public void seekDoesNotPartiallyWriteWhenLaterDrugFailsValidation() {
        Patient patient = patientWithDrugs("5@2,8@3");
        when(drugsMapper.selectByPrimaryKey(5)).thenReturn(drug(5, "4.50", 10));
        when(drugsMapper.selectByPrimaryKey(8)).thenReturn(drug(8, "3.00", 1));

        String message = service.seek(patient);

        assertThat(message).contains("数量不足");
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
    }

    private Patient patientWithDrugs(String drugsIds) {
        Patient patient = new Patient();
        patient.setId(12);
        patient.setDrugsids(drugsIds);
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
