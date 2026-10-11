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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PatientServiceImplTest {
    @Test
    public void rejectsDispenseWhenRequestedQuantityExceedsStock() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(5);
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("10@6");

        String message = service.seek(patient);

        assertEquals("对不起Aspirin数量不足", message);
        assertFalse(drugsMapper.updated);
        assertFalse(patientMapper.updated);
        assertFalse(seekMapper.updated);
    }

    @Test
    public void successfulDispenseUpdatesStockPatientAndLatestSeek() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(5);
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("10@2");

        String message = service.seek(patient);

        assertEquals("更新成功", message);
        assertTrue(drugsMapper.updated);
        assertEquals(Integer.valueOf(10), drugsMapper.updatedDrug.getId());
        assertEquals(Integer.valueOf(2), drugsMapper.updatedDrug.getNumber());
        assertTrue(patientMapper.updated);
        assertTrue(seekMapper.updated);
        assertEquals("10@2", seekMapper.updatedSeek.getDrugs());
        assertEquals(new BigDecimal("6.00"), seekMapper.updatedSeek.getPrice());
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private final int stock;
        private boolean updated;
        private Drugs updatedDrug;

        private FakeDrugsMapper(int stock) {
            this.stock = stock;
        }

        @Override
        public Drugs selectByPrimaryKey(Integer id) {
            Drugs drugs = new Drugs();
            drugs.setId(id);
            drugs.setName("Aspirin");
            drugs.setPrice(new BigDecimal("3.00"));
            drugs.setNumber(stock);
            return drugs;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            this.updated = true;
            this.updatedDrug = drugs;
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
        private boolean updated;

        @Override
        public int updateByPrimaryKeySelective(Patient record) {
            updated = true;
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
        private boolean updated;
        private Seek updatedSeek;

        @Override
        public Integer insert(Seek seek) {
            return 0;
        }

        @Override
        public Integer updateDrugs(Seek seek) {
            updated = true;
            updatedSeek = seek;
            return 1;
        }

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            Seek seek = new Seek();
            seek.setId(101);
            seek.setPatientid(patientid);
            return seek;
        }
    }
}
