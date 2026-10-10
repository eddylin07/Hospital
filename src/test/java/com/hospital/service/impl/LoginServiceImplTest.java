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
import static org.junit.Assert.assertNull;

public class LoginServiceImplTest {
    @Test
    public void failedPasswordClearsHydratedIdentity() {
        LoginServiceImpl service = serviceWith(new FakeLoginMapper(existingLogin()), new FakePatientMapper(), new FakeDoctorMapper());
        Login request = new Login();
        request.setUsername("admin");
        request.setPassword("wrong");
        request.setId(999);
        request.setRole(1);

        String message = service.login(request);

        assertEquals("密码错误", message);
        assertNull(request.getId());
        assertNull(request.getRole());
    }

    @Test
    public void blankCertIdCannotSelfRegisterAdmin() {
        FakeLoginMapper loginMapper = new FakeLoginMapper(null);
        LoginServiceImpl service = serviceWith(loginMapper, new FakePatientMapper(), new FakeDoctorMapper());
        Login request = new Login();
        request.setUsername("new-admin");
        request.setCertId(" ");

        String message = service.regist(request);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertEquals(0, loginMapper.insertCount);
    }

    private LoginServiceImpl serviceWith(LoginMapper loginMapper, PatientMapper patientMapper, DoctorMapper doctorMapper) {
        LoginServiceImpl service = new LoginServiceImpl();
        service.loginMapper = loginMapper;
        service.patientMapper = patientMapper;
        service.doctorMapper = doctorMapper;
        return service;
    }

    private Login existingLogin() {
        Login login = new Login();
        login.setId(1);
        login.setUsername("admin");
        login.setPassword("secret");
        login.setRole(1);
        return login;
    }

    private static class FakeLoginMapper implements LoginMapper {
        private final Login existingLogin;
        private int insertCount;

        FakeLoginMapper(Login existingLogin) {
            this.existingLogin = existingLogin;
        }

        @Override public Login findByUsername(String username) { return existingLogin; }
        @Override public int insert(Login record) { insertCount++; return 1; }
        @Override public int insertAdmin(Login login) { insertCount++; return 1; }
        @Override public int deleteByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override public int insertSelective(Login record) { throw new UnsupportedOperationException(); }
        @Override public Login selectByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override public int updateByPrimaryKeySelective(Login record) { throw new UnsupportedOperationException(); }
        @Override public int updateByPrimaryKey(Login record) { throw new UnsupportedOperationException(); }
        @Override public List<Login> findAllAdmin(String username) { throw new UnsupportedOperationException(); }
    }

    private static class FakePatientMapper implements PatientMapper {
        @Override public Patient findPatientByCertId(String certId) { return null; }
        @Override public List<Patient> findAll(String name, String certId) { throw new UnsupportedOperationException(); }
        @Override public int deleteByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override public int insert(Patient record) { throw new UnsupportedOperationException(); }
        @Override public int insertSelective(Patient record) { throw new UnsupportedOperationException(); }
        @Override public Patient selectByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override public int updateByPrimaryKeySelective(Patient record) { throw new UnsupportedOperationException(); }
        @Override public int updateByPrimaryKey(Patient record) { throw new UnsupportedOperationException(); }
        @Override public Patient findPatientByLoginId(Integer loginid) { throw new UnsupportedOperationException(); }
        @Override public List<Patient> getPatientByName(String name) { throw new UnsupportedOperationException(); }
    }

    private static class FakeDoctorMapper implements DoctorMapper {
        @Override public Doctor getDoctorByCertId(String certId) { return null; }
        @Override public List<Doctor> getAll(String name, String certId) { throw new UnsupportedOperationException(); }
        @Override public int deleteByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override public int insert(Doctor record) { throw new UnsupportedOperationException(); }
        @Override public int insertSelective(Doctor record) { throw new UnsupportedOperationException(); }
        @Override public Doctor selectByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override public int updateByPrimaryKeySelective(Doctor record) { throw new UnsupportedOperationException(); }
        @Override public int updateByPrimaryKey(Doctor record) { throw new UnsupportedOperationException(); }
        @Override public List<Doctor> getDoctorByDepartment(String department) { throw new UnsupportedOperationException(); }
        @Override public Doctor getDoctorByLoginId(Integer loginid) { throw new UnsupportedOperationException(); }
        @Override public List<Doctor> getDoctorByName(String name) { throw new UnsupportedOperationException(); }
    }
}
