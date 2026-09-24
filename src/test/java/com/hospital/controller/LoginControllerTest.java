package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    private LoginController controller;
    private MockHttpSession session;

    @Before
    public void setUp() {
        controller = new LoginController();
        controller.loginService = loginService;
        session = new MockHttpSession();
    }

    @Test
    public void failedLoginDoesNotStoreSessionLogin() {
        Login login = new Login();
        login.setUsername("bad-user");
        when(loginService.login(login)).thenReturn("用户名不存在");

        JSONObject result = controller.login(login, session);

        assertEquals("用户名不存在", result.get("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedLoginInSession() {
        Login login = new Login();
        login.setUsername("doctor");
        when(loginService.login(login)).thenReturn("登录成功2");

        JSONObject result = controller.login(login, session);

        assertEquals("登录成功2", result.get("message"));
        assertSame(login, session.getAttribute("login"));
    }
}
