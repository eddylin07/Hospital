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
        Login existing=new Login();
        existing.setId(7);
        existing.setUsername("doctor");
        existing.setPassword("right-password");
        existing.setRole(2);

        FakeLoginMapper loginMapper=new FakeLoginMapper(existing);
        LoginServiceImpl service=new LoginServiceImpl();
        service.loginMapper=loginMapper;

        Login attempt=new Login();
        attempt.setUsername("doctor");
        attempt.setPassword("wrong-password");
        attempt.setId(1);
        attempt.setRole(1);

        assertEquals("密码错误",service.login(attempt));
        assertNull(attempt.getId());
        assertNull(attempt.getRole());
    }

    @Test
    public void publicRegistrationWithBlankCertIdDoesNotCreateAdmin() {
        FakeLoginMapper loginMapper=new FakeLoginMapper(null);
        LoginServiceImpl service=new LoginServiceImpl();
        service.loginMapper=loginMapper;
        service.doctorMapper=new FakeDoctorMapper(null);
        service.patientMapper=new FakePatientMapper(null);

        Login login=new Login();
        login.setUsername("attacker");
        login.setPassword("pw");
        login.setCertId(" ");

        assertEquals("该证件信息未入库，不能注册该医生或者患者",service.regist(login));
        assertEquals(0,loginMapper.insertCount);
        assertNull(login.getRole());
    }

    private static class FakeLoginMapper implements LoginMapper {
        private final Login existing;
        private int insertCount;

        private FakeLoginMapper(Login existing) {
            this.existing=existing;
        }

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
            return null;
        }

        @Override
        public int insertAdmin(Login login) {
            return 0;
        }

        @Override
        public Login findByUsername(String username) {
            return existing!=null&&existing.getUsername().equals(username)?existing:null;
        }
    }

    private static class FakeDoctorMapper implements DoctorMapper {
        private final Doctor doctor;

        private FakeDoctorMapper(Doctor doctor) {
            this.doctor=doctor;
        }

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
            return doctor;
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

    private static class FakePatientMapper implements PatientMapper {
        private final Patient patient;

        private FakePatientMapper(Patient patient) {
            this.patient=patient;
        }

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
            return patient;
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
