package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {

    private final LoginInterceptor interceptor = new LoginInterceptor();

    @Test
    public void redirectsRequestsWithoutLoginSession() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/patientManage");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));

        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void rejectsNonAdminUserFromAdminPath() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("DELETE", "/admin/patient/1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Login login = new Login();
        login.setRole(3);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login);
        request.setSession(session);

        assertFalse(interceptor.preHandle(request, response, new Object()));

        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    @Test
    public void allowsAdminUserOnAdminPath() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/patientManage");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Login login = new Login();
        login.setRole(1);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login);
        request.setSession(session);

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }
}
