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

public class LoginControllerSecurityTest {
    @Test
    public void failedLoginDoesNotStoreForgedSessionIdentity() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService("密码错误");

        Login forgedLogin = new Login();
        forgedLogin.setUsername("patient");
        forgedLogin.setPassword("bad-password");
        forgedLogin.setId(1);
        forgedLogin.setRole(1);

        MockHttpSession session = new MockHttpSession();
        JSONObject response = controller.login(forgedLogin, session);

        assertEquals("密码错误", response.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresHydratedSessionIdentity() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService("登录成功3");

        Login login = new Login();
        MockHttpSession session = new MockHttpSession();
        JSONObject response = controller.login(login, session);

        assertEquals("登录成功3", response.getString("message"));
        assertEquals(login, session.getAttribute("login"));
        assertEquals(Integer.valueOf(99), login.getId());
        assertEquals(Integer.valueOf(3), login.getRole());
    }

    private static class FakeLoginService implements LoginService {
        private final String message;

        FakeLoginService(String message) {
            this.message = message;
        }

        @Override
        public String login(Login login) {
            if (message.startsWith("登录成功")) {
                login.setId(99);
                login.setRole(3);
            }
            return message;
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
