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

    private final LoginInterceptor interceptor = new LoginInterceptor();

    @Test
    public void redirectsWhenSessionIdentityIsMissingResolvedRole() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Login login = new Login();
        login.setId(1);
        request.getSession().setAttribute("login", login);

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void deniesPatientAccessToAdminEndpoints() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", loginWithRole(3));

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    @Test
    public void allowsDoctorAccessToDoctorWorkflowEndpoints() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/doctor/seekMedicalAdvice");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", loginWithRole(2));

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void allowsAuthenticatedDoctorDepartmentLookupForPatients() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/doctor/cardiology");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", loginWithRole(3));

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    private Login loginWithRole(Integer role) {
        Login login = new Login();
        login.setId(10);
        login.setRole(role);
        return login;
    }
}
