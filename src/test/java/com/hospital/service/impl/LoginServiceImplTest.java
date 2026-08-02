package com.hospital.service.impl;

import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Doctor;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LoginServiceImplTest {
    @Test
    public void publicRegistrationWithBlankCertIdDoesNotCreateAdmin() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        service.loginMapper = loginMapper;
        service.doctorMapper = new FakeDoctorMapper();
        service.patientMapper = new FakePatientMapper();
        Login login = new Login();
        login.setUsername("attacker");
        login.setCertId(" ");

        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertFalse(loginMapper.insertCalled);
    }

    @Test
    public void duplicateUsernameIsRejectedBeforeLinkingDoctorAccount() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        loginMapper.existingUsername = true;
        service.loginMapper = loginMapper;
        service.doctorMapper = new FakeDoctorMapper();
        service.patientMapper = new FakePatientMapper();
        Login login = new Login();
        login.setUsername("taken");
        login.setCertId("doctor-cert");

        String message = service.regist(login);

        assertEquals("该用户名已被注册", message);
        assertFalse(loginMapper.insertCalled);
    }

    private static class FakeLoginMapper implements LoginMapper {
        private boolean existingUsername;
        private boolean insertCalled;

        @Override
        public Login findByUsername(String username) {
            if (!existingUsername) {
                return null;
            }
            Login login = new Login();
            login.setId(1);
            return login;
        }

        @Override
        public int insert(Login record) {
            insertCalled = true;
            return 1;
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            throw new UnsupportedOperationException();
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
            insertCalled = true;
            return 1;
        }
    }

    private static class FakeDoctorMapper implements DoctorMapper {
        @Override
        public Doctor getDoctorByCertId(String certId) {
            if ("doctor-cert".equals(certId)) {
                Doctor doctor = new Doctor();
                doctor.setId(1);
                return doctor;
            }
            return null;
        }

        @Override
        public int updateByPrimaryKeySelective(Doctor record) {
            return 1;
        }

        @Override
        public List<Doctor> getAll(String name, String certId) {
            throw new UnsupportedOperationException();
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
        public int updateByPrimaryKey(Doctor record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Doctor> getDoctorByDepartment(String department) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Doctor getDoctorByLoginId(Integer loginid) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Doctor> getDoctorByName(String name) {
            throw new UnsupportedOperationException();
        }
    }

    private static class FakePatientMapper implements PatientMapper {
        @Override
        public Patient findPatientByCertId(String certId) {
            return null;
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
        public int updateByPrimaryKeySelective(Patient record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int updateByPrimaryKey(Patient record) {
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
}
