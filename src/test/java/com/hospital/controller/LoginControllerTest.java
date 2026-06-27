package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class LoginControllerTest {
    @Test
    public void loginStoresSessionOnlyWhenAuthenticationSucceeds() {
        LoginService loginService = new FakeLoginService("登录成功2", 2);
        LoginController controller = new LoginController();
        ReflectionTestUtils.setField(controller, "loginService", loginService);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();

        JSONObject response = controller.login(login, session);

        assertEquals("登录成功2", response.get("message"));
        assertSame(login, session.getAttribute("login"));
        assertEquals(Integer.valueOf(2), ((Login) session.getAttribute("login")).getRole());
    }

    @Test
    public void loginClearsSessionWhenAuthenticationFails() {
        LoginService loginService = new FakeLoginService("密码错误", null);
        LoginController controller = new LoginController();
        ReflectionTestUtils.setField(controller, "loginService", loginService);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        JSONObject response = controller.login(new Login(), session);

        assertEquals("密码错误", response.get("message"));
        assertNull(session.getAttribute("login"));
    }

    private static class FakeLoginService implements LoginService {
        private final String loginMessage;
        private final Integer role;

        FakeLoginService(String loginMessage, Integer role) {
            this.loginMessage = loginMessage;
            this.role = role;
        }

        @Override
        public String login(Login login) {
            if (role != null) {
                login.setId(7);
                login.setRole(role);
            }
            return loginMessage;
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
