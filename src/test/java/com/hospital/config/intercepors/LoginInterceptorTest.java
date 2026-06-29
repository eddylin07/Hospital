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
    public void unauthenticatedRequestRedirectsToLogin() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/doctorManage");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void patientCannotAccessAdminUrls() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/doctorManage");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Login login = new Login();
        login.setId(3);
        login.setRole(3);
        request.getSession().setAttribute("login", login);

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    @Test
    public void adminCanAccessAdminUrls() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/doctorManage");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Login login = new Login();
        login.setId(1);
        login.setRole(1);
        request.getSession().setAttribute("login", login);

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void patientCanUseDoctorDepartmentLookup() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/doctor/内科");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Login login = new Login();
        login.setId(3);
        login.setRole(3);
        request.getSession().setAttribute("login", login);

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }
}
