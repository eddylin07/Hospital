package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class LoginInterceptorTest {
    @Test
    public void anonymousRequestRedirectsWithoutCreatingSession() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals("/hospital/login", response.getRedirectedUrl());
        assertNull(request.getSession(false));
    }

    @Test
    public void authenticatedRequestIsAllowed() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession(true).setAttribute("login", new Login());

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertTrue(allowed);
        assertNull(response.getRedirectedUrl());
    }
}
