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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PatientServiceImplTest {
    @Test
    public void insufficientDrugStockDoesNotUpdateInventoryPatientOrSeek() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(drug(5, 3));
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        Patient patient = new Patient();
        patient.setId(1);
        patient.setDrugsids("5@4");

        String message = service.seek(patient);

        assertEquals("对不起药品数量不足", message);
        assertEquals(0, drugsMapper.updateNumberCalls);
        assertFalse(patientMapper.updated);
        assertFalse(seekMapper.updated);
    }

    @Test
    public void dispensingUpdatesOnlyLatestSeekRecord() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(drug(5, 10));
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        Patient patient = new Patient();
        patient.setId(1);
        patient.setDrugsids("5@3");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(1, drugsMapper.updateNumberCalls);
        assertEquals(Integer.valueOf(3), drugsMapper.lastUpdated.getNumber());
        assertTrue(patientMapper.updated);
        assertTrue(seekMapper.updated);
        assertEquals(22L, seekMapper.updatedSeek.getId());
        assertEquals(Integer.valueOf(1), seekMapper.updatedSeek.getPatientid());
    }

    private static Drugs drug(int id, int number) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setNumber(number);
        drugs.setPrice(new BigDecimal("2.00"));
        return drugs;
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private final Drugs drug;
        private int updateNumberCalls;
        private Drugs lastUpdated;

        private FakeDrugsMapper(Drugs drug) {
            this.drug = drug;
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateNumberCalls++;
            lastUpdated = drugs;
            return drug.getNumber() >= drugs.getNumber() ? 1 : 0;
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
            return drug;
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
            updated = true;
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
            seek.setId(22L);
            seek.setPatientid(patientid);
            return seek;
        }
    }
}
