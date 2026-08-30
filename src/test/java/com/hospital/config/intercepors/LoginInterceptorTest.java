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
    public void unpopulatedLoginIsRejected() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        request.getSession().setAttribute("login", new Login());
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void patientCannotAccessAdminOrDoctorWorkflows() throws Exception {
        MockHttpServletRequest adminRequest = authedRequest("GET", "/admin/adminManage", 3);
        MockHttpServletResponse adminResponse = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(adminRequest, adminResponse, new Object()));
        assertEquals(403, adminResponse.getStatus());

        MockHttpServletRequest doctorRequest = authedRequest("GET", "/doctor/seekMedicalAdvice", 3);
        MockHttpServletResponse doctorResponse = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(doctorRequest, doctorResponse, new Object()));
        assertEquals(403, doctorResponse.getStatus());
    }

    @Test
    public void authenticatedPatientCanLookupDoctorsByDepartment() throws Exception {
        MockHttpServletRequest request = authedRequest("GET", "/doctor/internal", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
        assertEquals(200, response.getStatus());
    }

    private MockHttpServletRequest authedRequest(String method, String uri, int role) {
        MockHttpServletRequest request = new MockHttpServletRequest(method, uri);
        Login login = new Login();
        login.setId(1);
        login.setRole(role);
        request.getSession().setAttribute("login", login);
        return request;
    }
}
