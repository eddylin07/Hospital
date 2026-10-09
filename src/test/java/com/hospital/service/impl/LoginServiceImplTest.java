package com.hospital.service.impl;

import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Doctor;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LoginServiceImplTest {
    @Test
    public void failedLoginClearsCallerSuppliedIdentity() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        Login stored = new Login();
        stored.setId(1);
        stored.setUsername("alice");
        stored.setPassword("correct");
        stored.setRole(1);
        loginMapper.byUsername = stored;
        service.loginMapper = loginMapper;

        Login attempt = new Login();
        attempt.setId(99);
        attempt.setUsername("alice");
        attempt.setPassword("wrong");
        attempt.setRole(1);

        assertEquals("密码错误", service.login(attempt));
        assertNull(attempt.getId());
        assertNull(attempt.getRole());
    }

    @Test
    public void publicRegistrationDoesNotCreateAdminForBlankCertId() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        service.loginMapper = loginMapper;
        service.doctorMapper = new FakeDoctorMapper();
        service.patientMapper = new FakePatientMapper();

        Login registration = new Login();
        registration.setUsername("new-admin");
        registration.setPassword("secret");
        registration.setCertId(" ");

        assertEquals("该证件信息未入库，不能注册该医生或者患者", service.regist(registration));
        assertEquals(0, loginMapper.insertCount);
        assertNull(registration.getRole());
    }

    private static class FakeLoginMapper implements LoginMapper {
        private Login byUsername;
        private int insertCount;

        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        public int insert(Login record) {
            insertCount++;
            return 1;
        }

        public int insertSelective(Login record) {
            return 0;
        }

        public Login selectByPrimaryKey(Integer id) {
            return null;
        }

        public int updateByPrimaryKeySelective(Login record) {
            return 0;
        }

        public int updateByPrimaryKey(Login record) {
            return 0;
        }

        public List<Login> findAllAdmin(String username) {
            return null;
        }

        public int insertAdmin(Login login) {
            return 0;
        }

        public Login findByUsername(String username) {
            return byUsername;
        }
    }

    private static class FakeDoctorMapper implements DoctorMapper {
        public List<Doctor> getAll(String name, String certId) {
            return null;
        }

        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        public int insert(Doctor record) {
            return 0;
        }

        public int insertSelective(Doctor record) {
            return 0;
        }

        public Doctor selectByPrimaryKey(Integer id) {
            return null;
        }

        public int updateByPrimaryKeySelective(Doctor record) {
            return 0;
        }

        public int updateByPrimaryKey(Doctor record) {
            return 0;
        }

        public Doctor getDoctorByCertId(String certId) {
            return null;
        }

        public List<Doctor> getDoctorByDepartment(String department) {
            return null;
        }

        public Doctor getDoctorByLoginId(Integer loginid) {
            return null;
        }

        public List<Doctor> getDoctorByName(String name) {
            return null;
        }
    }

    private static class FakePatientMapper implements PatientMapper {
        public List<Patient> findAll(String name, String certId) {
            return null;
        }

        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        public int insert(Patient record) {
            return 0;
        }

        public int insertSelective(Patient record) {
            return 0;
        }

        public Patient selectByPrimaryKey(Integer id) {
            return null;
        }

        public int updateByPrimaryKeySelective(Patient record) {
            return 0;
        }

        public int updateByPrimaryKey(Patient record) {
            return 0;
        }

        public Patient findPatientByCertId(String certId) {
            return null;
        }

        public Patient findPatientByLoginId(Integer loginid) {
            return null;
        }

        public List<Patient> getPatientByName(String name) {
            return null;
        }
    }
}
