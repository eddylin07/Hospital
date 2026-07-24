package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {

    private final LoginInterceptor interceptor=new LoginInterceptor();

    @Test
    public void redirectsWhenSessionHasNoAuthenticatedLogin() throws Exception {
        MockHttpServletRequest request=new MockHttpServletRequest("GET","/admin/adminManage");
        MockHttpServletResponse response=new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request,response,new Object()));
        assertEquals("/hospital/login",response.getRedirectedUrl());
    }

    @Test
    public void blocksPatientFromAdminRoutes() throws Exception {
        MockHttpServletRequest request=authenticatedRequest("/admin/adminManage",3);
        MockHttpServletResponse response=new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request,response,new Object()));
        assertEquals(403,response.getStatus());
    }

    @Test
    public void allowsDoctorDepartmentLookupForAuthenticatedUsers() throws Exception {
        MockHttpServletRequest request=authenticatedRequest("/doctor/cardiology",3);
        MockHttpServletResponse response=new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request,response,new Object()));
    }

    @Test
    public void blocksPatientFromDoctorWorkflowRoutes() throws Exception {
        MockHttpServletRequest request=authenticatedRequest("/doctor/drug",3);
        MockHttpServletResponse response=new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request,response,new Object()));
        assertEquals(403,response.getStatus());
    }

    private MockHttpServletRequest authenticatedRequest(String path,Integer role) {
        MockHttpServletRequest request=new MockHttpServletRequest("GET",path);
        Login login=new Login();
        login.setId(6);
        login.setRole(role);
        request.getSession(true).setAttribute("login",login);
        return request;
    }
}
