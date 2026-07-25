package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {
    @Mock
    private LoginService loginService;
    @Mock
    private HttpSession session;

    @Test
    public void loginDoesNotStoreSessionWhenAuthenticationFails() {
        LoginController controller = new LoginController();
        controller.loginService = loginService;
        Login login = new Login();
        login.setUsername("patient");

        when(loginService.login(login)).thenReturn("密码错误");

        JSONObject result = controller.login(login, session);

        assertEquals("密码错误", result.getString("message"));
        verify(session, never()).setAttribute("login", login);
    }

    @Test
    public void loginStoresAuthenticatedLoginWhenServiceReportsSuccess() {
        LoginController controller = new LoginController();
        controller.loginService = loginService;
        Login login = new Login();
        login.setUsername("patient");

        when(loginService.login(login)).thenReturn("登录成功3");

        JSONObject result = controller.login(login, session);

        assertEquals("登录成功3", result.getString("message"));
        verify(session).setAttribute("login", login);
    }
}
