package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {
    private final LoginInterceptor interceptor = new LoginInterceptor();

    @Test
    public void invalidSessionIsRejectedBeforeProtectedRoute() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void patientCannotAccessAdminRoute() throws Exception {
        MockHttpServletRequest request = requestWithLogin("GET", "/admin/adminManage", 7, 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    @Test
    public void patientCanLookupDoctorsByDepartmentButNotDoctorWorkflow() throws Exception {
        MockHttpServletResponse lookupResponse = new MockHttpServletResponse();
        boolean lookupAllowed = interceptor.preHandle(
                requestWithLogin("GET", "/doctor/Cardiology", 7, 3), lookupResponse, new Object());

        MockHttpServletResponse workflowResponse = new MockHttpServletResponse();
        boolean workflowAllowed = interceptor.preHandle(
                requestWithLogin("GET", "/doctor/seekMedicalAdvice", 7, 3), workflowResponse, new Object());

        assertTrue(lookupAllowed);
        assertFalse(workflowAllowed);
        assertEquals(HttpServletResponse.SC_FORBIDDEN, workflowResponse.getStatus());
    }

    private MockHttpServletRequest requestWithLogin(String method, String uri, Integer id, Integer role) {
        MockHttpServletRequest request = new MockHttpServletRequest(method, uri);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(id);
        login.setRole(role);
        session.setAttribute("login", login);
        request.setSession(session);
        return request;
    }
}
