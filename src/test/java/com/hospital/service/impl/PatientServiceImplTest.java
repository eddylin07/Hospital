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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PatientServiceImplTest {

    private PatientServiceImpl service;

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

    @Before
    public void setUp() {
        service = new PatientServiceImpl();
        service.patientMapper = patientMapper;
        service.loginMapper = loginMapper;
        service.doctorMapper = doctorMapper;
        service.illnessMapper = illnessMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;
    }

    @Test
    public void seekDispensesDrugsAndUpdatesPatientBill() {
        Patient patient = new Patient();
        patient.setId(42);
        patient.setDrugsids("7@2,8@3");

        Drugs firstDrug = drug(7, "5.00", 10);
        Drugs secondDrug = drug(8, "2.50", 4);
        when(drugsMapper.selectByPrimaryKey(7)).thenReturn(firstDrug);
        when(drugsMapper.selectByPrimaryKey(8)).thenReturn(secondDrug);
        when(patientMapper.updateByPrimaryKeySelective(patient)).thenReturn(1);
        when(seekMapper.updateDrugs(any(Seek.class))).thenReturn(1);

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);

        ArgumentCaptor<Drugs> drugsCaptor = ArgumentCaptor.forClass(Drugs.class);
        verify(drugsMapper, times(2)).updateNumber(drugsCaptor.capture());
        List<Drugs> updatedDrugs = drugsCaptor.getAllValues();
        assertEquals(Integer.valueOf(7), updatedDrugs.get(0).getId());
        assertEquals(Integer.valueOf(2), updatedDrugs.get(0).getNumber());
        assertEquals(Integer.valueOf(8), updatedDrugs.get(1).getId());
        assertEquals(Integer.valueOf(3), updatedDrugs.get(1).getNumber());

        ArgumentCaptor<Seek> seekCaptor = ArgumentCaptor.forClass(Seek.class);
        verify(seekMapper).updateDrugs(seekCaptor.capture());
        Seek billedSeek = seekCaptor.getValue();
        assertEquals(Integer.valueOf(42), billedSeek.getPatientid());
        assertEquals("7@2,8@3", billedSeek.getDrugs());
        assertTrue(new BigDecimal("17.50").compareTo(billedSeek.getPrice()) == 0);

        InOrder order = inOrder(drugsMapper, patientMapper, seekMapper);
        order.verify(drugsMapper).selectByPrimaryKey(7);
        order.verify(drugsMapper).updateNumber(firstDrug);
        order.verify(drugsMapper).selectByPrimaryKey(8);
        order.verify(drugsMapper).updateNumber(secondDrug);
        order.verify(patientMapper).updateByPrimaryKeySelective(patient);
        order.verify(seekMapper).updateDrugs(any(Seek.class));
    }

    private Drugs drug(Integer id, String price, Integer stock) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(stock);
        return drugs;
    }
}

