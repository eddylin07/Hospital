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
    public void doesNotSubtractStockWhenRequestedQuantityExceedsInventory() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = new FakePatientMapper();
        service.seekMapper = new FakeSeekMapper(true);

        Patient patient = new Patient();
        patient.setId(1);
        patient.setDrugsids("2@7");

        assertEquals("对不起Aspirin数量不足", service.seek(patient));
        assertEquals(0, drugsMapper.updateNumberCount);
    }

    @Test
    public void doesNotSubtractStockWhenLatestSeekIsMissing() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = new FakePatientMapper();
        service.seekMapper = new FakeSeekMapper(false);

        Patient patient = new Patient();
        patient.setId(1);
        patient.setDrugsids("2@1");

        assertEquals("未找到就诊信息", service.seek(patient));
        assertEquals(0, drugsMapper.updateNumberCount);
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        int updateNumberCount;

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateNumberCount++;
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
            drugs.setName("Aspirin");
            drugs.setNumber(5);
            drugs.setPrice(new BigDecimal("3.50"));
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

    private static class FakePatientMapper implements PatientMapper {
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

    private static class FakeSeekMapper implements SeekMapper {
        private final boolean hasSeek;

        FakeSeekMapper(boolean hasSeek) {
            this.hasSeek = hasSeek;
        }

        @Override
        public Integer insert(Seek seek) {
            return 1;
        }

        @Override
        public Integer updateDrugs(Seek seek) {
            return 1;
        }

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            return hasSeek ? new Seek() : null;
        }
    }
}
