package com.hospital.service.impl;

import com.hospital.dao.DrugsMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PatientServiceImplTest {
    @Test
    public void seekRejectsQuantityGreaterThanAvailableStockBeforeMutating() {
        PatientServiceImpl service = new PatientServiceImpl();
        CapturingDrugsMapper drugsMapper = new CapturingDrugsMapper();
        CapturingPatientMapper patientMapper = new CapturingPatientMapper();
        CapturingSeekMapper seekMapper = new CapturingSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;

        Patient patient = new Patient();
        patient.setId(5);
        patient.setDrugsids("1@2");

        assertEquals("对不起药品数量不足", service.seek(patient));
        assertFalse(drugsMapper.updateCalled);
        assertFalse(patientMapper.updateCalled);
        assertFalse(seekMapper.updateCalled);
    }

    private static class CapturingDrugsMapper implements DrugsMapper {
        boolean updateCalled;

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateCalled = true;
            return 1;
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
        public Drugs selectByPrimaryKey(Integer id) {
            Drugs drugs = new Drugs();
            drugs.setId(id);
            drugs.setPrice(BigDecimal.TEN);
            drugs.setNumber(1);
            return drugs;
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
            return null;
        }

        @Override
        public Drugs findByName(String name) {
            return null;
        }

        @Override
        public List<Drugs> getDrugsByName(String name) {
            return null;
        }
    }

    private static class CapturingPatientMapper implements PatientMapper {
        boolean updateCalled;

        @Override
        public List<Patient> findAll(String name, String certId) {
            return null;
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
        public int updateByPrimaryKeySelective(Patient record) {
            updateCalled = true;
            return 1;
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
            return null;
        }
    }

    private static class CapturingSeekMapper implements SeekMapper {
        boolean updateCalled;

        @Override
        public Integer insert(Seek seek) {
            return 0;
        }

        @Override
        public Integer updateDrugs(Seek seek) {
            updateCalled = true;
            return 1;
        }

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            return null;
        }
    }
}
