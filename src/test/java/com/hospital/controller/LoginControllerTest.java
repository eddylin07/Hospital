package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.Collections;
import java.util.List;

public class LoginControllerTest {

    @Test
    public void failedLoginDoesNotStoreForgedSessionLogin() {
        LoginController controller = new LoginController();
        controller.loginService = new StaticLoginService("密码错误", false);

        Login login = new Login();
        login.setUsername("doctor");
        login.setPassword("bad-password");
        login.setId(10);
        login.setRole(2);
        MockHttpSession session = new MockHttpSession();

        JSONObject response = controller.login(login, session);

        Assert.assertEquals("密码错误", response.getString("message"));
        Assert.assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresServerHydratedIdentity() {
        LoginController controller = new LoginController();
        controller.loginService = new StaticLoginService("登录成功2", true);

        Login login = new Login();
        login.setUsername("doctor");
        login.setPassword("correct-password");
        MockHttpSession session = new MockHttpSession();

        JSONObject response = controller.login(login, session);

        Assert.assertEquals("登录成功2", response.getString("message"));
        Assert.assertSame(login, session.getAttribute("login"));
        Assert.assertEquals(Integer.valueOf(42), login.getId());
        Assert.assertEquals(Integer.valueOf(2), login.getRole());
    }

    private static class StaticLoginService implements LoginService {
        private final String message;
        private final boolean hydrate;

        private StaticLoginService(String message, boolean hydrate) {
            this.message = message;
            this.hydrate = hydrate;
        }

        @Override
        public List<Login> findAllAdmin(String username) {
            return Collections.emptyList();
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
            if (hydrate) {
                login.setId(42);
                login.setRole(2);
            }
            return message;
        }

        @Override
        public String regist(Login login) {
            throw new UnsupportedOperationException();
        }
    }
}
