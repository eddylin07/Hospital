package com.hospital.controller;

import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class LoginControllerSecurityTest {

    @Test
    public void failedLoginDoesNotCreateAuthenticatedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new FixedLoginService("用户名不存在", null, null);
        Login login = new Login();
        login.setUsername("missing");
        MockHttpSession session = new MockHttpSession();

        controller.login(login, session);

        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresPopulatedLoginInSession() {
        LoginController controller = new LoginController();
        controller.loginService = new FixedLoginService("登录成功3", 7, 3);
        Login login = new Login();
        MockHttpSession session = new MockHttpSession();

        controller.login(login, session);

        assertEquals(Integer.valueOf(7), login.getId());
        assertEquals(Integer.valueOf(3), login.getRole());
        assertSame(login, session.getAttribute("login"));
    }

    private static class FixedLoginService implements LoginService {
        private final String message;
        private final Integer id;
        private final Integer role;

        private FixedLoginService(String message, Integer id, Integer role) {
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
        public String regist(Login login) {
            return "";
        }
    }
}
