package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginInterceptorTest {
    private LoginInterceptor interceptor;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Before
    public void setUp() {
        interceptor = new LoginInterceptor();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void preHandleRedirectsUnauthenticatedRequestsWithoutCreatingSession() throws Exception {
        when(request.getSession(false)).thenReturn(null);

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        verify(request).getSession(false);
        verify(response).sendRedirect("/hospital/login");
    }

    @Test
    public void preHandleAllowsRequestsWithLoginSession() throws Exception {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("login")).thenReturn(new Login());

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertTrue(allowed);
        verify(response, never()).sendRedirect(anyString());
    }
}
