package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {
    @Test
    public void rejectsAdminPathForPatientRole() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = requestWithLogin("/admin/adminManage", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    public void preservesAuthenticatedDoctorDepartmentLookupForPatients() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = requestWithLogin("/doctor/内科", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
        assertEquals(200, response.getStatus());
    }

    @Test
    public void rejectsUnhydratedSessionLogin() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/patient/medicalhistory");
        request.getSession().setAttribute("login", new Login());
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    private MockHttpServletRequest requestWithLogin(String path, int role) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", path);
        Login login = new Login();
        login.setId(11);
        login.setRole(role);
        request.getSession().setAttribute("login", login);
        return request;
    }
}
