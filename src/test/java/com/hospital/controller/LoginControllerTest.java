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
    public void failedLoginDoesNotCreateAuthenticatedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService("\u5bc6\u7801\u9519\u8bef", null, null);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        JSONObject result = controller.login(new Login(), session);

        assertEquals("\u5bc6\u7801\u9519\u8bef", result.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService("\u767b\u5f55\u6210\u529f1", 5, 1);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();

        JSONObject result = controller.login(login, session);

        assertEquals("\u767b\u5f55\u6210\u529f1", result.getString("message"));
        assertSame(login, session.getAttribute("login"));
        assertEquals(Integer.valueOf(5), login.getId());
        assertEquals(Integer.valueOf(1), login.getRole());
    }

    private static class StubLoginService implements LoginService {
        private final String message;
        private final Integer id;
        private final Integer role;

        private StubLoginService(String message, Integer id, Integer role) {
            this.message = message;
            this.id = id;
            this.role = role;
        }

        @Override
        public String login(Login login) {
            login.setId(id);
            login.setRole(role);
            return message;
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
        public String regist(Login login) {
            return null;
        }
    }
}
