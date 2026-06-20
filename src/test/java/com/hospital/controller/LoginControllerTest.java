package com.hospital.controller;

import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class LoginControllerTest {

    @Test
    public void failedLoginDoesNotCreateAuthenticatedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService("用户名不存在");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        Login request = new Login();
        request.setUsername("missing");
        request.setPassword("bad");
        request.setId(1);
        request.setRole(1);

        controller.login(request, session);

        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginCreatesAuthenticatedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService("登录成功1");
        MockHttpSession session = new MockHttpSession();
        Login request = new Login();

        controller.login(request, session);

        assertSame(request, session.getAttribute("login"));
    }

    private static class StubLoginService implements LoginService {
        private final String loginMessage;

        StubLoginService(String loginMessage) {
            this.loginMessage = loginMessage;
        }

        @Override
        public List<Login> findAllAdmin(String username) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String addAmin(Login login) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String updateAdmin(Login login) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String delAdmin(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Login getAdmin(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String login(Login login) {
            return loginMessage;
        }

        @Override
        public String regist(Login login) {
            throw new UnsupportedOperationException();
        }
    }
}
