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
    public void publicRegistrationWithBlankCertDoesNotCreateAdmin() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        FakeDoctorMapper doctorMapper = new FakeDoctorMapper();
        FakePatientMapper patientMapper = new FakePatientMapper();
        service.loginMapper = loginMapper;
        service.doctorMapper = doctorMapper;
        service.patientMapper = patientMapper;

        Login login = new Login();
        login.setUsername("new-admin");
        login.setCertId(" ");
        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertEquals(0, loginMapper.insertCount);
        assertNull(login.getRole());
    }

    @Test
    public void duplicateUsernameIsRejectedBeforePatientBinding() {
        LoginServiceImpl service = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper();
        loginMapper.existingUsername = "admin1";
        FakeDoctorMapper doctorMapper = new FakeDoctorMapper();
        FakePatientMapper patientMapper = new FakePatientMapper();
        patientMapper.patient = new Patient();
        patientMapper.patient.setId(5);
        patientMapper.patient.setCertId("patient-cert");
        service.loginMapper = loginMapper;
        service.doctorMapper = doctorMapper;
        service.patientMapper = patientMapper;

        Login login = new Login();
        login.setUsername("admin1");
        login.setCertId("patient-cert");
        String message = service.regist(login);

        assertEquals("该用户名已被注册", message);
        assertEquals(0, loginMapper.insertCount);
        assertEquals(0, patientMapper.updateCount);
        assertFalse(patientMapper.findByCertIdCalled);
        assertNull(patientMapper.patient.getLoginid());
    }

    private static class FakeLoginMapper implements LoginMapper {
        String existingUsername;
        int insertCount;
        private Login insertedLogin;

        @Override
        public Login findByUsername(String username) {
            if (existingUsername != null && existingUsername.equals(username)) {
                Login login = new Login();
                login.setId(99);
                login.setUsername(username);
                login.setRole(1);
                return login;
            }
            if (insertedLogin != null && insertedLogin.getUsername().equals(username)) {
                Login login = new Login();
                login.setId(101);
                login.setUsername(username);
                login.setRole(insertedLogin.getRole());
                return login;
            }
            return null;
        }

        @Override
        public int insert(Login record) {
            insertCount++;
            insertedLogin = record;
            return 1;
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int insertSelective(Login record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Login selectByPrimaryKey(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int updateByPrimaryKeySelective(Login record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int updateByPrimaryKey(Login record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Login> findAllAdmin(String username) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int insertAdmin(Login login) {
            throw new UnsupportedOperationException();
        }
    }

    private static class FakeDoctorMapper implements DoctorMapper {
        @Override
        public Doctor getDoctorByCertId(String certId) {
            return null;
        }

        @Override
        public List<Doctor> getAll(String name, String certId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int insert(Doctor record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int insertSelective(Doctor record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Doctor selectByPrimaryKey(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int updateByPrimaryKeySelective(Doctor record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int updateByPrimaryKey(Doctor record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Doctor> getDoctorByDepartment(String department) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Doctor getDoctorByLoginId(Integer loginid) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Doctor> getDoctorByName(String name) {
            throw new UnsupportedOperationException();
        }
    }

    private static class FakePatientMapper implements PatientMapper {
        Patient patient;
        int updateCount;
        boolean findByCertIdCalled;

        @Override
        public Patient findPatientByCertId(String certId) {
            findByCertIdCalled = true;
            return patient != null && certId != null && certId.equals(patient.getCertId()) ? patient : null;
        }

        @Override
        public int updateByPrimaryKeySelective(Patient record) {
            updateCount++;
            return 1;
        }

        @Override
        public List<Patient> findAll(String name, String certId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int deleteByPrimaryKey(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int insert(Patient record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int insertSelective(Patient record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Patient selectByPrimaryKey(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int updateByPrimaryKey(Patient record) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Patient> getPatientByName(String name) {
            throw new UnsupportedOperationException();
        }
    }
}
