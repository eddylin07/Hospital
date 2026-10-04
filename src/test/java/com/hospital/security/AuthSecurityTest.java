package com.hospital.security;

import com.hospital.config.intercepors.LoginInterceptor;
import com.hospital.controller.LoginController;
import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Doctor;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.impl.LoginServiceImpl;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AuthSecurityTest {
    @Test
    public void failedLoginDoesNotPersistSuppliedRoleInSession() {
        LoginController controller = new LoginController();
        LoginServiceImpl loginService = new LoginServiceImpl();
        ReflectionTestUtils.setField(loginService, "loginMapper", new FakeLoginMapper(existingLogin("admin", "correct", 1)));
        ReflectionTestUtils.setField(controller, "loginService", loginService);

        Login request = new Login();
        request.setUsername("admin");
        request.setPassword("wrong");
        request.setId(1);
        request.setRole(1);
        MockHttpSession session = new MockHttpSession();

        controller.login(request, session);

        assertNull(request.getId());
        assertNull(request.getRole());
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void patientRoleCannotAccessAdminPaths() throws Exception {
        Login login = new Login();
        login.setId(5);
        login.setRole(3);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        request.getSession(true).setAttribute("login", login);
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = new LoginInterceptor().preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void publicRegistrationDoesNotCreateAdminForBlankCertId() {
        LoginServiceImpl loginService = new LoginServiceImpl();
        FakeLoginMapper loginMapper = new FakeLoginMapper(null);
        ReflectionTestUtils.setField(loginService, "loginMapper", loginMapper);
        ReflectionTestUtils.setField(loginService, "doctorMapper", new FakeDoctorMapper());
        ReflectionTestUtils.setField(loginService, "patientMapper", new FakePatientMapper());
        Login request = new Login();
        request.setUsername("new-admin");
        request.setPassword("pw");
        request.setCertId(" ");

        String message = loginService.regist(request);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertEquals(0, loginMapper.insertCalls);
        assertNull(request.getRole());
    }

    private static Login existingLogin(String username, String password, Integer role) {
        Login login = new Login();
        login.setId(10);
        login.setUsername(username);
        login.setPassword(password);
        login.setRole(role);
        return login;
    }

    private static class FakeLoginMapper implements LoginMapper {
        private final Login login;
        private int insertCalls;

        private FakeLoginMapper(Login login) {
            this.login = login;
        }

        @Override
        public Login findByUsername(String username) {
            return login != null && login.getUsername().equals(username) ? login : null;
        }

        @Override
        public int insert(Login record) {
            insertCalls++;
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
            return new ArrayList<>();
        }

        @Override
        public int insertAdmin(Login login) {
            return 0;
        }
    }

    private static class FakeDoctorMapper implements DoctorMapper {
        @Override
        public Doctor getDoctorByCertId(String certId) {
            return null;
        }

        @Override
        public List<Doctor> getAll(String name, String certId) {
            return new ArrayList<>();
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
        public List<Doctor> getDoctorByDepartment(String department) {
            return new ArrayList<>();
        }

        @Override
        public Doctor getDoctorByLoginId(Integer loginid) {
            return null;
        }

        @Override
        public List<Doctor> getDoctorByName(String name) {
            return new ArrayList<>();
        }
    }

    private static class FakePatientMapper implements PatientMapper {
        @Override
        public Patient findPatientByCertId(String certId) {
            return null;
        }

        @Override
        public List<Patient> findAll(String name, String certId) {
            return new ArrayList<>();
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
            return new ArrayList<>();
        }
    }
}
