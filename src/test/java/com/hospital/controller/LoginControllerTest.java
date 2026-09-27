package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LoginControllerTest {
    @Test
    public void failedLoginDoesNotCreateAuthenticatedSession() {
        LoginController controller = new LoginController();
        controller.loginService = new LoginService() {
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
                return "用户名不存在";
            }

            @Override
            public String regist(Login login) {
                return null;
            }
        };

        MockHttpSession session = new MockHttpSession();
        JSONObject result = controller.login(new Login(), session);

        assertEquals("用户名不存在", result.getString("message"));
        assertNull(session.getAttribute("login"));
    }
}
