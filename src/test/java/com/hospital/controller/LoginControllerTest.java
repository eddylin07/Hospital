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
        controller.loginService = new FakeLoginService("密码错误");
        MockHttpSession session = new MockHttpSession();

        JSONObject response = controller.login(new Login(), session);

        assertNull(session.getAttribute("login"));
        assertEquals("密码错误", response.get("message"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService("登录成功3");
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();

        controller.login(login, session);

        assertSame(login, session.getAttribute("login"));
    }

    private static class FakeLoginService implements LoginService {
        private final String loginMessage;

        private FakeLoginService(String loginMessage) {
            this.loginMessage = loginMessage;
        }

        @Override
        public List<Login> findAllAdmin(String username) {
            return Collections.emptyList();
        }

        @Override
        public String addAmin(Login login) {
            return "";
        }

        @Override
        public String updateAdmin(Login login) {
            return "";
        }

        @Override
        public String delAdmin(Integer id) {
            return "";
        }

        @Override
        public Login getAdmin(Integer id) {
            return null;
        }

        @Override
        public String login(Login login) {
            return loginMessage;
        }

        @Override
        public String regist(Login login) {
            return "";
        }
    }
}
