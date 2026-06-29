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
    public void failedLoginClearsSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService(false);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        JSONObject json = controller.login(new Login(), session);

        assertEquals("密码错误", json.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedUser() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService(true);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();

        JSONObject json = controller.login(login, session);

        assertEquals("登录成功1", json.getString("message"));
        assertSame(login, session.getAttribute("login"));
    }

    private static class StubLoginService implements LoginService {
        private final boolean success;

        StubLoginService(boolean success) {
            this.success = success;
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
            if (!success) {
                return "密码错误";
            }
            login.setId(1);
            login.setRole(1);
            return "登录成功1";
        }

        @Override
        public String regist(Login login) {
            throw new UnsupportedOperationException();
        }
    }
}
