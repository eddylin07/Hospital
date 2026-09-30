package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginControllerTest {
    private LoginController controller;

    @Mock
    private LoginService loginService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new LoginController();
        ReflectionTestUtils.setField(controller, "loginService", loginService);
    }

    @Test
    public void failedLoginDoesNotCreateAuthenticatedSession() {
        Login login = new Login();
        HttpSession session = mock(HttpSession.class);
        when(loginService.login(login)).thenReturn("密码错误");

        JSONObject response = controller.login(login, session);

        assertEquals("密码错误", response.getString("message"));
        verify(session, never()).setAttribute(eq("login"), eq(login));
    }

    @Test
    public void successfulLoginStoresAuthenticatedLoginInSession() {
        Login login = new Login();
        HttpSession session = mock(HttpSession.class);
        when(loginService.login(login)).thenReturn("登录成功1");

        JSONObject response = controller.login(login, session);

        assertEquals("登录成功1", response.getString("message"));
        verify(session).setAttribute("login", login);
    }
}
