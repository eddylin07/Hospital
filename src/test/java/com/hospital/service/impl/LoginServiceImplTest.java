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

public class LoginServiceImplTest {

    @Test
    public void publicRegistrationWithBlankCertIdDoesNotCreateAdmin() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper(null);
        service.loginMapper = loginMapper;
        service.doctorMapper = new FakeDoctorMapper(null);
        service.patientMapper = new FakePatientMapper(null);
        Login login = new Login();
        login.setUsername("new-user");
        login.setCertId(" ");

        String message = service.regist(login);

        assertEquals("证件号不能为空", message);
        assertEquals(0, loginMapper.insertCalls);
        assertEquals(null, login.getRole());
    }

    @Test
    public void existingUsernameDoesNotLinkDoctorToAnotherLogin() {
        Login existing = new Login();
        existing.setId(99);
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper(existing);
        Doctor doctor = new Doctor();
        doctor.setId(7);
        doctor.setCertId("doctor-cert");
        FakeDoctorMapper doctorMapper = new FakeDoctorMapper(doctor);
        service.loginMapper = loginMapper;
        service.doctorMapper = doctorMapper;
        service.patientMapper = new FakePatientMapper(null);
        Login login = new Login();
        login.setUsername("taken");
        login.setCertId("doctor-cert");

        String message = service.regist(login);

        assertEquals("该用户名已被注册", message);
        assertEquals(0, loginMapper.insertCalls);
        assertEquals(0, doctorMapper.updateCalls);
    }

    private static class FakeLoginMapper implements LoginMapper {
        private final Login existingLogin;
        private int insertCalls;

        private FakeLoginMapper(Login existingLogin) {
            this.existingLogin = existingLogin;
        }

        @Override
        public Login findByUsername(String username) {
            return existingLogin;
        }

        @Override
        public int insert(Login record) {
            insertCalls++;
            return 1;
        }

        @Override public int deleteByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override public int insertSelective(Login record) { throw new UnsupportedOperationException(); }
        @Override public Login selectByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override public int updateByPrimaryKeySelective(Login record) { throw new UnsupportedOperationException(); }
        @Override public int updateByPrimaryKey(Login record) { throw new UnsupportedOperationException(); }
        @Override public List<Login> findAllAdmin(String username) { throw new UnsupportedOperationException(); }
        @Override public int insertAdmin(Login login) { throw new UnsupportedOperationException(); }
    }

    private static class FakeDoctorMapper implements DoctorMapper {
        private final Doctor doctor;
        private int updateCalls;

        private FakeDoctorMapper(Doctor doctor) {
            this.doctor = doctor;
        }

        @Override
        public Doctor getDoctorByCertId(String certId) {
            return doctor;
        }

        @Override
        public int updateByPrimaryKeySelective(Doctor record) {
            updateCalls++;
            return 1;
        }

        @Override public List<Doctor> getAll(String name, String certId) { throw new UnsupportedOperationException(); }
        @Override public int deleteByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override public int insert(Doctor record) { throw new UnsupportedOperationException(); }
        @Override public int insertSelective(Doctor record) { throw new UnsupportedOperationException(); }
        @Override public Doctor selectByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override public int updateByPrimaryKey(Doctor record) { throw new UnsupportedOperationException(); }
        @Override public List<Doctor> getDoctorByDepartment(String department) { throw new UnsupportedOperationException(); }
        @Override public Doctor getDoctorByLoginId(Integer loginid) { throw new UnsupportedOperationException(); }
        @Override public List<Doctor> getDoctorByName(String name) { throw new UnsupportedOperationException(); }
    }

    private static class FakePatientMapper implements PatientMapper {
        private final Patient patient;

        private FakePatientMapper(Patient patient) {
            this.patient = patient;
        }

        @Override
        public Patient findPatientByCertId(String certId) {
            return patient;
        }

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
}
