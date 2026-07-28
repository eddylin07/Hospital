package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class LoginControllerTest {
    @Test
    public void failedLoginDoesNotCreateAuthenticatedSession() {
        LoginController controller = new LoginController();
        controller.loginService = loginService(false);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setUsername("missing");

        JSONObject response = controller.login(login, session);

        assertEquals("用户名不存在", response.get("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresPopulatedLoginInSession() {
        LoginController controller = new LoginController();
        controller.loginService = loginService(true);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setUsername("admin");

        controller.login(login, session);

        assertSame(login, session.getAttribute("login"));
        assertEquals(Integer.valueOf(1), login.getId());
        assertEquals(Integer.valueOf(1), login.getRole());
    }

    private LoginService loginService(boolean success) {
        return (LoginService) Proxy.newProxyInstance(
                LoginService.class.getClassLoader(),
                new Class[]{LoginService.class},
                (proxy, method, args) -> {
                    if ("login".equals(method.getName())) {
                        Login login = (Login) args[0];
                        if (success) {
                            login.setId(1);
                            login.setRole(1);
                            return "登录成功1";
                        }
                        return "用户名不存在";
                    }
                    return null;
                });
    }
}
