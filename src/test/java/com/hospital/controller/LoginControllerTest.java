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

public class LoginControllerTest {

    @Test
    public void failedLoginDoesNotPersistSubmittedLoginInSession() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService("密码错误", null, null);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        Login submitted = new Login();
        submitted.setUsername("attacker");

        controller.login(submitted, session);

        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginPersistsResolvedIdentityInSession() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService("登录成功3", 12, 3);
        MockHttpSession session = new MockHttpSession();

        Login submitted = new Login();
        submitted.setUsername("patient");

        controller.login(submitted, session);

        assertSame(submitted, session.getAttribute("login"));
        assertEquals(Integer.valueOf(12), submitted.getId());
        assertEquals(Integer.valueOf(3), submitted.getRole());
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
