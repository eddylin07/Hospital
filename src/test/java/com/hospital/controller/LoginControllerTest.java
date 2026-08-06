package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {
    @Mock
    private LoginService loginService;
    @Mock
    private HttpSession session;

    private LoginController controller;

    @Before
    public void setUp() {
        controller = new LoginController();
        controller.loginService = loginService;
    }

    @Test
    public void failedLoginDoesNotStoreSessionUser() {
        Login login = new Login();
        when(loginService.login(login)).thenReturn("密码错误");

        JSONObject response = controller.login(login, session);

        assertEquals("密码错误", response.getString("message"));
        verify(session, never()).setAttribute(eq("login"), eq(login));
    }

    @Test
    public void successfulLoginStoresAuthenticatedUser() {
        Login login = new Login();
        when(loginService.login(login)).thenReturn("登录成功3");

        JSONObject response = controller.login(login, session);

        assertEquals("登录成功3", response.getString("message"));
        verify(session).setAttribute("login", login);
    }
}
