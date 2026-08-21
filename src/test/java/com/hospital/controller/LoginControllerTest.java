package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.TestSupport;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LoginControllerTest {
    @Test
    public void failedLoginDoesNotPersistForgedSessionIdentity() {
        LoginController controller = new LoginController();
        TestSupport.setField(controller, "loginService", new LoginService() {
            @Override
            public String login(Login login) {
                return "密码错误";
            }

            @Override
            public java.util.List<Login> findAllAdmin(String username) {
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
            public String regist(Login login) {
                return "";
            }
        });

        Login forged = new Login();
        forged.setId(99);
        forged.setRole(1);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", forged);

        JSONObject result = controller.login(forged, session);

        assertEquals("密码错误", result.getString("message"));
        assertNull(session.getAttribute("login"));
    }
}
