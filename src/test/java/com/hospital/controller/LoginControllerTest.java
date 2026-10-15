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
    public void failedLoginDoesNotStoreClientSuppliedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService("密码错误");
        MockHttpSession session = new MockHttpSession();
        Login forged = new Login();
        forged.setUsername("admin");
        forged.setPassword("bad");
        forged.setId(1);
        forged.setRole(1);

        JSONObject result = controller.login(forged, session);

        assertEquals("密码错误", result.get("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresServerResolvedLogin() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService("登录成功1") {
            @Override
            public String login(Login login) {
                login.setId(10);
                login.setRole(1);
                return super.login(login);
            }
        };
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();

        controller.login(login, session);

        assertSame(login, session.getAttribute("login"));
        assertEquals(Integer.valueOf(10), login.getId());
        assertEquals(Integer.valueOf(1), login.getRole());
    }

    private static class StubLoginService implements LoginService {
        private final String loginMessage;

        private StubLoginService(String loginMessage) {
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
