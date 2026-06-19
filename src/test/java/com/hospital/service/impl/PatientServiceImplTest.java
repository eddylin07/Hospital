package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DoctorMapper;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.IllnessMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Doctor;
import com.hospital.entity.Drugs;
import com.hospital.entity.Illness;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PatientServiceImplTest {

    @Mock
    private PatientMapper patientMapper;

    @Mock
    private LoginMapper loginMapper;

    @Mock
    private DoctorMapper doctorMapper;

    @Mock
    private IllnessMapper illnessMapper;

    @Mock
    private DrugsMapper drugsMapper;

    @Mock
    private SeekMapper seekMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Captor
    private ArgumentCaptor<Drugs> drugsCaptor;

    @Captor
    private ArgumentCaptor<Seek> seekCaptor;

    @Test
    public void seekUpdatesInventoryAndPersistsTotalPriceForSelectedDrugs() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@2,2@3");

        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "12.50", 10));
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(drug(2, "3.00", 7));
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = patientService.seek(patient);

        assertThat(message).isEqualTo(CommonService.upd_message_success);

        verify(drugsMapper, times(2)).updateNumber(drugsCaptor.capture());
        List<Drugs> inventoryUpdates = drugsCaptor.getAllValues();
        assertThat(inventoryUpdates).extracting(Drugs::getId).containsExactly(1, 2);
        assertThat(inventoryUpdates).extracting(Drugs::getNumber).containsExactly(2, 3);

        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek seek = seekCaptor.getValue();
        assertThat(seek.getPatientid()).isEqualTo(7);
        assertThat(seek.getDrugs()).isEqualTo("1@2,2@3");
        assertThat(seek.getPrice()).isEqualByComparingTo(new BigDecimal("34.00"));
    }

    @Test
    public void serrchInfoWithDoctorTypeDelegatesToDoctorMapper() {
        Doctor doctor = new Doctor();
        doctor.setName("Li");
        List<Doctor> doctors = Collections.singletonList(doctor);
        when(doctorMapper.getDoctorByName("Li")).thenReturn(doctors);

        Map<String, List> result = patientService.serrchInfo("Li", "doctor");

        assertThat(result).containsEntry("doctor", doctors);
        verify(doctorMapper).getDoctorByName("Li");
        verifyZeroInteractions(illnessMapper);
    }

    @Test
    public void serrchInfoWithIllnessTypeDelegatesToIllnessMapper() {
        Illness illness = new Illness();
        illness.setName("cold");
        List<Illness> illnesses = Collections.singletonList(illness);
        when(illnessMapper.getIllnessByName("cold")).thenReturn(illnesses);

        Map<String, List> result = patientService.serrchInfo("cold", "illness");

        assertThat(result).containsEntry("illness", illnesses);
        verify(illnessMapper).getIllnessByName("cold");
        verifyZeroInteractions(doctorMapper);
    }

    @Test
    public void serrchInfoWithOtherTypeDefaultsToDrugMapper() {
        Drugs aspirin = drug(3, "1.50", 100);
        List<Drugs> drugs = Collections.singletonList(aspirin);
        when(drugsMapper.getDrugsByName("aspirin")).thenReturn(drugs);

        Map<String, List> result = patientService.serrchInfo("aspirin", "drugs");

        assertThat(result).containsEntry("drugs", drugs);
        verify(drugsMapper).getDrugsByName("aspirin");
        verifyZeroInteractions(doctorMapper, illnessMapper);
    }

    private Drugs drug(Integer id, String price, Integer number) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(number);
        return drugs;
    }
}
