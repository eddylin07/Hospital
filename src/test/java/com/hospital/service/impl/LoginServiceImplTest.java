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
import static org.junit.Assert.assertNull;

public class LoginServiceImplTest {

    @Test
    public void blankCertificateRegistrationDoesNotCreateAdmin() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        service.loginMapper = loginMapper;
        service.doctorMapper = new FakeDoctorMapper();
        service.patientMapper = new FakePatientMapper();
        Login login = new Login();
        login.setUsername("public-user");
        login.setCertId(" ");

        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertNull(login.getRole());
        assertEquals(0, loginMapper.insertCount);
    }

    @Test
    public void usernameConflictDoesNotBindDoctorToExistingAccount() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        Login existing = new Login();
        existing.setId(99);
        loginMapper.existingByUsername = existing;
        FakeDoctorMapper doctorMapper = new FakeDoctorMapper();
        Doctor doctor = new Doctor();
        doctor.setCertId("doctor-cert");
        doctorMapper.doctorByCertId = doctor;
        service.loginMapper = loginMapper;
        service.doctorMapper = doctorMapper;
        service.patientMapper = new FakePatientMapper();
        Login login = new Login();
        login.setUsername("taken");
        login.setCertId("doctor-cert");

        String message = service.regist(login);

        assertEquals("该用户名已被注册", message);
        assertEquals(0, loginMapper.insertCount);
        assertEquals(0, doctorMapper.updateCount);
        assertNull(doctor.getLoginid());
    }

    private static class FakeLoginMapper implements LoginMapper {
        Login existingByUsername;
        int insertCount;

        @Override
        public Login findByUsername(String username) {
            return existingByUsername;
        }

        @Override
        public int insert(Login record) {
            insertCount++;
            return 1;
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
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
    }

    private static class FakeDoctorMapper implements DoctorMapper {
        Doctor doctorByCertId;
        int updateCount;

        @Override
        public Doctor getDoctorByCertId(String certId) {
            return doctorByCertId;
        }

        @Override
        public int updateByPrimaryKeySelective(Doctor record) {
            updateCount++;
            return 1;
        }

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
        public int updateByPrimaryKey(Doctor record) {
            return 0;
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

    private static class FakePatientMapper implements PatientMapper {
        @Override
        public Patient findPatientByCertId(String certId) {
            return null;
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
        public int updateByPrimaryKeySelective(Patient record) {
            return 0;
        }

        @Override
        public int updateByPrimaryKey(Patient record) {
            return 0;
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

