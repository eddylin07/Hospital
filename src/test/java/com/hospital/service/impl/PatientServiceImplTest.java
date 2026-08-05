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
import org.mockito.InOrder;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PatientServiceImplTest {

    private final PatientServiceImpl service = new PatientServiceImpl();
    private final PatientMapper patientMapper = mock(PatientMapper.class);
    private final DrugsMapper drugsMapper = mock(DrugsMapper.class);
    private final SeekMapper seekMapper = mock(SeekMapper.class);

    @Test
    public void seekDispensesRequestedDrugsAndAddsTotalPriceToSeekRecord() {
        service.patientMapper = patientMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;

        Patient patient = new Patient();
        patient.setId(42);
        patient.setDrugsids("1@2,2@1");

        when(drugsMapper.selectByPrimaryKey(1)).thenReturn(drug(1, "2.50", 10));
        when(drugsMapper.selectByPrimaryKey(2)).thenReturn(drug(2, "3.00", 4));
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);

        ArgumentCaptor<Drugs> drugsCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper, times(2)).updateNumber(drugsCaptor.capture());
        List<Drugs> updatedDrugs = drugsCaptor.getAllValues();
        assertEquals(Integer.valueOf(1), updatedDrugs.get(0).getId());
        assertEquals(Integer.valueOf(2), updatedDrugs.get(0).getNumber());
        assertEquals(Integer.valueOf(2), updatedDrugs.get(1).getId());
        assertEquals(Integer.valueOf(1), updatedDrugs.get(1).getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek seek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(42), seek.getPatientid());
        assertEquals("1@2,2@1", seek.getDrugs());
        assertEquals(new BigDecimal("8.00"), seek.getPrice());

        InOrder writes = inOrder(patientMapper, seekMapper);
        writes.verify(patientMapper).updateByPrimaryKeySelective(patient);
        writes.verify(seekMapper).updateDrugs(any(Seek.class));
    }

    private static Drugs drug(Integer id, String price, Integer number) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(number);
        return drugs;
    }
}
