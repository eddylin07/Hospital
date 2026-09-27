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
    public void patientSessionCannotAccessAdminRoutes() throws Exception {
        MockHttpServletRequest request = request("/admin/patientManage", "GET", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void patientSessionCanUseDoctorDepartmentLookup() throws Exception {
        MockHttpServletRequest request = request("/doctor/cardiology", "GET", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    @Test
    public void patientSessionCannotAccessDoctorWorkflowRoute() throws Exception {
        MockHttpServletRequest request = request("/doctor/seekMedicalAdvice", "GET", 3);
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
    }

    private MockHttpServletRequest request(String uri, String method, int role) {
        MockHttpServletRequest request = new MockHttpServletRequest(method, uri);
        Login login = new Login();
        login.setId(10);
        login.setRole(role);
        request.getSession().setAttribute("login", login);
        return request;
    }
}
