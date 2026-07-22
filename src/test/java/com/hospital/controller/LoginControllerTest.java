package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class LoginControllerTest {
    @Test
    public void failedLoginDoesNotCreateAuthenticatedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService(false);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        Login login = new Login();
        JSONObject result = controller.login(login, session);

        assertEquals("密码错误", result.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedUser() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService(true);
        MockHttpSession session = new MockHttpSession();

        Login login = new Login();
        JSONObject result = controller.login(login, session);

        assertEquals("登录成功3", result.getString("message"));
        assertSame(login, session.getAttribute("login"));
        assertEquals(Integer.valueOf(7), login.getId());
        assertEquals(Integer.valueOf(3), login.getRole());
    }

    private static class FakeLoginService implements LoginService {
        private final boolean success;

        private FakeLoginService(boolean success) {
            this.success = success;
        }

        @Override
        public String login(Login login) {
            if (!success) {
                return "密码错误";
            }
            login.setId(7);
            login.setRole(3);
            return "登录成功3";
        }

        @Override
        public List<Login> findAllAdmin(String username) {
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
        public String regist(Login login) {
            return null;
        }
    }
}
