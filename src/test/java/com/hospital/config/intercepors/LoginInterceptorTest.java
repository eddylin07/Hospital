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
    public void rejectsSessionWithoutResolvedIdentity() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/patientManage");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", new Login());

        assertFalse(interceptor.preHandle(request, response, null));
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void rejectsPatientAccessingAdminRoutes() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/patientManage");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", login(7, 3));

        assertFalse(interceptor.preHandle(request, response, null));
        assertEquals(403, response.getStatus());
    }

    @Test
    public void allowsDoctorDepartmentLookupForAuthenticatedUsers() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/doctor/cardiology");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.getSession().setAttribute("login", login(7, 3));

        assertTrue(interceptor.preHandle(request, response, null));
    }

    private Login login(Integer id, Integer role) {
        Login login = new Login();
        login.setId(id);
        login.setRole(role);
        return login;
    }
}
