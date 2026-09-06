package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
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
    public void failedLoginDoesNotLeaveAuthenticatedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService("密码错误");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        JSONObject response = controller.login(new Login(), session);

        assertEquals("密码错误", response.get("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresValidatedPrincipal() {
        Login login = new Login();
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService("登录成功3") {
            @Override
            public String login(Login request) {
                request.setId(5);
                request.setRole(3);
                return super.login(request);
            }
        };
        MockHttpSession session = new MockHttpSession();

        controller.login(login, session);

        assertSame(login, session.getAttribute("login"));
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
