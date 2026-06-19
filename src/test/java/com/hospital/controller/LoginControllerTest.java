package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginControllerTest {

    @Test
    public void failedLoginClearsAuthenticatedSession() {
        LoginController controller=new LoginController();
        LoginService loginService=mock(LoginService.class);
        controller.loginService=loginService;
        Login submittedLogin=new Login();
        submittedLogin.setUsername("bad-user");
        MockHttpSession session=new MockHttpSession();
        Login existingLogin=new Login();
        existingLogin.setId(1);
        existingLogin.setRole(1);
        session.setAttribute("login",existingLogin);
        when(loginService.login(submittedLogin)).thenReturn("密码错误");

        JSONObject response=controller.login(submittedLogin,session);

        assertEquals("密码错误",response.get("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedSession() {
        LoginController controller=new LoginController();
        LoginService loginService=mock(LoginService.class);
        controller.loginService=loginService;
        Login submittedLogin=new Login();
        submittedLogin.setUsername("admin");
        MockHttpSession session=new MockHttpSession();
        when(loginService.login(submittedLogin)).thenAnswer(invocation -> {
            submittedLogin.setId(1);
            submittedLogin.setRole(1);
            return "登录成功1";
        });

        JSONObject response=controller.login(submittedLogin,session);

        assertEquals("登录成功1",response.get("message"));
        assertSame(submittedLogin,session.getAttribute("login"));
    }
}
