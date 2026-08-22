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
    public void failedLoginDoesNotCreateSessionEvenWithForgedRole() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService("密码错误");
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(1);
        login.setRole(1);

        controller.login(login, session);

        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresHydratedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService("登录成功3");
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(5);
        login.setRole(3);

        controller.login(login, session);

        assertSame(login, session.getAttribute("login"));
        assertEquals(3, ((Login) session.getAttribute("login")).getRole().intValue());
    }

    private static class FakeLoginService implements LoginService {
        private final String message;

        private FakeLoginService(String message) {
            this.message = message;
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
            return message;
        }

        @Override
        public String regist(Login login) {
            return null;
        }
    }
}
