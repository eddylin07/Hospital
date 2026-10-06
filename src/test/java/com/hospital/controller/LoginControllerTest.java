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
    public void failedLoginDoesNotPersistRequestBodyInSession() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService("用户名不存在", false);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        Login request = new Login();
        request.setUsername("missing-user");
        request.setPassword("bad-password");
        JSONObject response = controller.login(request, session);

        assertEquals("用户名不存在", response.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginPersistsVerifiedLoginInSession() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService("登录成功3", true);
        MockHttpSession session = new MockHttpSession();

        Login request = new Login();
        JSONObject response = controller.login(request, session);

        assertEquals("登录成功3", response.getString("message"));
        assertSame(request, session.getAttribute("login"));
        assertEquals(Integer.valueOf(7), request.getId());
        assertEquals(Integer.valueOf(3), request.getRole());
    }

    private static class FakeLoginService implements LoginService {
        private final String message;
        private final boolean success;

        FakeLoginService(String message, boolean success) {
            this.message = message;
            this.success = success;
        }

        @Override
        public String login(Login login) {
            if (success) {
                login.setId(7);
                login.setRole(3);
            }
            return message;
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
