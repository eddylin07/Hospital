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
    public void redirectsUnauthenticatedRequestsToLogin() throws Exception {
        MockHttpServletRequest request = request("GET", "/admin/adminManage");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void rejectsForgedSessionWithoutVerifiedId() throws Exception {
        MockHttpServletRequest request = request("GET", "/admin/adminManage");
        request.getSession().setAttribute("login", login(null, 1));
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    @Test
    public void rejectsPatientAccessToAdminRoutes() throws Exception {
        MockHttpServletRequest request = request("GET", "/admin/adminManage");
        request.getSession().setAttribute("login", login(10, 3));
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    @Test
    public void allowsMatchingRoleRoutes() throws Exception {
        MockHttpServletRequest adminRequest = request("GET", "/admin/adminManage");
        adminRequest.getSession().setAttribute("login", login(1, 1));
        assertTrue(interceptor.preHandle(adminRequest, new MockHttpServletResponse(), new Object()));

        MockHttpServletRequest doctorRequest = request("GET", "/doctor/seekMedicalAdvice");
        doctorRequest.getSession().setAttribute("login", login(2, 2));
        assertTrue(interceptor.preHandle(doctorRequest, new MockHttpServletResponse(), new Object()));

        MockHttpServletRequest patientRequest = request("GET", "/patient/appointment");
        patientRequest.getSession().setAttribute("login", login(3, 3));
        assertTrue(interceptor.preHandle(patientRequest, new MockHttpServletResponse(), new Object()));
    }

    @Test
    public void preservesAuthenticatedDoctorDepartmentLookupForPatients() throws Exception {
        MockHttpServletRequest lookup = request("GET", "/doctor/cardiology");
        lookup.getSession().setAttribute("login", login(3, 3));
        assertTrue(interceptor.preHandle(lookup, new MockHttpServletResponse(), new Object()));

        MockHttpServletRequest workflow = request("GET", "/doctor/seekMedicalAdvice");
        workflow.getSession().setAttribute("login", login(3, 3));
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(workflow, response, new Object()));
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    private static MockHttpServletRequest request(String method, String path) {
        MockHttpServletRequest request = new MockHttpServletRequest(method, path);
        request.setRequestURI(path);
        return request;
    }

    private static Login login(Integer id, Integer role) {
        Login login = new Login();
        login.setId(id);
        login.setRole(role);
        return login;
    }
}
