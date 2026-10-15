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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PatientServiceImplTest {
    @Test
    public void seekRejectsQuantityGreaterThanStockBeforeWriting() {
        PatientServiceImpl service = new PatientServiceImpl();
        StubDrugsMapper drugsMapper = new StubDrugsMapper(3);
        StubPatientMapper patientMapper = new StubPatientMapper();
        StubSeekMapper seekMapper = new StubSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;

        Patient patient = new Patient();
        patient.setId(11);
        patient.setDrugsids("1@10");

        String message = service.seek(patient);

        assertEquals("对不起药品A数量不足", message);
        assertFalse(drugsMapper.updated);
        assertFalse(patientMapper.updated);
        assertFalse(seekMapper.updated);
    }

    @Test
    public void seekRejectsEmptyDrugSelection() {
        PatientServiceImpl service = new PatientServiceImpl();
        Patient patient = new Patient();
        patient.setId(11);
        patient.setDrugsids("");

        assertEquals("请选择药品", service.seek(patient));
    }

    @Test
    public void seekDeductsStockAndUpdatesSeekWhenEnoughStockExists() {
        PatientServiceImpl service = new PatientServiceImpl();
        StubDrugsMapper drugsMapper = new StubDrugsMapper(10);
        StubPatientMapper patientMapper = new StubPatientMapper();
        StubSeekMapper seekMapper = new StubSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;

        Patient patient = new Patient();
        patient.setId(11);
        patient.setDrugsids("1@4");

        String message = service.seek(patient);

        assertEquals("更新成功", message);
        assertTrue(drugsMapper.updated);
        assertEquals(Integer.valueOf(4), drugsMapper.lastUpdate.getNumber());
        assertTrue(patientMapper.updated);
        assertTrue(seekMapper.updated);
        assertEquals(new BigDecimal("10.00"), seekMapper.lastSeek.getPrice());
    }

    private static class StubDrugsMapper implements DrugsMapper {
        private final int stock;
        private boolean updated;
        private Drugs lastUpdate;

        private StubDrugsMapper(int stock) {
            this.stock = stock;
        }

        @Override
        public Drugs selectByPrimaryKey(Integer id) {
            Drugs drugs = new Drugs();
            drugs.setId(id);
            drugs.setName("药品A");
            drugs.setNumber(stock);
            drugs.setPrice(new BigDecimal("2.50"));
            return drugs;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updated = true;
            lastUpdate = drugs;
            return stock >= drugs.getNumber() ? 1 : 0;
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            throw new UnsupportedOperationException();
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

    private static class StubPatientMapper implements PatientMapper {
        private boolean updated;

        @Override
        public int updateByPrimaryKeySelective(Patient record) {
            updated = true;
            return 1;
        }

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

    private static class StubSeekMapper implements SeekMapper {
        private boolean updated;
        private Seek lastSeek;

        @Override
        public Integer updateDrugs(Seek seek) {
            updated = true;
            lastSeek = seek;
            return 1;
        }

        @Override
        public Integer insert(Seek seek) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            throw new UnsupportedOperationException();
        }
    }
}
