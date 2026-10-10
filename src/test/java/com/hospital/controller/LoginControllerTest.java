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
    public void failedLoginRemovesExistingSessionLogin() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService("密码错误");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", hydratedLogin());

        JSONObject response = controller.login(new Login(), session);

        assertEquals("密码错误", response.get("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresHydratedLogin() {
        LoginController controller = new LoginController();
        Login requestLogin = new Login();
        controller.loginService = new StubLoginService("登录成功3", requestLogin);
        MockHttpSession session = new MockHttpSession();

        controller.login(requestLogin, session);

        assertSame(requestLogin, session.getAttribute("login"));
    }

    private Login hydratedLogin() {
        Login login = new Login();
        login.setId(1);
        login.setRole(1);
        return login;
    }

    private static class StubLoginService implements LoginService {
        private final String message;
        private final Login loginToHydrate;

        StubLoginService(String message) {
            this(message, null);
        }

        StubLoginService(String message, Login loginToHydrate) {
            this.message = message;
            this.loginToHydrate = loginToHydrate;
        }

        @Override
        public String login(Login login) {
            if (loginToHydrate != null) {
                login.setId(3);
                login.setRole(3);
            }
            return message;
        }

        @Override public List<Login> findAllAdmin(String username) { throw new UnsupportedOperationException(); }
        @Override public String addAmin(Login login) { throw new UnsupportedOperationException(); }
        @Override public String updateAdmin(Login login) { throw new UnsupportedOperationException(); }
        @Override public String delAdmin(Integer id) { throw new UnsupportedOperationException(); }
        @Override public Login getAdmin(Integer id) { throw new UnsupportedOperationException(); }
        @Override public String regist(Login login) { throw new UnsupportedOperationException(); }
    }
}
