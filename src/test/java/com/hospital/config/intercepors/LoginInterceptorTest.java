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
    public void missingSessionRedirectsToLogin() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request("/admin/adminManage", null), response, new Object());

        assertFalse(allowed);
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void sessionWithoutResolvedIdentityIsNotAuthenticated() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        Login login = new Login();
        login.setUsername("admin");
        MockHttpServletRequest request = request("/admin/adminManage", login);

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void patientCannotAccessAdminPath() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request("/admin/adminManage", login(3)), response, new Object());

        assertFalse(allowed);
        assertEquals(403, response.getStatus());
    }

    @Test
    public void patientCannotUseDoctorWorkflowPath() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request("/doctor/drug", login(3)), response, new Object());

        assertFalse(allowed);
        assertEquals(403, response.getStatus());
    }

    @Test
    public void matchingRolesCanAccessTheirPaths() throws Exception {
        assertTrue(interceptor.preHandle(request("/admin/adminManage", login(1)), new MockHttpServletResponse(), new Object()));
        assertTrue(interceptor.preHandle(request("/doctor/seekMedicalAdvice", login(2)), new MockHttpServletResponse(), new Object()));
        assertTrue(interceptor.preHandle(request("/patient/patientManage", login(3)), new MockHttpServletResponse(), new Object()));
    }

    @Test
    public void authenticatedDepartmentLookupRemainsAvailableAcrossRoles() throws Exception {
        assertTrue(interceptor.preHandle(request("/doctor/Cardiology", login(3)), new MockHttpServletResponse(), new Object()));
    }

    private MockHttpServletRequest request(String uri, Login login) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", uri);
        request.setRequestURI(uri);
        if (login != null) {
            request.getSession().setAttribute("login", login);
        }
        return request;
    }

    private Login login(int role) {
        Login login = new Login();
        login.setId(11);
        login.setRole(role);
        return login;
    }
}
