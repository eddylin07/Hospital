package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class LoginControllerTest {
    @Test
    public void failedLoginDoesNotPopulateSession() {
        LoginController controller = new LoginController();
        controller.loginService = loginService("密码错误", false);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        JSONObject json = controller.login(new Login(), session);

        assertEquals("密码错误", json.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresServerPopulatedPrincipal() {
        LoginController controller = new LoginController();
        controller.loginService = loginService("登录成功3", true);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();

        JSONObject json = controller.login(login, session);

        assertEquals("登录成功3", json.getString("message"));
        assertSame(login, session.getAttribute("login"));
        assertEquals(Integer.valueOf(11), login.getId());
        assertEquals(Integer.valueOf(3), login.getRole());
    }

    private LoginService loginService(String message, boolean success) {
        return new LoginService() {
            @Override
            public java.util.List<Login> findAllAdmin(String username) {
                return Collections.emptyList();
            }

            @Override
            public String addAmin(Login login) {
                return null;
            }

            @Override
            public String updateAdmin(Login login) {
                return null;
            }

            @Override
            public String delAdmin(Integer id) {
                return null;
            }

            @Override
            public Login getAdmin(Integer id) {
                return null;
            }

            @Override
            public String login(Login login) {
                if (success) {
                    login.setId(11);
                    login.setRole(3);
                }
                return message;
            }

            @Override
            public String regist(Login login) {
                return null;
            }
        };
    }
}
