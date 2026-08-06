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
    public void failedLoginDoesNotLeaveAuthenticatedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService(false);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        JSONObject response = controller.login(new Login(), session);

        assertEquals("密码错误", response.get("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresResolvedIdentityInSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService(true);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();

        controller.login(login, session);

        assertEquals(Integer.valueOf(42), login.getId());
        assertEquals(Integer.valueOf(3), login.getRole());
        assertSame(login, session.getAttribute("login"));
    }

    private static class StubLoginService implements LoginService {
        private final boolean success;

        private StubLoginService(boolean success) {
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
        public String regist(Login login) {
            return null;
        }
    }
}
