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
    public void failedLoginDoesNotAuthenticateSession() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService(false);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        JSONObject response = controller.login(new Login(), session);

        assertEquals("bad", response.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresPopulatedLogin() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService(true);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();

        JSONObject response = controller.login(login, session);

        assertEquals("ok", response.getString("message"));
        assertSame(login, session.getAttribute("login"));
        assertEquals(Integer.valueOf(7), login.getId());
        assertEquals(Integer.valueOf(3), login.getRole());
    }

    private static class FakeLoginService implements LoginService {
        private final boolean success;

        private FakeLoginService(boolean success) {
            this.success = success;
        }

        @Override
        public String login(Login login) {
            if (success) {
                login.setId(7);
                login.setRole(3);
                return "ok";
            }
            return "bad";
        }

        @Override public List<Login> findAllAdmin(String username) { return Collections.emptyList(); }
        @Override public String addAmin(Login login) { return null; }
        @Override public String updateAdmin(Login login) { return null; }
        @Override public String delAdmin(Integer id) { return null; }
        @Override public Login getAdmin(Integer id) { return null; }
        @Override public String regist(Login login) { return null; }
    }
}
