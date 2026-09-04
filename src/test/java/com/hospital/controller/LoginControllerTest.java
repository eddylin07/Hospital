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
        login.setUsername("admin");
        login.setPassword("bad-password");
        when(loginService.login(login)).thenReturn("密码错误");

        MockHttpSession session = new MockHttpSession();
        JSONObject response = controller.login(login, session);

        assertEquals("密码错误", response.get("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedLoginInSession() {
        LoginController controller = new LoginController();
        LoginService loginService = mock(LoginService.class);
        controller.loginService = loginService;

        Login login = new Login();
        login.setUsername("admin");
        login.setPassword("correct-password");
        when(loginService.login(login)).thenReturn("登录成功1");

        MockHttpSession session = new MockHttpSession();
        JSONObject response = controller.login(login, session);

        assertEquals("登录成功1", response.get("message"));
        assertSame(login, session.getAttribute("login"));
    }
}
