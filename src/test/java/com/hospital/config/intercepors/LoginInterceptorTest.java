package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;

public class LoginInterceptorTest {
    @Test
    public void rejectsSessionWithoutTrustedIdentity() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/patientManage");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Login login = new Login();
        login.setRole(1);
        request.getSession().setAttribute("login", login);

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void rejectsPatientAccessToAdminPaths() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("DELETE", "/admin/patient/5");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", login(3));

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    public void permitsDepartmentLookupForAuthenticatedPatients() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/doctor/cardiology");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", login(3));

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    private static Login login(Integer role) {
        Login login = new Login();
        login.setId(10);
        login.setRole(role);
        return login;
    }
}
