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
    public void failedLoginDoesNotCreateAuthenticatedSession() {
        LoginController controller = new LoginController();
        Login attemptedLogin = new Login();
        attemptedLogin.setUsername("admin");
        controller.loginService = new FakeLoginService("密码错误", null, null);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        JSONObject response = controller.login(attemptedLogin, session);

        assertEquals("密码错误", response.get("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresPopulatedLoginInSession() {
        LoginController controller = new LoginController();
        Login attemptedLogin = new Login();
        controller.loginService = new FakeLoginService("登录成功1", 3, 1);
        MockHttpSession session = new MockHttpSession();

        JSONObject response = controller.login(attemptedLogin, session);

        assertEquals("登录成功1", response.get("message"));
        assertSame(attemptedLogin, session.getAttribute("login"));
        assertEquals(Integer.valueOf(3), attemptedLogin.getId());
        assertEquals(Integer.valueOf(1), attemptedLogin.getRole());
    }

    private static class FakeLoginService implements LoginService {
        private final String message;
        private final Integer id;
        private final Integer role;

        private FakeLoginService(String message, Integer id, Integer role) {
            this.message = message;
            this.id = id;
            this.role = role;
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
        public String login(Login login) {
            login.setId(id);
            login.setRole(role);
            return message;
        }

        @Override
        public String regist(Login login) {
            return null;
        }
    }
}
