package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DoctorMapper;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.IllnessMapper;
import com.hospital.dao.LoginMapper;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PatientServiceImplSeekTest {

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

    @Test
    public void seekDispensesRequestedDrugsAndAddsTotalPriceToSeekRecord() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("3@2,4@1");

        when(drugsMapper.selectByPrimaryKey(3)).thenReturn(drug(3, "10.50", 5));
        when(drugsMapper.selectByPrimaryKey(4)).thenReturn(drug(4, "4.00", 9));
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = patientService.seek(patient);

        assertEquals(CommonService.upd_message_success, message);

        ArgumentCaptor<Drugs> drugsCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper, times(2)).updateNumber(drugsCaptor.capture());
        List<Drugs> updatedDrugs = drugsCaptor.getAllValues();
        assertEquals(Integer.valueOf(3), updatedDrugs.get(0).getId());
        assertEquals(Integer.valueOf(2), updatedDrugs.get(0).getNumber());
        assertEquals(Integer.valueOf(4), updatedDrugs.get(1).getId());
        assertEquals(Integer.valueOf(1), updatedDrugs.get(1).getNumber());

        verify(patientMapper).updateByPrimaryKeySelective(patient);

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek seek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(7), seek.getPatientid());
        assertEquals("3@2,4@1", seek.getDrugs());
        assertEquals(0, new BigDecimal("25.00").compareTo(seek.getPrice()));
    }

    @Test
    public void seekDoesNotWriteWhenRequestedDrugQuantityExceedsStock() {
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("3@2");
        Drugs aspirin = drug(3, "10.50", 1);
        when(drugsMapper.selectByPrimaryKey(3)).thenReturn(aspirin);

        String message = patientService.seek(patient);

        assertEquals("对不起1数量不足", message);
        verify(drugsMapper, never()).updateNumber(any(Drugs.class));
        verify(patientMapper, never()).updateByPrimaryKeySelective(any(Patient.class));
        verify(seekMapper, never()).updateDrugs(any(Seek.class));
        assertEquals(Integer.valueOf(1), aspirin.getNumber());
    }

    private static Drugs drug(Integer id, String price, Integer stock) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(stock);
        return drugs;
    }
}
