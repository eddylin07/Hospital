package com.hospital.service.impl;

import com.hospital.entity.Doctor;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class LoginServiceImplTest {

    @Test
    public void blankCertificateCannotCreatePublicAdmin() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        service.loginMapper = loginMapper;
        service.doctorMapper = new FakeDoctorMapper();
        service.patientMapper = new FakePatientMapper();
        Login login = new Login();
        login.setUsername("attacker");
        login.setCertId("");

        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertEquals(0, loginMapper.insertCount);
        assertNotEquals(Integer.valueOf(1), login.getRole());
    }

    @Test
    public void duplicateUsernameDoesNotBindDoctorRecord() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        Login existing = new Login();
        existing.setId(99);
        loginMapper.existingLogin = existing;
        FakeDoctorMapper doctorMapper = new FakeDoctorMapper();
        Doctor doctor = new Doctor();
        doctor.setId(7);
        doctorMapper.doctor = doctor;
        service.loginMapper = loginMapper;
        service.doctorMapper = doctorMapper;
        service.patientMapper = new FakePatientMapper();
        Login login = new Login();
        login.setUsername("used");
        login.setCertId("cert-1");

        String message = service.regist(login);

        assertEquals("该用户名已被注册", message);
        assertEquals(0, loginMapper.insertCount);
        assertEquals(0, doctorMapper.updateCount);
    }

    private static class FakeLoginMapper implements LoginMapper {
        private Login existingLogin;
        private int insertCount;

        @Override
        public Login findByUsername(String username) { return existingLogin; }
        @Override
        public int insert(Login record) { insertCount++; return 1; }
        @Override
        public int deleteByPrimaryKey(Integer id) { return 0; }
        @Override
        public int insertSelective(Login record) { return 0; }
        @Override
        public Login selectByPrimaryKey(Integer id) { return null; }
        @Override
        public int updateByPrimaryKeySelective(Login record) { return 0; }
        @Override
        public int updateByPrimaryKey(Login record) { return 0; }
        @Override
        public List<Login> findAllAdmin(String username) { return null; }
        @Override
        public int insertAdmin(Login login) { return 0; }
    }

    private static class FakeDoctorMapper implements DoctorMapper {
        private Doctor doctor;
        private int updateCount;

        @Override
        public Doctor getDoctorByCertId(String certId) { return doctor; }
        @Override
        public int updateByPrimaryKeySelective(Doctor record) { updateCount++; return 1; }
        @Override
        public List<Doctor> getAll(String name, String certId) { return null; }
        @Override
        public int deleteByPrimaryKey(Integer id) { return 0; }
        @Override
        public int insert(Doctor record) { return 0; }
        @Override
        public int insertSelective(Doctor record) { return 0; }
        @Override
        public Doctor selectByPrimaryKey(Integer id) { return null; }
        @Override
        public int updateByPrimaryKey(Doctor record) { return 0; }
        @Override
        public List<Doctor> getDoctorByDepartment(String department) { return null; }
        @Override
        public Doctor getDoctorByLoginId(Integer loginid) { return null; }
        @Override
        public List<Doctor> getDoctorByName(String name) { return null; }
    }

    private static class FakePatientMapper implements PatientMapper {
        @Override
        public Patient findPatientByCertId(String certId) { return null; }
        @Override
        public List<Patient> findAll(String name, String certId) { return null; }
        @Override
        public int deleteByPrimaryKey(Integer id) { return 0; }
        @Override
        public int insert(Patient record) { return 0; }
        @Override
        public int insertSelective(Patient record) { return 0; }
        @Override
        public Patient selectByPrimaryKey(Integer id) { return null; }
        @Override
        public int updateByPrimaryKeySelective(Patient record) { return 0; }
        @Override
        public int updateByPrimaryKey(Patient record) { return 0; }
        @Override
        public Patient findPatientByLoginId(Integer loginid) { return null; }
        @Override
        public List<Patient> getPatientByName(String name) { return null; }
    }
}
