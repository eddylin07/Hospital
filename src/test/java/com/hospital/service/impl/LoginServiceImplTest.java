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
import static org.junit.Assert.assertNull;

public class LoginServiceImplTest {

    @Test
    public void publicRegistrationCannotCreateAdminWithBlankCertId() {
        LoginMapperStub loginMapper = new LoginMapperStub(null);
        LoginServiceImpl service = service(loginMapper, new DoctorMapperStub(null), new PatientMapperStub(null));
        Login login = new Login();
        login.setUsername("attacker");
        login.setCertId(" ");

        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertFalse(loginMapper.insertCalled);
        assertNull(login.getRole());
    }

    @Test
    public void duplicateUsernameIsRejectedBeforeBindingDoctorRecord() {
        Login existing = new Login();
        existing.setId(99);
        LoginMapperStub loginMapper = new LoginMapperStub(existing);
        DoctorMapperStub doctorMapper = new DoctorMapperStub(new Doctor());
        LoginServiceImpl service = service(loginMapper, doctorMapper, new PatientMapperStub(null));
        Login login = new Login();
        login.setUsername("existing");
        login.setCertId("doctor-cert");

        String message = service.regist(login);

        assertEquals("该用户名已被注册", message);
        assertFalse(loginMapper.insertCalled);
        assertFalse(doctorMapper.updateCalled);
    }

    private LoginServiceImpl service(LoginMapper loginMapper, DoctorMapper doctorMapper, PatientMapper patientMapper) {
        LoginServiceImpl service = new LoginServiceImpl();
        service.loginMapper = loginMapper;
        service.doctorMapper = doctorMapper;
        service.patientMapper = patientMapper;
        return service;
    }

    private static class LoginMapperStub implements LoginMapper {
        private final Login foundByUsername;
        private boolean insertCalled;

        private LoginMapperStub(Login foundByUsername) {
            this.foundByUsername = foundByUsername;
        }

        @Override public Login findByUsername(String username) { return foundByUsername; }
        @Override public int insert(Login record) { insertCalled = true; return 1; }
        @Override public int deleteByPrimaryKey(Integer id) { return 0; }
        @Override public int insertSelective(Login record) { return 0; }
        @Override public Login selectByPrimaryKey(Integer id) { return null; }
        @Override public int updateByPrimaryKeySelective(Login record) { return 0; }
        @Override public int updateByPrimaryKey(Login record) { return 0; }
        @Override public List<Login> findAllAdmin(String username) { return null; }
        @Override public int insertAdmin(Login login) { return 0; }
    }

    private static class DoctorMapperStub implements DoctorMapper {
        private final Doctor doctor;
        private boolean updateCalled;

        private DoctorMapperStub(Doctor doctor) {
            this.doctor = doctor;
        }

        @Override public Doctor getDoctorByCertId(String certId) { return doctor; }
        @Override public int updateByPrimaryKeySelective(Doctor record) { updateCalled = true; return 1; }
        @Override public List<Doctor> getAll(String name, String certId) { return null; }
        @Override public int deleteByPrimaryKey(Integer id) { return 0; }
        @Override public int insert(Doctor record) { return 0; }
        @Override public int insertSelective(Doctor record) { return 0; }
        @Override public Doctor selectByPrimaryKey(Integer id) { return null; }
        @Override public int updateByPrimaryKey(Doctor record) { return 0; }
        @Override public List<Doctor> getDoctorByDepartment(String department) { return null; }
        @Override public Doctor getDoctorByLoginId(Integer loginid) { return null; }
        @Override public List<Doctor> getDoctorByName(String name) { return null; }
    }

    private static class PatientMapperStub implements PatientMapper {
        private final Patient patient;

        private PatientMapperStub(Patient patient) {
            this.patient = patient;
        }

        @Override public Patient findPatientByCertId(String certId) { return patient; }
        @Override public List<Patient> findAll(String name, String certId) { return null; }
        @Override public int deleteByPrimaryKey(Integer id) { return 0; }
        @Override public int insert(Patient record) { return 0; }
        @Override public int insertSelective(Patient record) { return 0; }
        @Override public Patient selectByPrimaryKey(Integer id) { return null; }
        @Override public int updateByPrimaryKeySelective(Patient record) { return 0; }
        @Override public int updateByPrimaryKey(Patient record) { return 0; }
        @Override public Patient findPatientByLoginId(Integer loginid) { return null; }
        @Override public List<Patient> getPatientByName(String name) { return null; }
    }
}
