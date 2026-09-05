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
    public void rejectsLoggedInPatientFromAdminPath() throws Exception {
        MockHttpServletRequest request = request("/admin/adminManage", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    public void rejectsPatientFromDoctorWorkflowPath() throws Exception {
        MockHttpServletRequest request = request("/doctor/drug", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    public void allowsAuthenticatedPatientToQueryDoctorsByDepartment() throws Exception {
        MockHttpServletRequest request = request("/doctor/cardiology", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    private MockHttpServletRequest request(String uri, int role) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", uri);
        Login login = new Login();
        login.setId(9);
        login.setRole(role);
        request.getSession().setAttribute("login", login);
        return request;
    }
}
