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
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PatientServiceImplTest {

    @Test
    public void rejectsPrescriptionThatExceedsStockBeforeUpdating() {
        PatientServiceImpl service = serviceWithDrug(5, 1);
        Patient patient = patient("1@6");

        String message = service.seek(patient);

        assertEquals("对不起药品数量不足", message);
        assertEquals(0, ((FakeDrugsMapper) service.drugsMapper).updateCount);
        assertEquals(0, ((FakePatientMapper) service.patientMapper).updateCount);
        assertEquals(0, ((FakeSeekMapper) service.seekMapper).updateCount);
    }

    @Test
    public void atomicStockUpdateFailureStopsPatientAndSeekUpdates() {
        PatientServiceImpl service = serviceWithDrug(5, 0);
        Patient patient = patient("1@3");

        String message = service.seek(patient);

        assertEquals("对不起药品数量不足", message);
        assertEquals(1, ((FakeDrugsMapper) service.drugsMapper).updateCount);
        assertEquals(0, ((FakePatientMapper) service.patientMapper).updateCount);
        assertEquals(0, ((FakeSeekMapper) service.seekMapper).updateCount);
    }

    @Test
    public void validPrescriptionUpdatesPatientAndLatestSeek() {
        PatientServiceImpl service = serviceWithDrug(5, 1);
        Patient patient = patient("1@3");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(1, ((FakeDrugsMapper) service.drugsMapper).updateCount);
        assertEquals(1, ((FakePatientMapper) service.patientMapper).updateCount);
        assertEquals(1, ((FakeSeekMapper) service.seekMapper).updateCount);
    }

    private PatientServiceImpl serviceWithDrug(int stock, int updateResult) {
        PatientServiceImpl service = new PatientServiceImpl();
        service.patientMapper = new FakePatientMapper();
        service.drugsMapper = new FakeDrugsMapper(stock, updateResult);
        service.seekMapper = new FakeSeekMapper();
        return service;
    }

    private Patient patient(String drugsids) {
        Patient patient = new Patient();
        patient.setId(100);
        patient.setDrugsids(drugsids);
        return patient;
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private final int stock;
        private final int updateResult;
        private int updateCount;

        private FakeDrugsMapper(int stock, int updateResult) {
            this.stock = stock;
            this.updateResult = updateResult;
        }

        @Override
        public Drugs selectByPrimaryKey(Integer id) {
            Drugs drugs = new Drugs();
            drugs.setId(id);
            drugs.setName("drug");
            drugs.setNumber(stock);
            drugs.setPrice(new BigDecimal("2.00"));
            return drugs;
        }

        @Override
        public int updateNumber(Drugs drugs) { updateCount++; return updateResult; }
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
        public List<Drugs> findAll(Drugs drugs) { return null; }
        @Override
        public Drugs findByName(String name) { return null; }
        @Override
        public List<Drugs> getDrugsByName(String name) { return null; }
    }

    private static class FakePatientMapper implements PatientMapper {
        private int updateCount;

        @Override
        public int updateByPrimaryKeySelective(Patient record) { updateCount++; return 1; }
        @Override
        public List<Patient> findAll(String name, String certId) { return null; }
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
        public List<Patient> getPatientByName(String name) { return null; }
    }

    private static class FakeSeekMapper implements SeekMapper {
        private int updateCount;

        @Override
        public Integer updateDrugs(Seek seek) { updateCount++; return 1; }
        @Override
        public Integer insert(Seek seek) { return 0; }
        @Override
        public Seek getSeekByPatientId(Integer patientid) { return null; }
    }
}
