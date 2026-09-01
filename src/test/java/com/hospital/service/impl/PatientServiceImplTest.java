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

public class PatientServiceImplTest {
    @Test
    public void overStockPrescriptionDoesNotDecrementInventoryOrUpdatePatient() {
        PatientServiceImpl service = serviceWithDrug(5);
        FakeDrugsMapper drugsMapper = (FakeDrugsMapper) service.drugsMapper;
        FakePatientMapper patientMapper = (FakePatientMapper) service.patientMapper;
        FakeSeekMapper seekMapper = (FakeSeekMapper) service.seekMapper;
        Patient patient = new Patient();
        patient.setId(3);
        patient.setDrugsids("1@10");

        String message = service.seek(patient);

        assertEquals("对不起Aspirin数量不足", message);
        assertEquals(0, drugsMapper.updateCount);
        assertEquals(0, patientMapper.updateCount);
        assertEquals(0, seekMapper.updateCount);
    }

    @Test
    public void successfulPrescriptionUpdatesOnlyLatestSeekRow() {
        PatientServiceImpl service = serviceWithDrug(5);
        FakeDrugsMapper drugsMapper = (FakeDrugsMapper) service.drugsMapper;
        FakeSeekMapper seekMapper = (FakeSeekMapper) service.seekMapper;
        Patient patient = new Patient();
        patient.setId(3);
        patient.setDrugsids("1@2");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(1, drugsMapper.updateCount);
        assertEquals(1, seekMapper.updateCount);
        assertEquals(42, seekMapper.updatedSeek.getId());
        assertEquals("1@2", seekMapper.updatedSeek.getDrugs());
        assertEquals(new BigDecimal("6.00"), seekMapper.updatedSeek.getPrice());
    }

    private PatientServiceImpl serviceWithDrug(int stock) {
        PatientServiceImpl service = new PatientServiceImpl();
        service.patientMapper = new FakePatientMapper();
        service.loginMapper = new FakeLoginMapper();
        service.doctorMapper = new FakeDoctorMapper();
        service.illnessMapper = new FakeIllnessMapper();
        service.drugsMapper = new FakeDrugsMapper(stock);
        service.seekMapper = new FakeSeekMapper();
        return service;
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private final Drugs drug;
        private int updateCount;

        private FakeDrugsMapper(int stock) {
            drug = new Drugs();
            drug.setId(1);
            drug.setName("Aspirin");
            drug.setNumber(stock);
            drug.setPrice(new BigDecimal("3.00"));
        }

        @Override
        public Drugs selectByPrimaryKey(Integer id) {
            return drug;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateCount++;
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

    private static class FakeSeekMapper implements SeekMapper {
        private int updateCount;
        private Seek updatedSeek;

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            Seek seek = new Seek();
            seek.setId(42);
            seek.setPatientid(patientid);
            return seek;
        }

        @Override
        public Integer updateDrugs(Seek seek) {
            updateCount++;
            updatedSeek = seek;
            return 1;
        }

        @Override
        public Integer insert(Seek seek) {
            return 0;
        }
    }

    private static class FakePatientMapper implements PatientMapper {
        private int updateCount;

        @Override
        public int updateByPrimaryKeySelective(Patient record) {
            updateCount++;
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

    private static class FakeLoginMapper implements LoginMapper {
        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int insert(Login record) {
            return 0;
        }

        @Override
        public int insertSelective(Login record) {
            return 0;
        }

        @Override
        public Login selectByPrimaryKey(Integer id) {
            return null;
        }

        @Override
        public int updateByPrimaryKeySelective(Login record) {
            return 0;
        }

        @Override
        public int updateByPrimaryKey(Login record) {
            return 0;
        }

        @Override
        public List<Login> findAllAdmin(String username) {
            return Collections.emptyList();
        }

        @Override
        public int insertAdmin(Login login) {
            return 0;
        }

        @Override
        public Login findByUsername(String username) {
            return null;
        }
    }

    private static class FakeDoctorMapper implements DoctorMapper {
        @Override
        public List<Doctor> getAll(String name, String certId) {
            return Collections.emptyList();
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int insert(Doctor record) {
            return 0;
        }

        @Override
        public int insertSelective(Doctor record) {
            return 0;
        }

        @Override
        public Doctor selectByPrimaryKey(Integer id) {
            return null;
        }

        @Override
        public int updateByPrimaryKeySelective(Doctor record) {
            return 0;
        }

        @Override
        public int updateByPrimaryKey(Doctor record) {
            return 0;
        }

        @Override
        public Doctor getDoctorByCertId(String certId) {
            return null;
        }

        @Override
        public List<Doctor> getDoctorByDepartment(String department) {
            return Collections.emptyList();
        }

        @Override
        public Doctor getDoctorByLoginId(Integer loginid) {
            return null;
        }

        @Override
        public List<Doctor> getDoctorByName(String name) {
            return Collections.emptyList();
        }
    }

    private static class FakeIllnessMapper implements IllnessMapper {
        @Override
        public Integer insert(Illness illness) {
            return 0;
        }

        @Override
        public Integer deleteById(Integer id) {
            return 0;
        }

        @Override
        public Integer updateById(Illness illness) {
            return 0;
        }

        @Override
        public List<Illness> selectAll() {
            return Collections.emptyList();
        }

        @Override
        public Illness getIllness(Integer id) {
            return null;
        }

        @Override
        public List<Illness> getIllnessByName(String name) {
            return Collections.emptyList();
        }
    }
}
