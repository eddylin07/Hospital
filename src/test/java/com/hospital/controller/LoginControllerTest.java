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
    public void failedLoginDoesNotStoreSession() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService() {
            @Override
            public String login(Login login) {
                return "密码错误";
            }
        };
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        JSONObject json = controller.login(new Login(), session);

        assertEquals("密码错误", json.get("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresResolvedLogin() {
        LoginController controller = new LoginController();
        controller.loginService = new StubLoginService() {
            @Override
            public String login(Login login) {
                login.setId(9);
                login.setRole(3);
                return "登录成功3";
            }
        };
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();

        JSONObject json = controller.login(login, session);

        assertEquals("登录成功3", json.get("message"));
        assertSame(login, session.getAttribute("login"));
    }

    private static class StubLoginService implements LoginService {
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
        public String login(Login login) {
            return null;
        }

        @Override
        public String regist(Login login) {
            return null;
        }
    }
}
