package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {
    private final LoginInterceptor interceptor = new LoginInterceptor();

    @Test
    public void rejectsUnhydratedLoginSession() throws Exception {
        MockHttpServletRequest request = request("/admin/doctorManage");
        Login login = login(null, 1);
        request.getSession().setAttribute("login", login);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void rejectsCrossRoleAdminAccess() throws Exception {
        MockHttpServletRequest request = request("/admin/doctorManage");
        request.getSession().setAttribute("login", login(5, 3));
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    @Test
    public void allowsMatchingRoleAccess() throws Exception {
        MockHttpServletRequest request = request("/patient/medicalhistory");
        request.getSession().setAttribute("login", login(5, 3));

        assertTrue(interceptor.preHandle(request, new MockHttpServletResponse(), new Object()));
    }

    @Test
    public void keepsDepartmentLookupAvailableToAuthenticatedUsers() throws Exception {
        MockHttpServletRequest request = request("/doctor/internal");
        request.setMethod("GET");
        request.getSession().setAttribute("login", login(5, 3));

        assertTrue(interceptor.preHandle(request, new MockHttpServletResponse(), new Object()));
    }

    private MockHttpServletRequest request(String uri) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(uri);
        return request;
    }

    private Login login(Integer id, Integer role) {
        Login login = new Login();
        login.setId(id);
        login.setRole(role);
        return login;
    }
}
