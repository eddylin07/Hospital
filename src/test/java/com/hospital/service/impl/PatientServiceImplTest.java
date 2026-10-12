package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DoctorMapper;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.IllnessMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Doctor;
import com.hospital.entity.Drugs;
import com.hospital.entity.Illness;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PatientServiceImplTest {
    @Test
    public void seekDoesNotWriteWhenRequestedDrugExceedsStock() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(1);
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.patientMapper = patientMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;
        Patient patient = new Patient();
        patient.setId(9);
        patient.setDrugsids("5@2");

        String message = service.seek(patient);

        assertNotEquals(CommonService.upd_message_success, message);
        assertEquals(0, drugsMapper.updateNumberCalls);
        assertEquals(0, seekMapper.updateDrugsCalls);
        assertEquals(0, patientMapper.updateSelectiveCalls);
    }

    @Test
    public void seekUpdatesInventoryAndLatestSeekWhenStockIsAvailable() {
        PatientServiceImpl service = new PatientServiceImpl();
        FakePatientMapper patientMapper = new FakePatientMapper();
        FakeDrugsMapper drugsMapper = new FakeDrugsMapper(3);
        FakeSeekMapper seekMapper = new FakeSeekMapper();
        service.patientMapper = patientMapper;
        service.drugsMapper = drugsMapper;
        service.seekMapper = seekMapper;
        Patient patient = new Patient();
        patient.setId(9);
        patient.setDrugsids("5@2");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(1, drugsMapper.updateNumberCalls);
        assertEquals(1, seekMapper.updateDrugsCalls);
        assertEquals(1, patientMapper.updateSelectiveCalls);
        assertEquals(new BigDecimal("20.00"), seekMapper.lastSeek.getPrice());
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private final int stock;
        private int updateNumberCalls;

        private FakeDrugsMapper(int stock) {
            this.stock = stock;
        }

        @Override
        public Drugs selectByPrimaryKey(Integer id) {
            Drugs drugs = new Drugs();
            drugs.setId(id);
            drugs.setName("drug");
            drugs.setNumber(stock);
            drugs.setPrice(new BigDecimal("10.00"));
            return drugs;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateNumberCalls++;
            return stock >= drugs.getNumber() ? 1 : 0;
        }

        @Override public int deleteByPrimaryKey(Integer id) { return 0; }
        @Override public int insert(Drugs record) { return 0; }
        @Override public int insertSelective(Drugs record) { return 0; }
        @Override public int updateByPrimaryKeySelective(Drugs record) { return 0; }
        @Override public int updateByPrimaryKey(Drugs record) { return 0; }
        @Override public List<Drugs> findAll(Drugs drugs) { return Collections.emptyList(); }
        @Override public Drugs findByName(String name) { return null; }
        @Override public List<Drugs> getDrugsByName(String name) { return Collections.emptyList(); }
    }

    private static class FakeSeekMapper implements SeekMapper {
        private int updateDrugsCalls;
        private Seek lastSeek;

        @Override
        public Integer updateDrugs(Seek seek) {
            updateDrugsCalls++;
            lastSeek = seek;
            return 1;
        }

        @Override public Integer insert(Seek seek) { return 0; }
        @Override public Seek getSeekByPatientId(Integer patientid) { return null; }
    }

    private static class FakePatientMapper implements PatientMapper {
        private int updateSelectiveCalls;

        @Override
        public int updateByPrimaryKeySelective(Patient record) {
            updateSelectiveCalls++;
            return 1;
        }

        @Override public List<Patient> findAll(String name, String certId) { return Collections.emptyList(); }
        @Override public int deleteByPrimaryKey(Integer id) { return 0; }
        @Override public int insert(Patient record) { return 0; }
        @Override public int insertSelective(Patient record) { return 0; }
        @Override public Patient selectByPrimaryKey(Integer id) { return null; }
        @Override public int updateByPrimaryKey(Patient record) { return 0; }
        @Override public Patient findPatientByCertId(String certId) { return null; }
        @Override public Patient findPatientByLoginId(Integer loginid) { return null; }
        @Override public List<Patient> getPatientByName(String name) { return Collections.emptyList(); }
    }
}
