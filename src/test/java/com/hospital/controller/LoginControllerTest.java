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
    public void failedLoginDoesNotStoreRequestBodyInSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService("密码错误");
        MockHttpSession session = new MockHttpSession();

        Login login = new Login();
        login.setId(1);
        login.setRole(1);

        controller.login(login, session);

        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulHydratedLoginStoresSession() {
        LoginController controller = new LoginController();
        Login login = new Login();
        controller.loginService = new StubLoginService("登录成功3", login);
        MockHttpSession session = new MockHttpSession();

        controller.login(login, session);

        assertSame(login, session.getAttribute("login"));
    }

    private static class StubLoginService implements LoginService {
        private final String message;
        private final Login hydratedLogin;

        StubLoginService(String message) {
            this(message, null);
        }

        StubLoginService(String message, Login hydratedLogin) {
            this.message = message;
            this.hydratedLogin = hydratedLogin;
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
            if(hydratedLogin!=null){
                login.setId(10);
                login.setRole(3);
            }
            return message;
        }

        @Override
        public String regist(Login login) {
            return null;
        }
    }
}
