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
        controller.loginService = new FakeLoginService(false);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        Login login = new Login();
        JSONObject response = controller.login(login, session);

        assertEquals("密码错误", response.get("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService(true);
        MockHttpSession session = new MockHttpSession();

        Login login = new Login();
        JSONObject response = controller.login(login, session);

        assertEquals("登录成功3", response.get("message"));
        assertSame(login, session.getAttribute("login"));
        assertEquals(Integer.valueOf(10), login.getId());
        assertEquals(Integer.valueOf(3), login.getRole());
    }

    private static class FakeLoginService implements LoginService {
        private final boolean succeed;

        private FakeLoginService(boolean succeed) {
            this.succeed = succeed;
        }

        @Override
        public String login(Login login) {
            if (!succeed) {
                return "密码错误";
            }
            login.setId(10);
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
