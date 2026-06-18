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
    public void rejectsSessionWithoutAuthenticatedLoginId() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request("/admin/doctorManage", login(null, 1)), response, new Object());

        assertFalse(allowed);
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void blocksPatientFromAdminRoutes() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request("/admin/doctorManage", login(7, 3)), response, new Object());

        assertFalse(allowed);
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    @Test
    public void allowsOnlyExpectedRolesForProtectedRoutes() throws Exception {
        assertTrue(interceptor.preHandle(request("/admin/doctorManage", login(1, 1)), new MockHttpServletResponse(), new Object()));
        assertTrue(interceptor.preHandle(request("/patient/appointment", login(2, 3)), new MockHttpServletResponse(), new Object()));
        assertTrue(interceptor.preHandle(request("/doctor/seekMedicalAdvice", login(3, 2)), new MockHttpServletResponse(), new Object()));
    }

    private MockHttpServletRequest request(String uri, Login login) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", uri);
        request.getSession().setAttribute("login", login);
        return request;
    }

    private Login login(Integer id, Integer role) {
        Login login = new Login();
        login.setId(id);
        login.setRole(role);
        return login;
    }
}
