package com.hospital.config.intercepors;

import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginInterceptorTest {
    private final LoginInterceptor interceptor=new LoginInterceptor();

    @Test
    public void rejectsUnhydratedLoginObject() throws Exception {
        MockHttpServletRequest request=new MockHttpServletRequest("GET","/admin/adminManage");
        request.getSession().setAttribute("login",new Login());
        MockHttpServletResponse response=new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request,response,new Object()));
        assertEquals("/hospital/login",response.getRedirectedUrl());
    }

    @Test
    public void patientCannotAccessAdminOrDoctorWorkflow() throws Exception {
        assertFalse(preHandle("/admin/adminManage",3));
        assertFalse(preHandle("/doctor/drug",3));
    }

    @Test
    public void patientCanUseAuthenticatedDoctorDepartmentLookup() throws Exception {
        assertTrue(preHandle("/doctor/内科",3));
    }

    @Test
    public void doctorCannotOpenPatientArea() throws Exception {
        assertFalse(preHandle("/patient/appointment",2));
    }

    private boolean preHandle(String path, int role) throws Exception {
        MockHttpServletRequest request=new MockHttpServletRequest("GET",path);
        Login login=new Login();
        login.setId(99);
        login.setRole(role);
        request.getSession().setAttribute("login",login);
        return interceptor.preHandle(request,new MockHttpServletResponse(),new Object());
    }
}
