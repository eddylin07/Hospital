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
    public void rejectsMissingSessionWithoutCreatingOne() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));

        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void rejectsRoleMismatchForAdminRoutes() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        request.getSession().setAttribute("login", login(9, 3));
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));

        assertEquals(403, response.getStatus());
    }

    @Test
    public void allowsDoctorWorkflowForDoctors() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/doctor/seekMedicalAdvice");
        request.getSession().setAttribute("login", login(5, 2));
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    private Login login(Integer id, Integer role) {
        Login login = new Login();
        login.setId(id);
        login.setRole(role);
        return login;
    }
}
