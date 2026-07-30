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

public class PatientServiceImplSeekTest {
    @Test
    public void insufficientStockDoesNotUpdateInventoryPatientOrSeekRows() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(drug(1, "Aspirin", "5.00", 3));
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        Patient patient = new Patient();
        patient.setId(8);
        patient.setDrugsids("1@10");

        String message = service.seek(patient);

        assertEquals("对不起Aspirin数量不足", message);
        assertEquals(0, drugsMapper.updateNumberCount);
        assertEquals(0, patientMapper.updateCount);
        assertEquals(0, seekMapper.updateCount);
    }

    @Test
    public void validPrescriptionDeductsStockAndUpdatesLatestSeekOnce() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(drug(1, "Aspirin", "5.00", 3));
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.drugsMapper = drugsMapper;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        Patient patient = new Patient();
        patient.setId(8);
        patient.setDrugsids("1@2");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(1, drugsMapper.updateNumberCount);
        assertEquals(Integer.valueOf(2), drugsMapper.lastDeducted.getNumber());
        assertEquals(1, patientMapper.updateCount);
        assertEquals(1, seekMapper.updateCount);
        assertEquals(Integer.valueOf(8), seekMapper.lastSeek.getPatientid());
        assertEquals(new BigDecimal("10.00"), seekMapper.lastSeek.getPrice());
    }

    private static Drugs drug(Integer id, String name, String price, Integer number) {
        Drugs drugs = new Drugs();
        drugs.setId(id);
        drugs.setName(name);
        drugs.setPrice(new BigDecimal(price));
        drugs.setNumber(number);
        return drugs;
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private final Drugs drugs;
        private int updateNumberCount;
        private Drugs lastDeducted;

        private FakeDrugsMapper(Drugs drugs) {
            this.drugs = drugs;
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateNumberCount++;
            lastDeducted = drugs;
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
        private int updateCount;

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
            updateCount++;
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
        private int updateCount;
        private Seek lastSeek;

        @Override
        public Integer insert(Seek seek) {
            return 0;
        }

        @Override
        public Integer updateDrugs(Seek seek) {
            updateCount++;
            lastSeek = seek;
            return 1;
        }

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            return null;
        }
    }
}
