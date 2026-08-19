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
        controller.loginService = mock(LoginService.class);
        Login login = new Login();
        MockHttpSession session = new MockHttpSession();

        when(controller.loginService.login(login)).thenReturn("密码错误");

        JSONObject result = controller.login(login, session);

        assertEquals("密码错误", result.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedUserInSession() {
        LoginController controller = new LoginController();
        controller.loginService = mock(LoginService.class);
        Login login = new Login();
        MockHttpSession session = new MockHttpSession();

        when(controller.loginService.login(login)).thenReturn("登录成功3");

        JSONObject result = controller.login(login, session);

        assertEquals("登录成功3", result.getString("message"));
        assertSame(login, session.getAttribute("login"));
    }
}
