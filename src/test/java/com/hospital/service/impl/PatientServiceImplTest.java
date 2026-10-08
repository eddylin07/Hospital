package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PatientServiceImplTest {
    @Test
    public void seekRejectsOverdrawWithoutWritingPatientOrSeek() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(3);
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        service.drugsMapper = drugsMapper;

        Patient patient = new Patient();
        patient.setId(5);
        patient.setDrugsids("9@4");

        String message = service.seek(patient);

        assertEquals("对不起阿莫西林数量不足", message);
        assertEquals(0, drugsMapper.updateCalls);
        assertNull(patientMapper.updatedPatient);
        assertNull(seekMapper.updatedSeek);
    }

    @Test
    public void seekDeductsRequestedQuantityAndUpdatesPrescription() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(10);
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        service.drugsMapper = drugsMapper;

        Patient patient = new Patient();
        patient.setId(5);
        patient.setDrugsids("9@4");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(1, drugsMapper.updateCalls);
        assertEquals(Integer.valueOf(9), drugsMapper.updatedDrug.getId());
        assertEquals(Integer.valueOf(4), drugsMapper.updatedDrug.getNumber());
        assertEquals(patient, patientMapper.updatedPatient);
        assertEquals(Integer.valueOf(5), seekMapper.updatedSeek.getPatientid());
        assertEquals("9@4", seekMapper.updatedSeek.getDrugs());
        assertEquals(new BigDecimal("100.00"), seekMapper.updatedSeek.getPrice());
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private final int stock;
        private int updateCalls;
        private Drugs updatedDrug;

        FakeDrugsMapper(int stock) {
            this.stock = stock;
        }

        @Override
        public Drugs selectByPrimaryKey(Integer id) {
            Drugs drugs = new Drugs();
            drugs.setId(id);
            drugs.setName("阿莫西林");
            drugs.setPrice(new BigDecimal("25.00"));
            drugs.setNumber(stock);
            return drugs;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateCalls++;
            updatedDrug = drugs;
            return stock >= drugs.getNumber() ? 1 : 0;
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int insert(Drugs record) {
            return 0;
        }

        @Override
        public int insertSelective(Drugs record) {
            return 0;
        }

        @Override
        public int updateByPrimaryKeySelective(Drugs record) {
            return 0;
        }

        @Override
        public int updateByPrimaryKey(Drugs record) {
            return 0;
        }

        @Override
        public List<Drugs> findAll(Drugs drugs) {
            return Collections.emptyList();
        }

        @Override
        public Drugs findByName(String name) {
            return null;
        }

        @Override
        public List<Drugs> getDrugsByName(String name) {
            return Collections.emptyList();
        }
    }

    private static class FakePatientMapper implements PatientMapper {
        private Patient updatedPatient;

        @Override
        public int updateByPrimaryKeySelective(Patient record) {
            updatedPatient = record;
            return 1;
        }

        @Override
        public List<Patient> findAll(String name, String certId) {
            return Collections.emptyList();
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int insert(Patient record) {
            return 0;
        }

        @Override
        public int insertSelective(Patient record) {
            return 0;
        }

        @Override
        public Patient selectByPrimaryKey(Integer id) {
            return null;
        }

        @Override
        public int updateByPrimaryKey(Patient record) {
            return 0;
        }

        @Override
        public Patient findPatientByCertId(String certId) {
            return null;
        }

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            return null;
        }

        @Override
        public List<Patient> getPatientByName(String name) {
            return Collections.emptyList();
        }
    }

    private static class FakeSeekMapper implements SeekMapper {
        private Seek updatedSeek;

        @Override
        public Integer updateDrugs(Seek seek) {
            updatedSeek = seek;
            return 1;
        }

        @Override
        public Integer insert(Seek seek) {
            return 0;
        }

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            return null;
        }
    }
}
