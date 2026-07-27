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
    public void patientCannotAccessAdminEndpoints() throws Exception {
        MockHttpServletRequest request = request("/admin/patientManage", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals(403, response.getStatus());
    }

    @Test
    public void doctorWorkflowRequiresDoctorRole() throws Exception {
        MockHttpServletRequest request = request("/doctor/drug", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals(403, response.getStatus());
    }

    @Test
    public void authenticatedDoctorLookupEndpointRemainsAvailable() throws Exception {
        MockHttpServletRequest request = request("/doctor/cardiology", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    private MockHttpServletRequest request(String uri, int role) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(uri);
        Login login = new Login();
        login.setId(10);
        login.setRole(role);
        request.getSession().setAttribute("login", login);
        return request;
    }
}
