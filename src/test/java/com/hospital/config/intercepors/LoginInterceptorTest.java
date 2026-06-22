package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class LoginInterceptorTest {

    @Test
    public void preHandleRedirectsAnonymousUsersToLogin() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/doctor/seekMedicalAdvice");
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void preHandleAllowsUsersWithLoginSession() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/doctor/seekMedicalAdvice");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", new Login());

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertTrue(allowed);
        assertNull(response.getRedirectedUrl());
    }
}
