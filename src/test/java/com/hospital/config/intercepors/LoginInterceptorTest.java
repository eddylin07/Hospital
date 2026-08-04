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
    public void unauthenticatedRequestRedirectsToLogin() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/patientManage");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void patientCannotAccessAdminRoutes() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/patientManage");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", loginWithRole(3));

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    public void doctorWorkflowRequiresDoctorRole() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("PUT", "/doctor/drug");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", loginWithRole(3));

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    public void departmentLookupAllowsAnyAuthenticatedRole() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/doctor/internal");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", loginWithRole(3));

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    private Login loginWithRole(Integer role) {
        Login login = new Login();
        login.setId(1);
        login.setRole(role);
        return login;
    }
}
