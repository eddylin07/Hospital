package com.hospital.service.impl;

import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Doctor;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class LoginServiceImplTest {

    @Test
    public void publicRegistrationDoesNotCreateAdminForBlankCertId() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper(null);
        service.loginMapper = loginMapper;
        service.doctorMapper = new FakeDoctorMapper(null);
        service.patientMapper = new FakePatientMapper(null);

        Login login = new Login();
        login.setUsername("new-admin");
        login.setPassword("secret");
        login.setCertId(" ");

        String message = service.regist(login);

        Assert.assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        Assert.assertEquals(0, loginMapper.insertCount);
        Assert.assertNull(login.getRole());
    }

    @Test
    public void duplicateUsernameIsRejectedBeforePatientBinding() {
        Login existing = new Login();
        existing.setId(99);
        existing.setUsername("existing");
        FakeLoginMapper loginMapper = new FakeLoginMapper(existing);
        FakePatientMapper patientMapper = new FakePatientMapper(new Patient());
        LoginServiceImpl service = new LoginServiceImpl();
        service.loginMapper = loginMapper;
        service.doctorMapper = new FakeDoctorMapper(null);
        service.patientMapper = patientMapper;

        Login login = new Login();
        login.setUsername("existing");
        login.setPassword("secret");
        login.setCertId("CERT-1");

        String message = service.regist(login);

        Assert.assertEquals("该用户名已被注册", message);
        Assert.assertEquals(0, loginMapper.insertCount);
        Assert.assertEquals(0, patientMapper.updateCount);
    }

    private static class FakeLoginMapper implements LoginMapper {
        private final Login existing;
        private int insertCount;

        private FakeLoginMapper(Login existing) {
            this.existing = existing;
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int insert(Login record) {
            insertCount++;
            return 1;
        }

        @Override
        public int insertSelective(Login record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Login selectByPrimaryKey(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int updateByPrimaryKeySelective(Login record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int updateByPrimaryKey(Login record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Login> findAllAdmin(String username) {
            return Collections.emptyList();
        }

        @Override
        public int insertAdmin(Login login) {
            insertCount++;
            return 1;
        }

        @Override
        public Login findByUsername(String username) {
            if (existing != null && existing.getUsername().equals(username)) {
                return existing;
            }
            return null;
        }
    }

    private static class FakeDoctorMapper implements DoctorMapper {
        private final Doctor doctor;

        private FakeDoctorMapper(Doctor doctor) {
            this.doctor = doctor;
        }

        @Override
        public List<Doctor> getAll(String name, String certId) {
            return Collections.emptyList();
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int insert(Doctor record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int insertSelective(Doctor record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Doctor selectByPrimaryKey(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int updateByPrimaryKeySelective(Doctor record) {
            return 1;
        }

        @Override
        public int updateByPrimaryKey(Doctor record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Doctor getDoctorByCertId(String certId) {
            return doctor;
        }

        @Override
        public List<Doctor> getDoctorByDepartment(String department) {
            return Collections.emptyList();
        }

        @Override
        public Doctor getDoctorByLoginId(Integer loginid) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Doctor> getDoctorByName(String name) {
            return Collections.emptyList();
        }
    }

    private static class FakePatientMapper implements PatientMapper {
        private final Patient patient;
        private int updateCount;

        private FakePatientMapper(Patient patient) {
            this.patient = patient;
        }

        @Override
        public List<Patient> findAll(String name, String certId) {
            return Collections.emptyList();
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
        public int updateByPrimaryKeySelective(Patient record) {
            updateCount++;
            return 1;
        }

        @Override
        public int updateByPrimaryKey(Patient record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Patient findPatientByCertId(String certId) {
            return patient;
        }

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Patient> getPatientByName(String name) {
            return Collections.emptyList();
        }
    }
}
