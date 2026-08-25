package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LoginInterceptorTest {

    @Test
    public void rejectsForgedLoginWithoutServerIdentity() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        request.setRequestURI("/admin/adminManage");
        MockHttpServletResponse response = new MockHttpServletResponse();

        Login login = new Login();
        login.setUsername("attacker");
        request.getSession().setAttribute("login", login);

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void rejectsPatientAccessToAdminRoutes() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        request.setRequestURI("/admin/adminManage");
        MockHttpServletResponse response = new MockHttpServletResponse();

        Login login = new Login();
        login.setId(5);
        login.setRole(3);
        request.getSession().setAttribute("login", login);

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(403, response.getStatus());
    }
}
