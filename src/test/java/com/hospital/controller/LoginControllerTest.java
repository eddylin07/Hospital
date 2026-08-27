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
    public void failedLoginDoesNotStoreAttackerSuppliedSessionIdentity() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService("密码错误", false);
        MockHttpSession session = new MockHttpSession();

        Login login = new Login();
        login.setUsername("admin");
        login.setPassword("bad-password");
        login.setId(1);
        login.setRole(1);

        JSONObject response = controller.login(login, session);

        assertEquals("密码错误", response.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresResolvedSessionIdentity() {
        LoginController controller = new LoginController();
        controller.loginService = new FakeLoginService("登录成功1", true);
        MockHttpSession session = new MockHttpSession();

        Login login = new Login();
        login.setUsername("admin");
        login.setPassword("correct-password");

        controller.login(login, session);

        assertEquals(Integer.valueOf(1), login.getId());
        assertEquals(Integer.valueOf(1), login.getRole());
        assertSame(login, session.getAttribute("login"));
    }

    private static class FakeLoginService implements LoginService {
        private final String message;
        private final boolean success;

        FakeLoginService(String message, boolean success) {
            this.message = message;
            this.success = success;
        }

        @Override
        public List<Login> findAllAdmin(String username) {
            return Collections.emptyList();
        }

        @Override
        public String addAmin(Login login) {
            return "";
        }

        @Override
        public String updateAdmin(Login login) {
            return "";
        }

        @Override
        public String delAdmin(Integer id) {
            return "";
        }

        @Override
        public Login getAdmin(Integer id) {
            return null;
        }

        @Override
        public String login(Login login) {
            if (success) {
                login.setId(1);
                login.setRole(1);
            }
            return message;
        }

        @Override
        public String regist(Login login) {
            return "";
        }
    }
}
