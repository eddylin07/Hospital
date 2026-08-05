package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {
    private final LoginInterceptor interceptor = new LoginInterceptor();

    @Test
    public void rejectsLoginObjectWithoutAuthenticatedIdentity() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        request.getSession().setAttribute("login", new Login());
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertTrue(response.getRedirectedUrl().endsWith("/hospital/login"));
    }

    @Test
    public void blocksPatientFromAdminAndDoctorWorkflowPaths() throws Exception {
        assertForbidden("/admin/adminManage", "GET", login(3));
        assertForbidden("/hospital/admin/index", "GET", login(3));
        assertForbidden("/doctor/seekMedicalAdvice", "GET", login(3));
        assertForbidden("/hospital/doctor/index", "GET", login(3));
        assertAllowed("/doctor/internal", "GET", login(3));
    }

    @Test
    public void allowsOnlyMatchingRoleForPatientArea() throws Exception {
        assertForbidden("/patient/appointment", "GET", login(2));
        assertAllowed("/patient/appointment", "GET", login(3));
    }

    private void assertForbidden(String path, String method, Login login) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(method, path);
        request.getSession().setAttribute("login", login);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertTrue(response.getStatus() == HttpServletResponse.SC_FORBIDDEN);
    }

    private void assertAllowed(String path, String method, Login login) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(method, path);
        request.getSession().setAttribute("login", login);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    private Login login(Integer role) {
        Login login = new Login();
        login.setId(10);
        login.setRole(role);
        return login;
    }
}
