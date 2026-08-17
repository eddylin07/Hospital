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
    public void rejectsMissingSessionWithoutCreatingOne() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertFalse(request.isRequestedSessionIdValid());
    }

    @Test
    public void rejectsRoleMismatchOnAdminPath() throws Exception {
        MockHttpServletRequest request = requestWithLogin("/admin/adminManage", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void allowsDoctorWorkflowOnlyForDoctorRole() throws Exception {
        assertFalse(interceptor.preHandle(requestWithLogin("/doctor/drug", 3), new MockHttpServletResponse(), new Object()));
        assertTrue(interceptor.preHandle(requestWithLogin("/doctor/drug", 2), new MockHttpServletResponse(), new Object()));
    }

    @Test
    public void keepsAuthenticatedDoctorLookupAvailableForPatients() throws Exception {
        assertTrue(interceptor.preHandle(requestWithLogin("/doctor/cardiology", 3), new MockHttpServletResponse(), new Object()));
    }

    private MockHttpServletRequest requestWithLogin(String path, int role) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", path);
        Login login = new Login();
        login.setId(10);
        login.setRole(role);
        request.getSession().setAttribute("login", login);
        return request;
    }
}
