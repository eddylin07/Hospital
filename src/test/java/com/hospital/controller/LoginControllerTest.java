package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.Collections;
import java.util.List;

public class LoginControllerTest {

    @Test
    public void failedLoginDoesNotPersistForgedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService("密码错误");
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(1);
        login.setRole(1);

        JSONObject response = controller.login(login, session);

        Assert.assertEquals("密码错误", response.getString("message"));
        Assert.assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginPersistsHydratedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService("登录成功3");
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(5);
        login.setRole(3);

        controller.login(login, session);

        Assert.assertSame(login, session.getAttribute("login"));
    }

    private static class StubLoginService implements LoginService {
        private final String message;

        private StubLoginService(String message) {
            this.message = message;
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
        public String login(Login login) {
            return message;
        }

        @Override
        public String regist(Login login) {
            return null;
        }
    }
}
