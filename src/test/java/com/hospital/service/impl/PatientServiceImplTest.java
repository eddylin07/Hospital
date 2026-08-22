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

public class PatientServiceImplTest {
    @Test
    public void rejectsRequestedQuantityGreaterThanStockBeforeWriting() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(drug(1, "5.00", 5), 1);
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        Patient patient = patient(9, "1@6");

        String message = service.seek(patient);

        assertEquals("药品库存不足", message);
        assertEquals(0, drugsMapper.updateCalls);
        assertEquals(0, patientMapper.updateCalls);
        assertEquals(0, seekMapper.updateCalls);
    }

    @Test
    public void atomicStockGuardFailureSkipsPatientAndSeekWrites() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(drug(1, "5.00", 10), 0);
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        Patient patient = patient(9, "1@6");

        String message = service.seek(patient);

        assertEquals("药品库存不足", message);
        assertEquals(1, drugsMapper.updateCalls);
        assertEquals(0, patientMapper.updateCalls);
        assertEquals(0, seekMapper.updateCalls);
    }

    @Test
    public void validDispenseUpdatesStockAndLatestSeek() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(drug(1, "5.00", 10), 1);
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        Patient patient = patient(9, "1@3");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(1, drugsMapper.updateCalls);
        assertEquals(3, drugsMapper.lastRequestedNumber);
        assertEquals(1, patientMapper.updateCalls);
        assertEquals(1, seekMapper.updateCalls);
        assertEquals(9, seekMapper.lastSeek.getPatientid().intValue());
        assertEquals(new BigDecimal("15.00"), seekMapper.lastSeek.getPrice());
    }

    private static Patient patient(Integer id, String drugsids) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setDrugsids(drugsids);
        return patient;
    }

    private static Drugs drug(Integer id, String price, Integer number) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(number);
        return drugs;
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private final Drugs drug;
        private final int updateResult;
        private int updateCalls;
        private int lastRequestedNumber;

        private FakeDrugsMapper(Drugs drug, int updateResult) {
            this.drug = drug;
            this.updateResult = updateResult;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateCalls++;
            lastRequestedNumber = drugs.getNumber();
            return updateResult;
        }

        @Override
        public Drugs selectByPrimaryKey(Integer id) {
            return drug;
        }

        @Override
        public int deleteByPrimaryKey(Integer id) { return 0; }
        @Override
        public int insert(Drugs record) { return 0; }
        @Override
        public int insertSelective(Drugs record) { return 0; }
        @Override
        public int updateByPrimaryKeySelective(Drugs record) { return 0; }
        @Override
        public int updateByPrimaryKey(Drugs record) { return 0; }
        @Override
        public List<Drugs> findAll(Drugs drugs) { return Collections.emptyList(); }
        @Override
        public Drugs findByName(String name) { return null; }
        @Override
        public List<Drugs> getDrugsByName(String name) { return Collections.emptyList(); }
    }

    private static class FakePatientMapper implements PatientMapper {
        private int updateCalls;

        @Override
        public int updateByPrimaryKeySelective(Patient record) {
            updateCalls++;
            return 1;
        }

        @Override
        public List<Patient> findAll(String name, String certId) { return Collections.emptyList(); }
        @Override
        public int deleteByPrimaryKey(Integer id) { return 0; }
        @Override
        public int insert(Patient record) { return 0; }
        @Override
        public int insertSelective(Patient record) { return 0; }
        @Override
        public Patient selectByPrimaryKey(Integer id) { return null; }
        @Override
        public int updateByPrimaryKey(Patient record) { return 0; }
        @Override
        public Patient findPatientByCertId(String certId) { return null; }
        @Override
        public Patient findPatientByLoginId(Integer loginid) { return null; }
        @Override
        public List<Patient> getPatientByName(String name) { return Collections.emptyList(); }
    }

    private static class FakeSeekMapper implements SeekMapper {
        private int updateCalls;
        private Seek lastSeek;

        @Override
        public Integer updateDrugs(Seek seek) {
            updateCalls++;
            lastSeek = seek;
            return 1;
        }

        @Override
        public Integer insert(Seek seek) { return 0; }
        @Override
        public Seek getSeekByPatientId(Integer patientid) { return null; }
    }
}
