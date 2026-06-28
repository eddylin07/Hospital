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
        controller.loginService = new StubLoginService("密码错误", null);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        JSONObject response = controller.login(new Login(), session);

        assertEquals("密码错误", response.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresServerPopulatedLogin() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService("登录成功1", new LoginMutator() {
            @Override
            public void mutate(Login login) {
                login.setId(7);
                login.setRole(1);
            }
        });
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();

        JSONObject response = controller.login(login, session);

        assertEquals("登录成功1", response.getString("message"));
        assertSame(login, session.getAttribute("login"));
        assertEquals(Integer.valueOf(7), login.getId());
        assertEquals(Integer.valueOf(1), login.getRole());
    }

    private interface LoginMutator {
        void mutate(Login login);
    }

    private static class StubLoginService implements LoginService {
        private final String message;
        private final LoginMutator mutator;

        private StubLoginService(String message, LoginMutator mutator) {
            this.message = message;
            this.mutator = mutator;
        }

        @Override
        public String login(Login login) {
            if (mutator != null) {
                mutator.mutate(login);
            }
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
