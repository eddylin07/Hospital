package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {
    private final LoginInterceptor interceptor = new LoginInterceptor();

    @Test
    public void rejectsSessionWithoutAuthenticatedUserId() throws Exception {
        MockHttpServletRequest request = request("/admin/doctorManage", login(null, 1));
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));

        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void rejectsWrongRoleForAdminPath() throws Exception {
        MockHttpServletRequest request = request("/admin/drug/1", login(10, 3));
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));

        assertEquals(403, response.getStatus());
    }

    @Test
    public void allowsPatientSearchForAuthenticatedPatient() throws Exception {
        MockHttpServletRequest request = request("/patient/searchinfo", login(10, 3));
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    private MockHttpServletRequest request(String path, Login login) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", path);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login);
        request.setSession(session);
        return request;
    }

    private Login login(Integer id, Integer role) {
        Login login = new Login();
        login.setId(id);
        login.setRole(role);
        return login;
    }
}
