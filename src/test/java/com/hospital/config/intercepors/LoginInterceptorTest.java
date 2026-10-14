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
    public void rejectsMissingAuthenticatedIdentity() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        request.getSession().setAttribute("login", new Login());
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void rejectsPatientAccessingAdminRoute() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        request.getSession().setAttribute("login", login(3));
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals(403, response.getStatus());
    }

    @Test
    public void allowsAuthenticatedDepartmentLookupForNonDoctorRoutes() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/doctor/cardiology");
        request.getSession().setAttribute("login", login(3));
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void rejectsNonDoctorWorkflowRoute() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/doctor/drug");
        request.getSession().setAttribute("login", login(3));
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals(403, response.getStatus());
    }

    private Login login(int role) {
        Login login = new Login();
        login.setId(1);
        login.setRole(role);
        return login;
    }
}
