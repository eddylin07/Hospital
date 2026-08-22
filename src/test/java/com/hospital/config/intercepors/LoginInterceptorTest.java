package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {
    @Test
    public void rejectsUnhydratedLoginObjects() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", new Login());

        assertFalse(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void rejectsRoleMismatchForAdminRoutes() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", login(7, 3));

        assertFalse(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void allowsAuthenticatedDoctorLookupForNonDoctorRoles() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/doctor/internal");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", login(7, 3));

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void protectsDoctorWorkflowRoutes() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("PUT", "/doctor/drug");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", login(7, 3));

        assertFalse(interceptor.preHandle(request, response, new Object()));
    }

    private Login login(Integer id, Integer role) {
        Login login = new Login();
        login.setId(id);
        login.setRole(role);
        return login;
    }
}
