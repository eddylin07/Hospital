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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class LoginServiceImplTest {

    @Test
    public void failedLoginClearsAttackerSuppliedIdentity() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        Login storedLogin = new Login();
        storedLogin.setId(1);
        storedLogin.setUsername("admin");
        storedLogin.setPassword("correct-password");
        storedLogin.setRole(1);
        loginMapper.foundByUsername = storedLogin;
        service.loginMapper = loginMapper;

        Login attempt = new Login();
        attempt.setUsername("admin");
        attempt.setPassword("wrong-password");
        attempt.setId(1);
        attempt.setRole(1);

        assertEquals("密码错误", service.login(attempt));
        assertNull(attempt.getId());
        assertNull(attempt.getRole());
    }

    @Test
    public void publicRegistrationWithBlankCertIdDoesNotCreateAdmin() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        service.loginMapper = loginMapper;
        service.doctorMapper = new FakeDoctorMapper();
        service.patientMapper = new FakePatientMapper();

        Login login = new Login();
        login.setUsername("new-admin");
        login.setPassword("password");
        login.setCertId(" ");

        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertEquals(0, loginMapper.insertCalls);
        assertEquals(0, loginMapper.insertAdminCalls);
        assertNotEquals(Integer.valueOf(1), login.getRole());
    }

    @Test
    public void registrationRejectsDuplicateUsernameBeforeBindingDoctor() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        Login existingLogin = new Login();
        existingLogin.setId(99);
        existingLogin.setUsername("taken");
        loginMapper.foundByUsername = existingLogin;
        FakeDoctorMapper doctorMapper = new FakeDoctorMapper();
        Doctor doctor = new Doctor();
        doctor.setId(3);
        doctor.setCertId("doctor-cert");
        doctorMapper.foundByCertId = doctor;
        service.loginMapper = loginMapper;
        service.doctorMapper = doctorMapper;
        service.patientMapper = new FakePatientMapper();

        Login login = new Login();
        login.setUsername("taken");
        login.setPassword("password");
        login.setCertId("doctor-cert");

        String message = service.regist(login);

        assertEquals("该用户名已被注册", message);
        assertEquals(0, loginMapper.insertCalls);
        assertEquals(0, doctorMapper.updateSelectiveCalls);
    }

    private static class FakeLoginMapper implements LoginMapper {
        Login foundByUsername;
        int insertCalls;
        int insertAdminCalls;

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int insert(Login record) {
            insertCalls++;
            Login saved = new Login();
            saved.setId(44);
            saved.setUsername(record.getUsername());
            saved.setRole(record.getRole());
            foundByUsername = saved;
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
            insertAdminCalls++;
            return 1;
        }

        @Override
        public Login findByUsername(String username) {
            return foundByUsername;
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

    private static class FakeDoctorMapper implements DoctorMapper {
        Doctor foundByCertId;
        int updateSelectiveCalls;

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
            updateSelectiveCalls++;
            return 1;
        }

        @Override
        public int updateByPrimaryKey(Doctor record) {
            return 0;
        }

        @Override
        public Doctor getDoctorByCertId(String certId) {
            return foundByCertId;
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
}
