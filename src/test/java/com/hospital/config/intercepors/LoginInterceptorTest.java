package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginInterceptorTest {
    @Test
    public void preHandleRedirectsUnauthenticatedRequestsToLogin() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("login")).thenReturn(null);

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        verify(response).sendRedirect("/hospital/login");
    }

    @Test
    public void preHandleAllowsRequestsWithLoginSession() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("login")).thenReturn(new Login());

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertTrue(allowed);
        verify(response, never()).sendRedirect("/hospital/login");
    }
}
