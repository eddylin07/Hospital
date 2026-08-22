package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
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
    public void loginFailureDoesNotStoreSessionLogin() {
        Login login = new Login();
        login.setUsername("doctor");
        when(loginService.login(login)).thenReturn("密码错误");

        JSONObject response = controller.login(login, session);

        assertEquals("密码错误", response.getString("message"));
        verify(session, never()).setAttribute("login", login);
    }

    @Test
    public void loginSuccessStoresAuthenticatedLoginInSession() {
        Login login = new Login();
        login.setUsername("doctor");
        when(loginService.login(login)).thenReturn("登录成功2");

        JSONObject response = controller.login(login, session);

        assertEquals("登录成功2", response.getString("message"));
        verify(session).setAttribute("login", login);
    }
}
