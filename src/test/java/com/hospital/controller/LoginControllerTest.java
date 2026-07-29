package com.hospital.controller;

import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class LoginControllerTest {
    @Test
    public void failedLoginRemovesExistingSession() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService(false);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        Login attempt = new Login();
        controller.login(attempt, session);

        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresTrustedLoginInSession() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService(true);
        MockHttpSession session = new MockHttpSession();

        Login attempt = new Login();
        controller.login(attempt, session);

        assertSame(attempt, session.getAttribute("login"));
    }

    private static class FakeLoginService implements LoginService {
        private final boolean success;

        private FakeLoginService(boolean success) {
            this.success = success;
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
            if (success) {
                login.setId(1);
                login.setRole(1);
                return "登录成功1";
            }
            login.setId(null);
            login.setRole(null);
            return "密码错误";
        }

        @Override
        public String regist(Login login) {
            return null;
        }
    }
}
