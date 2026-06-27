package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginControllerTest {
    @Test
    public void loginStoresSessionOnlyWhenAuthenticationSucceeds() {
        LoginService loginService = mock(LoginService.class);
        when(loginService.login(any(Login.class))).thenAnswer(invocation -> {
            Login login = invocation.getArgument(0);
            login.setId(7);
            login.setRole(2);
            return "登录成功2";
        });
        LoginController controller = new LoginController();
        ReflectionTestUtils.setField(controller, "loginService", loginService);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();

        JSONObject response = controller.login(login, session);

        assertEquals("登录成功2", response.get("message"));
        assertSame(login, session.getAttribute("login"));
        assertEquals(Integer.valueOf(2), ((Login) session.getAttribute("login")).getRole());
    }

    @Test
    public void loginClearsSessionWhenAuthenticationFails() {
        LoginService loginService = mock(LoginService.class);
        when(loginService.login(any(Login.class))).thenReturn("密码错误");
        LoginController controller = new LoginController();
        ReflectionTestUtils.setField(controller, "loginService", loginService);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        JSONObject response = controller.login(new Login(), session);

        assertEquals("密码错误", response.get("message"));
        assertNull(session.getAttribute("login"));
    }
}
