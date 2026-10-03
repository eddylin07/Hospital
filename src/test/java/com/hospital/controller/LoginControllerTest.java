package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginControllerTest {
    private LoginController controller;
    private LoginService loginService;

    @Before
    public void setUp() {
        controller = new LoginController();
        loginService = mock(LoginService.class);
        controller.loginService = loginService;
    }

    @Test
    public void failedLoginDoesNotStoreSessionLogin() {
        Login login = new Login();
        MockHttpSession session = new MockHttpSession();
        when(loginService.login(login)).thenReturn("密码错误");

        JSONObject json = controller.login(login, session);

        assertEquals("密码错误", json.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedLoginInSession() {
        Login login = new Login();
        MockHttpSession session = new MockHttpSession();
        when(loginService.login(login)).thenReturn("登录成功3");

        JSONObject json = controller.login(login, session);

        assertEquals("登录成功3", json.getString("message"));
        assertSame(login, session.getAttribute("login"));
    }
}
