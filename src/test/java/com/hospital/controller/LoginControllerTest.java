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
    public void failedLoginDoesNotPersistForgedSessionIdentity() {
        LoginController controller = new LoginController();
        controller.loginService = (LoginOnlyService) login -> "用户名不存在";

        Login forged = new Login();
        forged.setUsername("missing");
        forged.setPassword("bad");
        forged.setId(1);
        forged.setRole(1);
        MockHttpSession session = new MockHttpSession();

        JSONObject json = controller.login(forged, session);

        assertEquals("用户名不存在", json.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresServerHydratedIdentity() {
        LoginController controller = new LoginController();
        controller.loginService = (LoginOnlyService) login -> {
            login.setId(7);
            login.setRole(3);
            return "登录成功3";
        };

        Login login = new Login();
        MockHttpSession session = new MockHttpSession();

        controller.login(login, session);

        assertSame(login, session.getAttribute("login"));
        assertEquals(Integer.valueOf(7), login.getId());
        assertEquals(Integer.valueOf(3), login.getRole());
    }

    private interface LoginOnlyService extends LoginService {
        @Override
        default List<Login> findAllAdmin(String username) {
            throw new UnsupportedOperationException();
        }

        @Override
        default String addAmin(Login login) {
            throw new UnsupportedOperationException();
        }

        @Override
        default String updateAdmin(Login login) {
            throw new UnsupportedOperationException();
        }

        @Override
        default String delAdmin(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        default Login getAdmin(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        default String regist(Login login) {
            throw new UnsupportedOperationException();
        }
    }
}
