package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {

    private final LoginInterceptor loginInterceptor = new LoginInterceptor();

    @Test
    public void anonymousRequestRedirectsWithoutCreatingSession() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = loginInterceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals("/hospital/login", response.getRedirectedUrl());
        assertNull(request.getSession(false));
    }

    @Test
    public void requestWithLoginSessionIsAllowed() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", new Login());

        boolean allowed = loginInterceptor.preHandle(request, response, new Object());

        assertTrue(allowed);
        assertNull(response.getRedirectedUrl());
    }
}
