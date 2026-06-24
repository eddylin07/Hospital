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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
    public void seekUpdatesInventoryAndAddsTotalDrugPrice() {
        Patient patient = new Patient();
        patient.setId(12);
        patient.setDrugsids("1@3,2@2");

        Drugs aspirin = drug(1, "2.50", 10);
        Drugs vitamin = drug(2, "4.50", 8);
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(aspirin);
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(vitamin);
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        assertEquals(CommonService.upd_message_success, patientService.seek(patient));

        verify(drugsMapper).updateNumber(aspirin);
        verify(drugsMapper).updateNumber(vitamin);
        assertEquals(Integer.valueOf(3), aspirin.getNumber());
        assertEquals(Integer.valueOf(2), vitamin.getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek seek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(12), seek.getPatientid());
        assertEquals("1@3,2@2", seek.getDrugs());
        assertEquals(0, new BigDecimal("16.50").compareTo(seek.getPrice()));
    }

    @Test
    public void seekDoesNotDecrementInventoryWhenDrugIsOutOfStock() {
        Patient patient = new Patient();
        patient.setId(12);
        patient.setDrugsids("1@3");

        Drugs aspirin = drug(1, "2.50", 0);
        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(aspirin);
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        assertEquals(CommonService.upd_message_success, patientService.seek(patient));

        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        assertTrue(BigDecimal.ZERO.compareTo(seekCaptor.getValue().getPrice()) == 0);
    }

    private Drugs drug(Integer id, String price, Integer number) {
        Drugs drug = new Drugs();
        drug.setId(id);
        drug.setPrice(new BigDecimal(price));
        drug.setNumber(number);
        return drug;
    }
}
