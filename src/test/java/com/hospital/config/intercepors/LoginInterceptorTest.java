package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class LoginInterceptorTest {

    @Test
    public void rejectsUnhydratedSessionLogin() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = request("/patient/appointment");
        request.getSession().setAttribute("login", new Login());
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        Assert.assertFalse(allowed);
        Assert.assertEquals("/hospital/login", response.getRedirectedUrl());
    }

    @Test
    public void forbidsPatientFromAdminPath() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = request("/admin/patientManage");
        request.getSession().setAttribute("login", login(7, 3));
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        Assert.assertFalse(allowed);
        Assert.assertEquals(403, response.getStatus());
    }

    @Test
    public void allowsAuthenticatedDoctorLookupForPatientAppointmentPage() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = request("/doctor/内科");
        request.getSession().setAttribute("login", login(7, 3));
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        Assert.assertTrue(allowed);
    }

    private MockHttpServletRequest request(String path) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(path);
        return request;
    }

    private Login login(Integer id, Integer role) {
        Login login = new Login();
        login.setId(id);
        login.setRole(role);
        return login;
    }
}
