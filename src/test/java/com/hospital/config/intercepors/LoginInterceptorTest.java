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
    public void rejectsForgedSessionFromFailedLogin() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/doctorManage");
        request.getSession().setAttribute("login", new Login());
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void rejectsPatientRoleFromAdminPath() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/doctorManage");
        request.getSession().setAttribute("login", login(7, 3));
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    public void allowsPatientRoleOnPatientPath() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/patient/appointment");
        request.getSession().setAttribute("login", login(7, 3));

        assertTrue(interceptor.preHandle(request, new MockHttpServletResponse(), new Object()));
    }

    @Test
    public void allowsAuthenticatedDoctorDepartmentLookupForAppointmentForms() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/doctor/内科");
        request.getSession().setAttribute("login", login(7, 3));

        assertTrue(interceptor.preHandle(request, new MockHttpServletResponse(), new Object()));
    }

    private Login login(Integer id, Integer role) {
        Login login = new Login();
        login.setId(id);
        login.setRole(role);
        return login;
    }
}
