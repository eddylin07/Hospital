package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {
    private final LoginInterceptor interceptor = new LoginInterceptor();

    @Test
    public void missingLoginRedirectsToLoginPage() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/drugsManage");
        request.setRequestURI("/admin/drugsManage");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void patientCannotAccessAdminMutation() throws Exception {
        MockHttpServletRequest request = requestWithLogin("/admin/drug/1", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    public void doctorDepartmentLookupAllowsAuthenticatedNonDoctorUsers() throws Exception {
        MockHttpServletRequest request = requestWithLogin("/doctor/cardiology", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    private MockHttpServletRequest requestWithLogin(String uri, Integer role) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", uri);
        request.setRequestURI(uri);
        Login login = new Login();
        login.setId(10);
        login.setRole(role);
        request.getSession().setAttribute("login", login);
        return request;
    }
}
