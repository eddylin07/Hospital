package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;

public class LoginControllerTest {
    @Test
    public void failedLoginDoesNotCreateAuthenticatedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService(false);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setUsername("missing");
        login.setPassword("bad");

        JSONObject response = controller.login(login, session);

        Assert.assertEquals("用户名不存在", response.getString("message"));
        Assert.assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresHydratedLoginInSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService(true);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setUsername("doctor");
        login.setPassword("ok");

        JSONObject response = controller.login(login, session);

        Assert.assertEquals("登录成功2", response.getString("message"));
        Assert.assertSame(login, session.getAttribute("login"));
        Assert.assertEquals(Integer.valueOf(10), login.getId());
        Assert.assertEquals(Integer.valueOf(2), login.getRole());
    }

    private static class StubLoginService implements LoginService {
        private final boolean success;

        private StubLoginService(boolean success) {
            this.success = success;
        }

        @Override
        public String login(Login login) {
            if (!success) {
                return "用户名不存在";
            }
            login.setId(10);
            login.setRole(2);
            return "登录成功2";
        }

        @Override
        public List<Login> findAllAdmin(String username) {
            return null;
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
