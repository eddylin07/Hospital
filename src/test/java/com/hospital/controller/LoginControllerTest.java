package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginControllerTest {
    private LoginController controller;
    private LoginService loginService;
    private MockHttpSession session;

    @Before
    public void setUp() {
        controller=new LoginController();
        loginService=mock(LoginService.class);
        controller.loginService=loginService;
        session=new MockHttpSession();
    }

    @Test
    public void failedLoginDoesNotCreateAuthenticatedSession() {
        Login existingLogin=new Login();
        existingLogin.setId(1);
        existingLogin.setUsername("existing");
        session.setAttribute("login",existingLogin);

        Login submittedLogin=new Login();
        submittedLogin.setUsername("admin");
        submittedLogin.setPassword("wrong-password");
        submittedLogin.setId(1);
        submittedLogin.setRole(1);
        when(loginService.login(submittedLogin)).thenReturn("密码错误");

        JSONObject response=controller.login(submittedLogin,session);

        assertEquals("密码错误",response.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresSanitizedAuthenticatedSession() {
        Login submittedLogin=new Login();
        submittedLogin.setUsername("doctor");
        submittedLogin.setPassword("correct-password");
        when(loginService.login(submittedLogin)).thenAnswer(invocation -> {
            submittedLogin.setId(2);
            submittedLogin.setRole(2);
            return "登录成功2";
        });

        JSONObject response=controller.login(submittedLogin,session);

        Login sessionLogin=(Login) session.getAttribute("login");
        assertEquals("登录成功2",response.getString("message"));
        assertEquals(Integer.valueOf(2),sessionLogin.getId());
        assertEquals(Integer.valueOf(2),sessionLogin.getRole());
        assertEquals("doctor",sessionLogin.getUsername());
        assertNull(sessionLogin.getPassword());
    }
}
