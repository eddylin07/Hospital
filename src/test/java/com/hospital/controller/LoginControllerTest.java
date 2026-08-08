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
        controller.loginService = new FakeLoginService(false);
        MockHttpSession session = new MockHttpSession();

        controller.login(new Login(), session);

        assertNull(session.getAttribute("login"));
    }

    @Test
    public void failedLoginClearsExistingAuthenticatedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService(false);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        controller.login(new Login(), session);

        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresPopulatedLoginInSession() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService(true);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();

        controller.login(login, session);

        assertSame(login, session.getAttribute("login"));
    }

    private static class FakeLoginService implements LoginService {
        private final boolean success;

        private FakeLoginService(boolean success) {
            this.success = success;
        }

        @Override
        public String login(Login login) {
            if (!success) {
                return "密码错误";
            }
            login.setId(42);
            login.setRole(3);
            return "登录成功3";
        }

        @Override public List<Login> findAllAdmin(String username) { throw new UnsupportedOperationException(); }
        @Override public String addAmin(Login login) { throw new UnsupportedOperationException(); }
        @Override public String updateAdmin(Login login) { throw new UnsupportedOperationException(); }
        @Override public String delAdmin(Integer id) { throw new UnsupportedOperationException(); }
        @Override public Login getAdmin(Integer id) { throw new UnsupportedOperationException(); }
        @Override public String regist(Login login) { throw new UnsupportedOperationException(); }
    }
}
