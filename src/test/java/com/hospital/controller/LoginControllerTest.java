package com.hospital.controller;

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
        controller.loginService = new FakeLoginService(false);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        Login login = new Login();
        login.setUsername("alice");
        login.setPassword("wrong");
        controller.login(login, session);

        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresResolvedIdentityInSession() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService(true);
        MockHttpSession session = new MockHttpSession();

        Login login = new Login();
        login.setUsername("alice");
        login.setPassword("secret");
        controller.login(login, session);

        assertEquals(Integer.valueOf(7), login.getId());
        assertEquals(Integer.valueOf(3), login.getRole());
        assertSame(login, session.getAttribute("login"));
    }

    private static class FakeLoginService implements LoginService {
        private final boolean success;

        private FakeLoginService(boolean success) {
            this.success = success;
        }

        @Override
        public String login(Login login) {
            if (success) {
                login.setId(7);
                login.setRole(3);
                return "登录成功3";
            }
            return "密码错误";
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
        public String regist(Login login) {
            throw new UnsupportedOperationException();
        }
    }
}
