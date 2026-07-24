package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

public class LoginControllerTest {
    private LoginController controller;

    @Mock
    private LoginService loginService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new LoginController();
        controller.loginService = loginService;
    }

    @Test
    public void failedLoginDoesNotStoreSessionLogin() {
        Login login = new Login();
        login.setUsername("bad-user");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());
        when(loginService.login(login)).thenReturn("密码错误");

        JSONObject json = controller.login(login, session);

        assertEquals("密码错误", json.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedLoginInSession() {
        Login login = new Login();
        login.setUsername("doctor");
        MockHttpSession session = new MockHttpSession();
        when(loginService.login(login)).thenReturn("登录成功2");

        JSONObject json = controller.login(login, session);

        assertEquals("登录成功2", json.getString("message"));
        assertSame(login, session.getAttribute("login"));
    }
}
