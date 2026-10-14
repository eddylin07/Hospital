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
        controller.loginService = new FakeLoginService("密码错误", null, null);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        JSONObject response = controller.login(new Login(), session);

        assertEquals("密码错误", response.get("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedUser() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService("登录成功3", 7, 3);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();

        JSONObject response = controller.login(login, session);

        assertEquals("登录成功3", response.get("message"));
        assertSame(login, session.getAttribute("login"));
        assertEquals(Integer.valueOf(7), login.getId());
        assertEquals(Integer.valueOf(3), login.getRole());
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
        public String login(Login login) {
            login.setId(id);
            login.setRole(role);
            return message;
        }

        @Override public List<Login> findAllAdmin(String username) { return null; }
        @Override public String addAmin(Login login) { return null; }
        @Override public String updateAdmin(Login login) { return null; }
        @Override public String delAdmin(Integer id) { return null; }
        @Override public Login getAdmin(Integer id) { return null; }
        @Override public String regist(Login login) { return null; }
    }
}
