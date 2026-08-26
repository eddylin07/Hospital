package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {

    private final LoginInterceptor interceptor = new LoginInterceptor();

    @Test
    public void rejectsSessionLoginWithoutResolvedIdentity() throws Exception {
        MockHttpServletRequest request = request("/admin/patientManage", "GET", login(null, null));
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void rejectsPatientFromAdminRoutes() throws Exception {
        MockHttpServletRequest request = request("/admin/patientManage", "GET", login(10, 3));
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void rejectsPatientFromDoctorWorkflowRoutes() throws Exception {
        MockHttpServletRequest request = request("/doctor/drug", "PUT", login(10, 3));
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void allowsAuthenticatedDoctorDepartmentLookupForAppointmentPages() throws Exception {
        MockHttpServletRequest request = request("/doctor/cardiology", "GET", login(10, 3));
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    private MockHttpServletRequest request(String uri, String method, Login login) {
        MockHttpServletRequest request = new MockHttpServletRequest(method, uri);
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
