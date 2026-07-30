package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    @Test
    public void failedLoginDoesNotCreateAuthenticatedSession() {
        Login login = new Login();
        MockHttpSession session = new MockHttpSession();
        when(loginService.login(login)).thenReturn("密码错误");

        JSONObject result = loginController.login(login, session);

        assertEquals("密码错误", result.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedUserInSession() {
        Login login = new Login();
        MockHttpSession session = new MockHttpSession();
        when(loginService.login(login)).thenReturn("登录成功3");

        JSONObject result = loginController.login(login, session);

        assertEquals("登录成功3", result.getString("message"));
        assertSame(login, session.getAttribute("login"));
    }
}
