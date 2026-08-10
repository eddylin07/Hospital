package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorRoleTest {

    private final LoginInterceptor interceptor = new LoginInterceptor();

    @Test
    public void rejectsWrongRoleForAdminPath() throws Exception {
        MockHttpServletRequest request = request("/admin/adminManage", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals(403, response.getStatus());
    }

    @Test
    public void allowsMatchingRoleForPatientPath() throws Exception {
        MockHttpServletRequest request = request("/patient/appointment", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertTrue(allowed);
    }

    @Test
    public void keepsDepartmentLookupAvailableToAuthenticatedNonDoctors() throws Exception {
        MockHttpServletRequest request = request("/doctor/cardiology", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertTrue(allowed);
    }

    private MockHttpServletRequest request(String uri, Integer role) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", uri);
        Login login = new Login();
        login.setId(10);
        login.setRole(role);
        request.getSession().setAttribute("login", login);
        return request;
    }
}
