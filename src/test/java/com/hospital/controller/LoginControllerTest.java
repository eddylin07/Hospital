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
    public void failedLoginDoesNotLeaveLoginInSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService(false);
        MockHttpSession session = new MockHttpSession();

        controller.login(new Login(), session);

        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresResolvedLoginInSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService(true);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();

        controller.login(login, session);

        assertSame(login, session.getAttribute("login"));
    }

    private static class StubLoginService implements LoginService {
        private final boolean success;

        private StubLoginService(boolean success) {
            this.success = success;
        }

        @Override
        public String login(Login login) {
            if (success) {
                login.setId(1);
                login.setRole(1);
                return "登录成功1";
            }
            return "密码错误";
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
