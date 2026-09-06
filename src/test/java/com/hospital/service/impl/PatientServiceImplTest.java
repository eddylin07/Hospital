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

public class PatientServiceImplTest {

    @Test
    public void seekRejectsRequestedQuantityGreaterThanStockWithoutWrites() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper();
        drugsMapper.drug = drug(10, 5, "2.00");
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        service.drugsMapper = drugsMapper;

        Patient patient = new Patient();
        patient.setId(1);
        patient.setDrugsids("10@6");

        assertEquals("对不起药品数量不足", service.seek(patient));
        assertEquals(0, drugsMapper.updateNumberCalls);
        assertFalse(patientMapper.updated);
        assertFalse(seekMapper.updated);
    }

    @Test
    public void seekDoesNotWritePrescriptionWhenAtomicStockUpdateFails() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper();
        drugsMapper.drug = drug(10, 10, "2.00");
        drugsMapper.updateNumberResult = 0;
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        service.drugsMapper = drugsMapper;

        Patient patient = new Patient();
        patient.setId(1);
        patient.setDrugsids("10@6");

        assertEquals("对不起药品数量不足", service.seek(patient));
        assertEquals(1, drugsMapper.updateNumberCalls);
        assertFalse(patientMapper.updated);
        assertFalse(seekMapper.updated);
    }

    @Test
    public void seekUpdatesPatientAndLatestSeekAfterSuccessfulStockDeduction() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper();
        drugsMapper.drug = drug(10, 10, "2.00");
        service.patientMapper = patientMapper;
        service.seekMapper = seekMapper;
        service.drugsMapper = drugsMapper;

        Patient patient = new Patient();
        patient.setId(1);
        patient.setDrugsids("10@6");

        assertEquals(CommonService.upd_message_success, service.seek(patient));
        assertEquals(Integer.valueOf(10), drugsMapper.updatedDrugId);
        assertEquals(Integer.valueOf(6), drugsMapper.updatedNumber);
        assertEquals(new BigDecimal("12.00"), seekMapper.updatedSeek.getPrice());
    }

    private static Drugs drug(Integer id, Integer number, String price) {
        Drugs drug = new Drugs();
        drug.setId(id);
        drug.setNumber(number);
        drug.setPrice(new BigDecimal(price));
        return drug;
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
            return null;
        }
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private Drugs drug;
        private int updateNumberResult = 1;
        private int updateNumberCalls;
        private Integer updatedDrugId;
        private Integer updatedNumber;

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int updateNumber(Integer id, Integer number) {
            updateNumberCalls++;
            updatedDrugId = id;
            updatedNumber = number;
            return updateNumberResult;
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
}
