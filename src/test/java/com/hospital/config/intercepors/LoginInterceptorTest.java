package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {

    @Test
    public void redirectsAnonymousRequestsToLogin() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/patientManage");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void rejectsPatientFromAdminRoutes() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = requestWithRole("/admin/patientManage", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    @Test
    public void rejectsPatientFromDoctorWorkflowRoutes() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = requestWithRole("/doctor/seekinfo", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    @Test
    public void allowsAuthenticatedDoctorDepartmentLookupForPatients() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = requestWithRole("/doctor/cardiology", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    private MockHttpServletRequest requestWithRole(String path, int role) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", path);
        Login login = new Login();
        login.setId(10);
        login.setRole(role);
        request.getSession().setAttribute("login", login);
        return request;
    }
}

