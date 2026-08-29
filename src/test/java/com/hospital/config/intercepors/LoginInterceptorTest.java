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
    public void rejectsMissingAuthenticatedIdentity() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void rejectsWrongRoleForAdminPath() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", login(5, 3));

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    public void allowsMatchingRoleForPatientPath() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/patient/appointment");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", login(5, 3));

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void keepsDepartmentLookupAvailableToLoggedInPatients() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/doctor/internal");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", login(5, 3));

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    private Login login(Integer id, Integer role) {
        Login login = new Login();
        login.setId(id);
        login.setRole(role);
        return login;
    }
}
