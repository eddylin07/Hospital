package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {
    private final LoginInterceptor interceptor = new LoginInterceptor();

    @Test
    public void redirectsAnonymousUsersToLogin() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/patientManage");
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void rejectsNonAdminUsersFromAdminPaths() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("DELETE", "/admin/patient/1");
        request.getSession().setAttribute("login", loginWithRole(2));
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals(403, response.getStatus());
    }

    @Test
    public void allowsAdminUsersOnAdminPaths() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("DELETE", "/admin/patient/1");
        request.getSession().setAttribute("login", loginWithRole(1));
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void rejectsPatientsFromDoctorPaths() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("PUT", "/doctor/drug");
        request.getSession().setAttribute("login", loginWithRole(3));
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals(403, response.getStatus());
    }

    @Test
    public void allowsPatientsOnPatientPaths() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/patient/appointment");
        request.getSession().setAttribute("login", loginWithRole(3));
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    private Login loginWithRole(Integer role) {
        Login login = new Login();
        login.setRole(role);
        return login;
    }
}
