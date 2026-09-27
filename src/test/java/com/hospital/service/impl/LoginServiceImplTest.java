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
import static org.junit.Assert.assertFalse;

public class LoginServiceImplTest {
    @Test
    public void publicRegistrationWithBlankCertIdDoesNotCreateAdmin() {
        LoginServiceImpl service = new LoginServiceImpl();
        CapturingLoginMapper loginMapper = new CapturingLoginMapper();
        service.loginMapper = loginMapper;
        service.doctorMapper = new EmptyDoctorMapper();
        service.patientMapper = new EmptyPatientMapper();

        Login login = new Login();
        login.setUsername("attacker");
        login.setPassword("pw");
        login.setCertId(" ");

        assertEquals("该证件信息未入库，不能注册该医生或者患者", service.regist(login));
        assertFalse(loginMapper.insertCalled);
    }

    private static class CapturingLoginMapper implements LoginMapper {
        boolean insertCalled;

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int insert(Login record) {
            insertCalled = true;
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
            return null;
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

    private static class EmptyDoctorMapper implements DoctorMapper {
        @Override
        public List<Doctor> getAll(String name, String certId) {
            return null;
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
            return null;
        }

        @Override
        public Doctor getDoctorByLoginId(Integer loginid) {
            return null;
        }

        @Override
        public List<Doctor> getDoctorByName(String name) {
            return null;
        }
    }

    private static class EmptyPatientMapper implements PatientMapper {
        @Override
        public List<Patient> findAll(String name, String certId) {
            return null;
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
            return null;
        }
    }
}
