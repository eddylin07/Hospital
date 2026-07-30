package com.hospital.controller;

import com.hospital.config.intercepors.LoginInterceptor;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;

import static org.junit.Assert.*;

public class LoginControllerSecurityTest {
    @Test
    public void failedLoginDoesNotCreateAuthenticatedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new LoginService() {
            @Override
            public List<Login> findAllAdmin(String username) {
                return null;
            }

            @Override
            public String addAmin(Login login) {
                return null;
            }

            @Override
            public String updateAdmin(Login login) {
                return null;
            }

            @Override
            public String delAdmin(Integer id) {
                return null;
            }

            @Override
            public Login getAdmin(Integer id) {
                return null;
            }

            @Override
            public String login(Login login) {
                return "密码错误";
            }

            @Override
            public String regist(Login login) {
                return null;
            }
        };
        MockHttpSession session = new MockHttpSession();

        controller.login(new Login(), session);

        assertNull(session.getAttribute("login"));
    }

    @Test
    public void patientRoleCannotAccessAdminEndpoint() throws Exception {
        Login login = new Login();
        login.setId(1);
        login.setRole(3);
        MockHttpServletRequest request = new MockHttpServletRequest("DELETE", "/admin/patient/2");
        request.setRequestURI("/admin/patient/2");
        request.getSession().setAttribute("login", login);
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = new LoginInterceptor().preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals(403, response.getStatus());
    }

    @Test
    public void oneSegmentDoctorLookupRemainsAvailableToAuthenticatedPatient() throws Exception {
        Login login = new Login();
        login.setId(1);
        login.setRole(3);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/doctor/internal");
        request.setRequestURI("/doctor/internal");
        request.getSession().setAttribute("login", login);
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = new LoginInterceptor().preHandle(request, response, new Object());

        assertTrue(allowed);
        assertEquals(200, response.getStatus());
    }
}
