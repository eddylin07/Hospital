package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {

    @Test
    public void redirectsWhenThereIsNoSession() throws Exception {
        LoginInterceptor interceptor=new LoginInterceptor();
        MockHttpServletRequest request=new MockHttpServletRequest();
        MockHttpServletResponse response=new MockHttpServletResponse();

        boolean allowed=interceptor.preHandle(request,response,new Object());

        assertFalse(allowed);
        assertEquals("/hospital/login",response.getRedirectedUrl());
    }

    @Test
    public void redirectsMalformedLoginSession() throws Exception {
        LoginInterceptor interceptor=new LoginInterceptor();
        MockHttpServletRequest request=new MockHttpServletRequest();
        request.getSession().setAttribute("login",new Login());
        MockHttpServletResponse response=new MockHttpServletResponse();

        boolean allowed=interceptor.preHandle(request,response,new Object());

        assertFalse(allowed);
        assertEquals("/hospital/login",response.getRedirectedUrl());
    }

    @Test
    public void allowsAuthenticatedLoginSession() throws Exception {
        LoginInterceptor interceptor=new LoginInterceptor();
        MockHttpServletRequest request=new MockHttpServletRequest();
        Login login=new Login();
        login.setId(1);
        login.setRole(1);
        request.getSession().setAttribute("login",login);
        MockHttpServletResponse response=new MockHttpServletResponse();

        boolean allowed=interceptor.preHandle(request,response,new Object());

        assertTrue(allowed);
    }
}
