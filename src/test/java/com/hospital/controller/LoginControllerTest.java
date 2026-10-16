package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class LoginControllerTest {

    @Test
    public void failedLoginDoesNotCreateSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService(false);
        Login login = new Login();
        login.setUsername("missing");
        MockHttpSession session = new MockHttpSession();

        JSONObject result = controller.login(login, session);

        assertEquals("用户名不存在", result.get("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresResolvedAccountInSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService(true);
        Login login = new Login();
        MockHttpSession session = new MockHttpSession();

        JSONObject result = controller.login(login, session);

        assertEquals("登录成功1", result.get("message"));
        assertSame(login, session.getAttribute("login"));
        assertEquals(Integer.valueOf(7), login.getId());
        assertEquals(Integer.valueOf(1), login.getRole());
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
            login.setId(7);
            login.setRole(1);
            return "登录成功1";
        }

        @Override public List<Login> findAllAdmin(String username) { return null; }
        @Override public String addAmin(Login login) { return null; }
        @Override public String updateAdmin(Login login) { return null; }
        @Override public String delAdmin(Integer id) { return null; }
        @Override public Login getAdmin(Integer id) { return null; }
        @Override public String regist(Login login) { return null; }
    }
}
