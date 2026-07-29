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

import static org.junit.Assert.*;

public class LoginServiceImplTest {
    @Test
    public void failedLoginClearsClientSuppliedIdentity() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        Login saved = new Login();
        saved.setId(7);
        saved.setRole(1);
        saved.setUsername("admin");
        saved.setPassword("right");
        loginMapper.savedLogin = saved;
        service.loginMapper = loginMapper;

        Login attempt = new Login();
        attempt.setUsername("admin");
        attempt.setPassword("wrong");
        attempt.setId(1);
        attempt.setRole(1);

        assertEquals("密码错误", service.login(attempt));
        assertNull(attempt.getId());
        assertNull(attempt.getRole());
    }

    @Test
    public void publicRegistrationWithBlankCertificateDoesNotCreateAdmin() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        service.loginMapper = loginMapper;
        service.doctorMapper = new FakeDoctorMapper();
        service.patientMapper = new FakePatientMapper();

        Login request = new Login();
        request.setUsername("new-admin");
        request.setPassword("pw");
        request.setCertId("");

        assertEquals("该证件信息未入库，不能注册该医生或者患者", service.regist(request));
        assertEquals(0, loginMapper.insertCount);
        assertNull(request.getRole());
    }

    private static class FakeLoginMapper implements LoginMapper {
        private Login savedLogin;
        private int insertCount;

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int insert(Login record) {
            insertCount++;
            return 1;
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
            return savedLogin != null && savedLogin.getUsername().equals(username) ? savedLogin : null;
        }
    }

    private static class FakeDoctorMapper implements DoctorMapper {
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
        public List<Doctor> getAll(String name, String certId) {
            return Collections.emptyList();
        }

        @Override
        public Doctor getDoctorByCertId(String certId) {
            return null;
        }

        @Override
        public List<Doctor> getDoctorByName(String name) {
            return Collections.emptyList();
        }

        @Override
        public Doctor getDoctorByLoginId(Integer loginid) {
            return null;
        }

        @Override
        public List<Doctor> getDoctorByDepartment(String department) {
            return Collections.emptyList();
        }
    }

    private static class FakePatientMapper implements PatientMapper {
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
            return 0;
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
}
