package com.hospital.service.impl;

import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PatientServiceImplTest {

    @Test
    public void seekDoesNotUpdateInventoryWhenRequestedDrugQuantityExceedsStock() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.patientMapper = patientMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;

        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("1@3");

        String message = service.seek(patient);

        assertEquals("对不起阿莫西林数量不足", message);
        assertEquals(0, drugsMapper.updateNumberCalls);
        assertEquals(0, patientMapper.updateCalls);
        assertEquals(0, seekMapper.updateCalls);
    }

    private static class FakePatientMapper implements PatientMapper {
        int updateCalls;

        @Override
        public List<Patient> findAll(String name, String certId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int insert(Patient record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int insertSelective(Patient record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Patient selectByPrimaryKey(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int updateByPrimaryKeySelective(Patient record) {
            updateCalls++;
            return 1;
        }

        @Override
        public int updateByPrimaryKey(Patient record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Patient findPatientByCertId(String certId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Patient> getPatientByName(String name) {
            throw new UnsupportedOperationException();
        }
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        int updateNumberCalls;

        @Override
        public int deleteByPrimaryKey(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateNumberCalls++;
            return 1;
        }

        @Override
        public int insert(Drugs record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int insertSelective(Drugs record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Drugs selectByPrimaryKey(Integer id) {
            Drugs drugs = new Drugs();
            drugs.setId(id);
            drugs.setName("阿莫西林");
            drugs.setPrice(new BigDecimal("10.00"));
            drugs.setNumber(2);
            return drugs;
        }

        @Override
        public int updateByPrimaryKeySelective(Drugs record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int updateByPrimaryKey(Drugs record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Drugs> findAll(Drugs drugs) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Drugs findByName(String name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Drugs> getDrugsByName(String name) {
            throw new UnsupportedOperationException();
        }
    }

    private static class FakeSeekMapper implements SeekMapper {
        int updateCalls;

        @Override
        public Integer insert(Seek seek) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Integer updateDrugs(Seek seek) {
            updateCalls++;
            return 1;
        }

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            throw new UnsupportedOperationException();
        }
    }
}
