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

public class LoginServiceImplTest {
    @Test
    public void publicRegistrationWithBlankCertIdDoesNotCreateAdmin() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        service.loginMapper = loginMapper;
        service.doctorMapper = new FakeDoctorMapper();
        service.patientMapper = new FakePatientMapper();
        Login login = new Login();
        login.setUsername("new-user");
        login.setCertId(" ");

        service.regist(login);

        assertEquals(0, loginMapper.insertCalls);
        assertEquals(0, loginMapper.insertAdminCalls);
    }

    private static class FakeLoginMapper implements LoginMapper {
        private int insertCalls;
        private int insertAdminCalls;

        @Override
        public int insert(Login record) {
            insertCalls++;
            return 1;
        }

        @Override
        public int insertAdmin(Login login) {
            insertAdminCalls++;
            return 1;
        }

        @Override public Login findByUsername(String username) { return null; }
        @Override public int deleteByPrimaryKey(Integer id) { return 0; }
        @Override public int insertSelective(Login record) { return 0; }
        @Override public Login selectByPrimaryKey(Integer id) { return null; }
        @Override public int updateByPrimaryKeySelective(Login record) { return 0; }
        @Override public int updateByPrimaryKey(Login record) { return 0; }
        @Override public List<Login> findAllAdmin(String username) { return Collections.emptyList(); }
    }

    private static class FakeDoctorMapper implements DoctorMapper {
        @Override public Doctor getDoctorByCertId(String certId) { return null; }
        @Override public List<Doctor> getAll(String name, String certId) { return Collections.emptyList(); }
        @Override public int deleteByPrimaryKey(Integer id) { return 0; }
        @Override public int insert(Doctor record) { return 0; }
        @Override public int insertSelective(Doctor record) { return 0; }
        @Override public Doctor selectByPrimaryKey(Integer id) { return null; }
        @Override public int updateByPrimaryKeySelective(Doctor record) { return 0; }
        @Override public int updateByPrimaryKey(Doctor record) { return 0; }
        @Override public List<Doctor> getDoctorByDepartment(String department) { return Collections.emptyList(); }
        @Override public Doctor getDoctorByLoginId(Integer loginid) { return null; }
        @Override public List<Doctor> getDoctorByName(String name) { return Collections.emptyList(); }
    }

    private static class FakePatientMapper implements PatientMapper {
        @Override public Patient findPatientByCertId(String certId) { return null; }
        @Override public List<Patient> findAll(String name, String certId) { return Collections.emptyList(); }
        @Override public int deleteByPrimaryKey(Integer id) { return 0; }
        @Override public int insert(Patient record) { return 0; }
        @Override public int insertSelective(Patient record) { return 0; }
        @Override public Patient selectByPrimaryKey(Integer id) { return null; }
        @Override public int updateByPrimaryKeySelective(Patient record) { return 0; }
        @Override public int updateByPrimaryKey(Patient record) { return 0; }
        @Override public Patient findPatientByLoginId(Integer loginid) { return null; }
        @Override public List<Patient> getPatientByName(String name) { return Collections.emptyList(); }
    }
}
