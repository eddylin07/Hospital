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
    public void publicRegistrationCannotCreateAdminForBlankCertId() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper(false);
        service.loginMapper = loginMapper;
        service.doctorMapper = new FakeDoctorMapper(null);
        service.patientMapper = new FakePatientMapper(null);

        Login login = new Login();
        login.setUsername("new-admin");
        login.setCertId("");

        assertEquals("该证件信息未入库，不能注册该医生或者患者", service.regist(login));
        assertFalse(loginMapper.inserted);
    }

    @Test
    public void duplicateUsernameIsRejectedBeforeBindingDoctor() {
        Doctor doctor = new Doctor();
        doctor.setId(3);
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper(true);
        FakeDoctorMapper doctorMapper = new FakeDoctorMapper(doctor);
        service.loginMapper = loginMapper;
        service.doctorMapper = doctorMapper;
        service.patientMapper = new FakePatientMapper(null);

        Login login = new Login();
        login.setUsername("taken");
        login.setCertId("cert-1");

        assertEquals("该用户名已被注册", service.regist(login));
        assertFalse(loginMapper.inserted);
        assertFalse(doctorMapper.updated);
    }

    private static class FakeLoginMapper implements LoginMapper {
        private final boolean usernameExists;
        private boolean inserted;

        private FakeLoginMapper(boolean usernameExists) {
            this.usernameExists = usernameExists;
        }

        @Override
        public Login findByUsername(String username) {
            return usernameExists ? new Login() : null;
        }

        @Override
        public int insert(Login record) {
            inserted = true;
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
            return null;
        }

        @Override
        public int insertAdmin(Login login) {
            return 0;
        }
    }

    private static class FakeDoctorMapper implements DoctorMapper {
        private final Doctor doctor;
        private boolean updated;

        private FakeDoctorMapper(Doctor doctor) {
            this.doctor = doctor;
        }

        @Override
        public Doctor getDoctorByCertId(String certId) {
            return doctor;
        }

        @Override
        public int updateByPrimaryKeySelective(Doctor record) {
            updated = true;
            return 1;
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
        public int updateByPrimaryKey(Doctor record) {
            return 0;
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
            this.patient = patient;
        }

        @Override
        public Patient findPatientByCertId(String certId) {
            return patient;
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
        public Patient findPatientByLoginId(Integer loginid) {
            return null;
        }

        @Override
        public List<Patient> getPatientByName(String name) {
            return null;
        }
    }
}
