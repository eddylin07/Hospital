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

    private final LoginInterceptor interceptor = new LoginInterceptor();

    @Test
    public void preHandleRedirectsAnonymousUsersToLogin() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean handled = interceptor.preHandle(request, response, new Object());

        assertFalse(handled);
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void preHandleAllowsRequestsWithLoginSession() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getSession().setAttribute("login", new Login());
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean handled = interceptor.preHandle(request, response, new Object());

        assertTrue(handled);
        assertNull(response.getRedirectedUrl());
    }
}
