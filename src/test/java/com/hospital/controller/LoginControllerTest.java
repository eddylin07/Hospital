package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.TestProxy;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class LoginControllerTest {
    @Test
    public void failedLoginDoesNotStoreRequestBodyInSession() {
        LoginController controller = new LoginController();
        controller.loginService = TestProxy.of(LoginService.class, (proxy, method, args) -> {
            if ("login".equals(method.getName())) {
                return "密码错误";
            }
            throw new AssertionError("Unexpected method: " + method.getName());
        });
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        JSONObject result = controller.login(new Login(), session);

        assertEquals("密码错误", result.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedLoginInSession() {
        LoginController controller = new LoginController();
        Login login = new Login();
        controller.loginService = TestProxy.of(LoginService.class, (proxy, method, args) -> {
            if ("login".equals(method.getName())) {
                Login authenticated = (Login) args[0];
                authenticated.setId(7);
                authenticated.setRole(3);
                return "登录成功3";
            }
            throw new AssertionError("Unexpected method: " + method.getName());
        });
        MockHttpSession session = new MockHttpSession();

        JSONObject result = controller.login(login, session);

        assertEquals("登录成功3", result.getString("message"));
        assertSame(login, session.getAttribute("login"));
    }
}
