package com.hospital.controller;

import com.hospital.config.intercepors.LoginInterceptor;
import com.hospital.entity.Login;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class LoginInterceptorTest {
    private final LoginInterceptor interceptor = new LoginInterceptor();

    @Test
    public void rejectsUnhydratedLoginObject() throws Exception {
        MockHttpServletRequest request = request("/admin/adminManage", new Login());
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        Assert.assertFalse(allowed);
        Assert.assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void rejectsWrongRoleForAdminPath() throws Exception {
        Login patient = login(3);
        MockHttpServletRequest request = request("/admin/adminManage", patient);
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        Assert.assertFalse(allowed);
        Assert.assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void rejectsWrongRoleForDoctorWorkflowPath() throws Exception {
        Login patient = login(3);
        MockHttpServletRequest request = request("/doctor/seekinfo", patient);
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        Assert.assertFalse(allowed);
        Assert.assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void allowsAuthenticatedDoctorDepartmentLookupForNonDoctorRoles() throws Exception {
        Login patient = login(3);
        MockHttpServletRequest request = request("/doctor/internal", patient);
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        Assert.assertTrue(allowed);
        Assert.assertNull(response.getRedirectedUrl());
    }

    private MockHttpServletRequest request(String uri, Login login) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(uri);
        request.getSession().setAttribute("login", login);
        return request;
    }

    private Login login(int role) {
        Login login = new Login();
        login.setId(1);
        login.setRole(role);
        return login;
    }
}
