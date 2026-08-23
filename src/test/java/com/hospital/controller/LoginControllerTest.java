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
    public void failedLoginDoesNotCreateSession() {
        Login login = new Login();
        login.setUsername("missing");
        MockHttpSession session = new MockHttpSession();

        when(loginService.login(login)).thenReturn("用户名不存在");

        JSONObject response = loginController.login(login, session);

        assertEquals("用户名不存在", response.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedUserInSession() {
        Login login = new Login();
        login.setUsername("doctor");
        MockHttpSession session = new MockHttpSession();

        when(loginService.login(login)).thenReturn("登录成功2");

        JSONObject response = loginController.login(login, session);

        assertEquals("登录成功2", response.getString("message"));
        assertSame(login, session.getAttribute("login"));
    }
}
