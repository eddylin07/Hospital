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
    public void failedLoginDoesNotCreateAuthenticatedSession() {
        LoginController controller = new LoginController();
        LoginService loginService = mock(LoginService.class);
        controller.loginService = loginService;
        Login login = new Login();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        when(loginService.login(login)).thenReturn("密码错误");

        JSONObject result = controller.login(login, session);

        assertEquals("密码错误", result.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedSession() {
        LoginController controller = new LoginController();
        LoginService loginService = mock(LoginService.class);
        controller.loginService = loginService;
        Login login = new Login();
        MockHttpSession session = new MockHttpSession();

        when(loginService.login(login)).thenReturn("登录成功1");

        JSONObject result = controller.login(login, session);

        assertEquals("登录成功1", result.getString("message"));
        assertSame(login, session.getAttribute("login"));
    }
}
