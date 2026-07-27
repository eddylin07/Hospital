package com.hospital.service.impl;

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

public class PatientServiceImplTest {
    @Test
    public void seekRejectsRequestedQuantityGreaterThanStockWithoutWriting() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.patientMapper = patientMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;

        Patient patient = new Patient();
        patient.setId(123);
        patient.setDrugsids("1@2");

        String message = service.seek(patient);

        assertEquals("对不起阿司匹林数量不足", message);
        assertEquals(0, drugsMapper.updateNumberCalls);
        assertEquals(0, seekMapper.updateDrugsCalls);
        assertEquals(0, patientMapper.updateCalls);
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private int updateNumberCalls;

        @Override
        public Drugs selectByPrimaryKey(Integer id) {
            Drugs drugs = new Drugs();
            drugs.setId(id);
            drugs.setName("阿司匹林");
            drugs.setNumber(1);
            drugs.setPrice(new BigDecimal("3.50"));
            return drugs;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateNumberCalls++;
            return 1;
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
        private int updateCalls;

        @Override
        public Patient selectByPrimaryKey(Integer id) {
            Patient patient = new Patient();
            patient.setId(id);
            return patient;
        }

        @Override
        public int updateByPrimaryKeySelective(Patient record) {
            updateCalls++;
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
        private int updateDrugsCalls;

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            Seek seek = new Seek();
            seek.setPatientid(patientid);
            return seek;
        }

        @Override
        public Integer updateDrugs(Seek seek) {
            updateDrugsCalls++;
            return 1;
        }

        @Override
        public Integer insert(Seek seek) {
            return 0;
        }
    }
}
