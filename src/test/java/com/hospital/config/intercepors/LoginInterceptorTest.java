package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {
    private final LoginInterceptor interceptor=new LoginInterceptor();

    @Test
    public void rejectsSessionWithoutAuthenticatedIdentity() throws Exception {
        MockHttpServletRequest request=new MockHttpServletRequest("GET","/admin/adminManage");
        request.getSession().setAttribute("login",new Login());
        MockHttpServletResponse response=new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request,response,new Object()));

        assertEquals("/hospital/login",response.getRedirectedUrl());
    }

    @Test
    public void rejectsPatientFromAdminEndpoints() throws Exception {
        MockHttpServletRequest request=new MockHttpServletRequest("DELETE","/admin/patient/1");
        request.getSession().setAttribute("login",login(7,3));
        MockHttpServletResponse response=new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request,response,new Object()));

        assertEquals(HttpServletResponse.SC_FORBIDDEN,response.getStatus());
    }

    @Test
    public void rejectsPatientFromDoctorWorkflowEndpoints() throws Exception {
        MockHttpServletRequest request=new MockHttpServletRequest("PUT","/doctor/drug");
        request.getSession().setAttribute("login",login(7,3));
        MockHttpServletResponse response=new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request,response,new Object()));

        assertEquals(HttpServletResponse.SC_FORBIDDEN,response.getStatus());
    }

    @Test
    public void allowsPatientToUseAuthenticatedDoctorLookupEndpoint() throws Exception {
        MockHttpServletRequest request=new MockHttpServletRequest("GET","/doctor/Cardiology");
        request.getSession().setAttribute("login",login(7,3));
        MockHttpServletResponse response=new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request,response,new Object()));
    }

    private Login login(Integer id,Integer role){
        Login login=new Login();
        login.setId(id);
        login.setRole(role);
        return login;
    }
}
