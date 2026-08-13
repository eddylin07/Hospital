package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginControllerTest {
    @Test
    public void failedLoginDoesNotCreateAuthenticatedSession() {
        LoginController controller = new LoginController();
        LoginService loginService = mock(LoginService.class);
        HttpSession session = mock(HttpSession.class);
        controller.loginService = loginService;

        Login login = new Login();
        login.setUsername("patient");
        when(loginService.login(login)).thenReturn("密码错误");

        JSONObject response = controller.login(login, session);

        assertEquals("密码错误", response.getString("message"));
        verify(session, never()).setAttribute(eq("login"), any(Login.class));
    }

    @Test
    public void successfulLoginStoresAuthenticatedSession() {
        LoginController controller = new LoginController();
        LoginService loginService = mock(LoginService.class);
        HttpSession session = mock(HttpSession.class);
        controller.loginService = loginService;

        Login login = new Login();
        login.setUsername("doctor");
        when(loginService.login(login)).thenReturn("登录成功2");

        JSONObject response = controller.login(login, session);

        assertEquals("登录成功2", response.getString("message"));
        verify(session).setAttribute("login", login);
    }
}
