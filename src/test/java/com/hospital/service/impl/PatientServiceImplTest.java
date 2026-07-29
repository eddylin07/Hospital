package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.*;
import com.hospital.entity.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class PatientServiceImplTest {
    @Test
    public void seekRejectsQuantityGreaterThanStockBeforeUpdatingAnything() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper();
        drugsMapper.savedDrug = drug(5, "A", 3, "2.50");
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;

        Patient patient = new Patient();
        patient.setId(9);
        patient.setDrugsids("5@10");

        assertEquals("对不起A数量不足", service.seek(patient));
        assertEquals(0, drugsMapper.updateNumberCount);
        assertEquals(0, patientMapper.updateSelectiveCount);
        assertEquals(0, seekMapper.updateDrugsCount);
    }

    @Test
    public void seekUpdatesOnlyTheLatestSeekRecord() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper();
        drugsMapper.savedDrug = drug(5, "A", 10, "2.50");
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        Seek latestSeek = new Seek();
        latestSeek.setId(88);
        latestSeek.setPatientid(9);
        seekMapper.latestSeek = latestSeek;
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;

        Patient patient = new Patient();
        patient.setId(9);
        patient.setDrugsids("5@2");

        assertEquals(CommonService.upd_message_success, service.seek(patient));
        assertEquals(1, drugsMapper.updateNumberCount);
        assertEquals(Integer.valueOf(2), drugsMapper.updatedDrug.getNumber());
        assertEquals(1, seekMapper.updateDrugsCount);
        assertEquals(88L, seekMapper.updatedSeek.getId());
        assertEquals(new BigDecimal("5.00"), seekMapper.updatedSeek.getPrice());
    }

    private static Drugs drug(Integer id, String name, Integer number, String price) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setName(name);
        drugs.setNumber(number);
        drugs.setPrice(new BigDecimal(price));
        return drugs;
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private Drugs savedDrug;
        private Drugs updatedDrug;
        private int updateNumberCount;

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateNumberCount++;
            updatedDrug = drugs;
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
            return savedDrug != null && savedDrug.getId().equals(id) ? savedDrug : null;
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

    private static class FakeSeekMapper implements SeekMapper {
        private Seek latestSeek;
        private Seek updatedSeek;
        private int updateDrugsCount;

        @Override
        public Integer insert(Seek seek) {
            return 0;
        }

        @Override
        public Integer updateDrugs(Seek seek) {
            updateDrugsCount++;
            updatedSeek = seek;
            return 1;
        }

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            return latestSeek;
        }
    }

    private static class FakePatientMapper implements PatientMapper {
        private int updateSelectiveCount;

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
        public int updateByPrimaryKeySelective(Patient record) {
            updateSelectiveCount++;
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
            return Collections.emptyList();
        }
    }
}
