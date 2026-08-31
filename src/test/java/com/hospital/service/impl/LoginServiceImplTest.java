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
        service.loginMapper = new FakeLoginMapper();

        Login login = new Login();
        login.setUsername("missing");
        login.setPassword("bad");
        login.setId(1);
        login.setRole(1);

        assertEquals("用户名不存在", service.login(login));
        assertNull(login.getId());
        assertNull(login.getRole());
    }

    @Test
    public void blankCertificateDoesNotCreateAdminFromPublicRegistration() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        service.loginMapper = loginMapper;
        service.doctorMapper = new FakeDoctorMapper();
        service.patientMapper = new FakePatientMapper();

        Login login = new Login();
        login.setUsername("attacker");
        login.setPassword("pw");
        login.setCertId(" ");

        assertEquals("该证件信息未入库，不能注册该医生或者患者", service.regist(login));
        assertEquals(0, loginMapper.insertCount);
        assertNull(login.getRole());
    }

    private static class FakeLoginMapper implements LoginMapper {
        int insertCount;

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
            insertCount++;
            return 1;
        }

        @Override
        public Login findByUsername(String username) {
            return null;
        }
    }

    private static class FakeDoctorMapper implements DoctorMapper {
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

    private static class FakePatientMapper implements PatientMapper {
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
