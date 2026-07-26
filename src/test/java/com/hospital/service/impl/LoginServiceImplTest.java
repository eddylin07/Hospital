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
    public void blankCertIdCannotSelfRegisterAdmin() {
        LoginServiceImpl service=new LoginServiceImpl();
        FakeLoginMapper loginMapper=new FakeLoginMapper();
        service.loginMapper=loginMapper;
        service.doctorMapper=new EmptyDoctorMapper();
        service.patientMapper=new EmptyPatientMapper();
        Login login=new Login();
        login.setUsername("attacker");
        login.setPassword("pw");
        login.setCertId(" ");

        String message=service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者",message);
        assertFalse(loginMapper.inserted);
        assertNull(login.getRole());
    }

    private static class FakeLoginMapper implements LoginMapper {
        private boolean inserted;

        @Override
        public int insert(Login record) {
            inserted=true;
            return 1;
        }

        @Override public Login findByUsername(String username) { return null; }
        @Override public int deleteByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override public int insertSelective(Login record) { throw new UnsupportedOperationException(); }
        @Override public Login selectByPrimaryKey(Integer id) { throw new UnsupportedOperationException(); }
        @Override public int updateByPrimaryKeySelective(Login record) { throw new UnsupportedOperationException(); }
        @Override public int updateByPrimaryKey(Login record) { throw new UnsupportedOperationException(); }
        @Override public List<Login> findAllAdmin(String username) { throw new UnsupportedOperationException(); }
        @Override public int insertAdmin(Login login) { throw new UnsupportedOperationException(); }
    }

    private static class EmptyDoctorMapper implements DoctorMapper {
        @Override public Doctor getDoctorByCertId(String certId) { return null; }
        @Override public List<Doctor> getAll(String name,String certId) { throw new UnsupportedOperationException(); }
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

    private static class EmptyPatientMapper implements PatientMapper {
        @Override public Patient findPatientByCertId(String certId) { return null; }
        @Override public List<Patient> findAll(String name,String certId) { throw new UnsupportedOperationException(); }
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
