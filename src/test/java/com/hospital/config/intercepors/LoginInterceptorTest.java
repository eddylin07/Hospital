package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpServletResponse;

public class LoginInterceptorTest {

    private final LoginInterceptor interceptor = new LoginInterceptor();

    @Test
    public void rejectsMissingSessionLogin() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        MockHttpServletResponse response = new MockHttpServletResponse();

        Assert.assertFalse(interceptor.preHandle(request, response, new Object()));
        Assert.assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void rejectsUnhydratedSessionLogin() throws Exception {
        MockHttpServletRequest request = requestWithLogin("/admin/adminManage", null, null);
        MockHttpServletResponse response = new MockHttpServletResponse();

        Assert.assertFalse(interceptor.preHandle(request, response, new Object()));
        Assert.assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void rejectsPatientFromAdminRoutes() throws Exception {
        MockHttpServletRequest request = requestWithLogin("/admin/adminManage", 3, 11);
        MockHttpServletResponse response = new MockHttpServletResponse();

        Assert.assertFalse(interceptor.preHandle(request, response, new Object()));
        Assert.assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    @Test
    public void rejectsPatientFromDoctorWorkflowRoutes() throws Exception {
        MockHttpServletRequest request = requestWithLogin("/doctor/seekMedicalAdvice", 3, 11);
        MockHttpServletResponse response = new MockHttpServletResponse();

        Assert.assertFalse(interceptor.preHandle(request, response, new Object()));
        Assert.assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    @Test
    public void allowsAuthenticatedDepartmentLookupForNonDoctorRoles() throws Exception {
        MockHttpServletRequest request = requestWithLogin("/doctor/cardiology", 3, 11);
        MockHttpServletResponse response = new MockHttpServletResponse();

        Assert.assertTrue(interceptor.preHandle(request, response, new Object()));
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    public void rejectsDoctorFromPatientRoutes() throws Exception {
        MockHttpServletRequest request = requestWithLogin("/patient/appointment", 2, 22);
        MockHttpServletResponse response = new MockHttpServletResponse();

        Assert.assertFalse(interceptor.preHandle(request, response, new Object()));
        Assert.assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    private MockHttpServletRequest requestWithLogin(String path, Integer role, Integer id) {
        Login login = new Login();
        login.setRole(role);
        login.setId(id);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", path);
        request.setSession(session);
        return request;
    }
}
