package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Before;
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
    private LoginController controller;
    private LoginService loginService;
    private HttpSession session;

    @Before
    public void setUp() {
        controller = new LoginController();
        loginService = mock(LoginService.class);
        session = mock(HttpSession.class);
        controller.loginService = loginService;
    }

    @Test
    public void loginDoesNotStoreSessionWhenAuthenticationFails() {
        Login login = new Login();
        login.setUsername("patient");
        when(loginService.login(login)).thenReturn("密码错误");

        JSONObject result = controller.login(login, session);

        assertEquals("密码错误", result.getString("message"));
        verify(session, never()).setAttribute(eq("login"), any());
    }

    @Test
    public void loginStoresAuthenticatedLoginWhenServiceReportsSuccess() {
        Login login = new Login();
        login.setUsername("patient");
        when(loginService.login(login)).thenReturn("登录成功3");

        JSONObject result = controller.login(login, session);

        assertEquals("登录成功3", result.getString("message"));
        verify(session).setAttribute("login", login);
    }
}
