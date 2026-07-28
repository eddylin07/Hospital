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
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PatientServiceImplTest {

    private PatientServiceImpl service;
    private PatientMapper patientMapper;
    private DrugsMapper drugsMapper;
    private SeekMapper seekMapper;

    @Before
    public void setUp() {
        service = new PatientServiceImpl();
        patientMapper = mock(PatientMapper.class);
        drugsMapper = mock(DrugsMapper.class);
        seekMapper = mock(SeekMapper.class);
        ReflectionTestUtils.setField(service, "patientMapper", patientMapper);
        ReflectionTestUtils.setField(service, "drugsMapper", drugsMapper);
        ReflectionTestUtils.setField(service, "seekMapper", seekMapper);
    }

    @Test
    public void seekDispensesRequestedDrugQuantitiesAndAccumulatesPrescriptionPrice() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@2,2@3");
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "1.50", 10));
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(drug(2, "5.00", 8));
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);

        ArgumentCaptor<Drugs> dispensedDrugs = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper, times(2)).updateNumber(dispensedDrugs.capture());
        List<Drugs> updates = dispensedDrugs.getAllValues();
        assertEquals(Integer.valueOf(1), updates.get(0).getId());
        assertEquals(Integer.valueOf(2), updates.get(0).getNumber());
        assertEquals(Integer.valueOf(2), updates.get(1).getId());
        assertEquals(Integer.valueOf(3), updates.get(1).getNumber());

        ArgumentCaptor<Seek> updatedSeek = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(updatedSeek.capture());
        assertEquals(Integer.valueOf(7), updatedSeek.getValue().getPatientid());
        assertEquals("1@2,2@3", updatedSeek.getValue().getDrugs());
        assertEquals(0, new BigDecimal("18.00").compareTo(updatedSeek.getValue().getPrice()));
    }

    private static Drugs drug(Integer id, String price, Integer stock) {
        Drugs drug = new Drugs();
        drug.setId(id);
        drug.setPrice(new BigDecimal(price));
        drug.setNumber(stock);
        return drug;
    }
}
