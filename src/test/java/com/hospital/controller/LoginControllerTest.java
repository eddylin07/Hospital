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
    public void failedLoginDoesNotCreateSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService("密码错误");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        JSONObject response = controller.login(new Login(), session);

        assertEquals("密码错误", response.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService("登录成功3") {
            @Override
            public String login(Login login) {
                login.setId(7);
                login.setRole(3);
                return super.login(login);
            }
        };
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();

        JSONObject response = controller.login(login, session);

        assertEquals("登录成功3", response.getString("message"));
        assertSame(login, session.getAttribute("login"));
    }

    private static class StubLoginService implements LoginService {
        private final String loginMessage;

        private StubLoginService(String loginMessage) {
            this.loginMessage = loginMessage;
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
            return loginMessage;
        }

        @Override
        public String regist(Login login) {
            return null;
        }
    }
}

