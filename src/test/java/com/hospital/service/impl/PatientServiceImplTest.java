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
    public void seekRejectsRequestsGreaterThanCurrentStockBeforeWriting() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(drug(5, 2, "12.50"));
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("5@3");

        String message = service.seek(patient);

        assertEquals("对不起药品数量不足", message);
        assertEquals(0, drugsMapper.updateCount);
        assertNull(patientMapper.updatedPatient);
        assertNull(seekMapper.updatedSeek);
    }

    @Test
    public void seekUpdatesInventoryAndLatestSeekForValidRequest() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(drug(5, 4, "12.50"));
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("5@3");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(1, drugsMapper.updateCount);
        assertEquals(Integer.valueOf(5), drugsMapper.updatedDrug.getId());
        assertEquals(Integer.valueOf(3), drugsMapper.updatedDrug.getNumber());
        assertEquals(patient, patientMapper.updatedPatient);
        assertEquals(Integer.valueOf(7), seekMapper.updatedSeek.getPatientid());
        assertEquals("5@3", seekMapper.updatedSeek.getDrugs());
        assertEquals(new BigDecimal("37.50"), seekMapper.updatedSeek.getPrice());
    }

    private static Drugs drug(Integer id, Integer stock, String price) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setNumber(stock);
        drugs.setPrice(new BigDecimal(price));
        return drugs;
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private final Drugs drug;
        Drugs updatedDrug;
        int updateCount;

        FakeDrugsMapper(Drugs drug) {
            this.drug = drug;
        }

        @Override
        public Drugs selectByPrimaryKey(Integer id) {
            return drug;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateCount++;
            updatedDrug = drugs;
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
        Patient updatedPatient;

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
        Seek updatedSeek;

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

