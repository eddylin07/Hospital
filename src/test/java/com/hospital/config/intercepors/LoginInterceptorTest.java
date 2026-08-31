package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {

    @Test
    public void rejectsSessionWithoutHydratedIdentity() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", new Login());

        assertFalse(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void rejectsPatientRoleFromAdminPaths() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/adminManage");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Login login = new Login();
        login.setId(10);
        login.setRole(3);
        request.getSession().setAttribute("login", login);

        assertFalse(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void keepsDepartmentLookupAvailableToAuthenticatedPatients() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/doctor/internal");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Login login = new Login();
        login.setId(10);
        login.setRole(3);
        request.getSession().setAttribute("login", login);

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }
}
