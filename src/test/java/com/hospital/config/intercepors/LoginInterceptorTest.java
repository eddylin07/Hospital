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
    public void patientCannotAccessAdminRoutes() throws Exception {
        MockHttpServletRequest request = requestWithRole("/admin/adminManage", "GET", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    public void patientCannotUseDoctorWriteRoutes() throws Exception {
        MockHttpServletRequest request = requestWithRole("/doctor/drug", "PUT", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    public void patientCannotUseDoctorSeekMedicalAdviceRoute() throws Exception {
        MockHttpServletRequest request = requestWithRole("/doctor/seekMedicalAdvice", "GET", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    public void authenticatedDepartmentLookupRemainsAvailable() throws Exception {
        MockHttpServletRequest request = requestWithRole("/doctor/internal", "GET", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    private MockHttpServletRequest requestWithRole(String uri, String method, int role) {
        MockHttpServletRequest request = new MockHttpServletRequest(method, uri);
        Login login = new Login();
        login.setId(9);
        login.setRole(role);
        request.getSession().setAttribute("login", login);
        return request;
    }
}
