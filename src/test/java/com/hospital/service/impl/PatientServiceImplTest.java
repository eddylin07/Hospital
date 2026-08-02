package com.hospital.service.impl;

import com.hospital.common.CommonService;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.IllnessMapper;
import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Drugs;
import com.hospital.entity.Doctor;
import com.hospital.entity.Illness;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PatientServiceImplTest {
    @Test
    public void dispensingRejectsQuantityGreaterThanStockWithoutUpdatingAnything() {
        PatientServiceImpl service = serviceWithStock(1);
        FakeDrugsMapper drugsMapper = (FakeDrugsMapper) service.drugsMapper;
        FakePatientMapper patientMapper = (FakePatientMapper) service.patientMapper;
        FakeSeekMapper seekMapper = (FakeSeekMapper) service.seekMapper;
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("5@2");

        String message = service.seek(patient);

        assertEquals("对不起药品数量不足", message);
        assertEquals(0, drugsMapper.updateCalls);
        assertNull(patientMapper.updatedPatient);
        assertNull(seekMapper.updatedSeek);
    }

    @Test
    public void dispensingDeductsRequestedQuantityAndUpdatesSeekPrice() {
        PatientServiceImpl service = serviceWithStock(10);
        FakeDrugsMapper drugsMapper = (FakeDrugsMapper) service.drugsMapper;
        FakeSeekMapper seekMapper = (FakeSeekMapper) service.seekMapper;
        Patient patient = new Patient();
        patient.setId(7);
        patient.setDrugsids("5@2");

        String message = service.seek(patient);

        assertEquals(CommonService.upd_message_success, message);
        assertEquals(1, drugsMapper.updateCalls);
        assertEquals(8, drugsMapper.stock);
        assertEquals("5@2", seekMapper.updatedSeek.getDrugs());
        assertEquals(Integer.valueOf(7), seekMapper.updatedSeek.getPatientid());
        assertTrue(BigDecimal.valueOf(7).compareTo(seekMapper.updatedSeek.getPrice()) == 0);
    }

    private static PatientServiceImpl serviceWithStock(int stock) {
        PatientServiceImpl service = new PatientServiceImpl();
        service.drugsMapper = new FakeDrugsMapper(stock);
        service.patientMapper = new FakePatientMapper();
        service.seekMapper = new FakeSeekMapper();
        service.loginMapper = new UnusedLoginMapper();
        service.doctorMapper = new UnusedDoctorMapper();
        service.illnessMapper = new UnusedIllnessMapper();
        return service;
    }

    private static class FakeDrugsMapper implements DrugsMapper {
        private int stock;
        private int updateCalls;

        private FakeDrugsMapper(int stock) {
            this.stock = stock;
        }

        @Override
        public Drugs selectByPrimaryKey(Integer id) {
            Drugs drugs = new Drugs();
            drugs.setId(id);
            drugs.setNumber(stock);
            drugs.setPrice(new BigDecimal("3.50"));
            return drugs;
        }

        @Override
        public int updateNumber(Drugs drugs) {
            updateCalls++;
            if (stock < drugs.getNumber()) {
                return 0;
            }
            stock -= drugs.getNumber();
            return 1;
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
        private Patient updatedPatient;

        @Override
        public int updateByPrimaryKeySelective(Patient record) {
            updatedPatient = record;
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

    private static class FakeSeekMapper implements SeekMapper {
        private Seek updatedSeek;

        @Override
        public Integer updateDrugs(Seek seek) {
            updatedSeek = seek;
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

    private static class UnusedLoginMapper implements LoginMapper {
        @Override
        public int deleteByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override
        public int insert(Login record) { throw new UnsupportedOperationException(); }
        @Override
        public int insertSelective(Login record) { throw new UnsupportedOperationException(); }
        @Override
        public Login selectByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override
        public int updateByPrimaryKeySelective(Login record) { throw new UnsupportedOperationException(); }
        @Override
        public int updateByPrimaryKey(Login record) { throw new UnsupportedOperationException(); }
        @Override
        public List<Login> findAllAdmin(String username) { throw new UnsupportedOperationException(); }
        @Override
        public int insertAdmin(Login login) { throw new UnsupportedOperationException(); }
        @Override
        public Login findByUsername(String username) { throw new UnsupportedOperationException(); }
    }

    private static class UnusedDoctorMapper implements DoctorMapper {
        @Override
        public List<Doctor> getAll(String name, String certId) { throw new UnsupportedOperationException(); }
        @Override
        public int deleteByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override
        public int insert(Doctor record) { throw new UnsupportedOperationException(); }
        @Override
        public int insertSelective(Doctor record) { throw new UnsupportedOperationException(); }
        @Override
        public Doctor selectByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override
        public int updateByPrimaryKeySelective(Doctor record) { throw new UnsupportedOperationException(); }
        @Override
        public int updateByPrimaryKey(Doctor record) { throw new UnsupportedOperationException(); }
        @Override
        public Doctor getDoctorByCertId(String certId) { throw new UnsupportedOperationException(); }
        @Override
        public List<Doctor> getDoctorByDepartment(String department) { throw new UnsupportedOperationException(); }
        @Override
        public Doctor getDoctorByLoginId(Integer loginid) { throw new UnsupportedOperationException(); }
        @Override
        public List<Doctor> getDoctorByName(String name) { throw new UnsupportedOperationException(); }
    }

    private static class UnusedIllnessMapper implements IllnessMapper {
        @Override
        public Integer insert(Illness illness) { throw new UnsupportedOperationException(); }
        @Override
        public Integer deleteById(Integer id) { throw new UnsupportedOperationException(); }
        @Override
        public Integer updateById(Illness illness) { throw new UnsupportedOperationException(); }
        @Override
        public List<Illness> selectAll() { throw new UnsupportedOperationException(); }
        @Override
        public Illness getIllness(Integer id) { throw new UnsupportedOperationException(); }
        @Override
        public List<Illness> getIllnessByName(String name) { throw new UnsupportedOperationException(); }
    }
}
