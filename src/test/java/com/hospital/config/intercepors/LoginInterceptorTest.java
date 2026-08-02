package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginInterceptorTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    private final LoginInterceptor interceptor = new LoginInterceptor();

    @Test
    public void preHandleRedirectsAnonymousUsersToLoginPage() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("login")).thenReturn(null);

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        verify(response).sendRedirect("/hospital/login");
    }

    @Test
    public void preHandleAllowsAuthenticatedUsers() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("login")).thenReturn(new Login());

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertTrue(allowed);
        verify(response, never()).sendRedirect("/hospital/login");
    }
}
