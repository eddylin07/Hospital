package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {
    private final LoginInterceptor interceptor = new LoginInterceptor();

    @Test
    public void rejectsAuthenticatedPatientFromAdminEndpoints() throws Exception {
        MockHttpServletRequest request = request("GET", "/admin/patientManage", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void rejectsPatientFromDoctorWorkflowEndpoint() throws Exception {
        MockHttpServletRequest request = request("GET", "/doctor/seekMedicalAdvice", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void allowsAuthenticatedDoctorDepartmentLookupForPatients() throws Exception {
        MockHttpServletRequest request = request("GET", "/doctor/cardiology", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void allowsDoctorWorkflowForDoctorRole() throws Exception {
        MockHttpServletRequest request = request("PUT", "/doctor/drug", 2);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    private MockHttpServletRequest request(String method, String uri, int role) {
        MockHttpServletRequest request = new MockHttpServletRequest(method, uri);
        Login login = new Login();
        login.setId(7);
        login.setRole(role);
        request.getSession().setAttribute("login", login);
        return request;
    }
}
