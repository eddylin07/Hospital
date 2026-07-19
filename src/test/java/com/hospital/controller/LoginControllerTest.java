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
    public void loginDoesNotStoreFailedLoginInSession() {
        LoginController controller=new LoginController();
        controller.loginService=new StubLoginService("密码错误");
        Login login=new Login();
        MockHttpSession session=new MockHttpSession();

        JSONObject json=controller.login(login,session);

        assertEquals("密码错误",json.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void loginStoresSuccessfulLoginInSession() {
        LoginController controller=new LoginController();
        controller.loginService=new StubLoginService("登录成功1"){
            @Override
            public String login(Login login) {
                login.setId(1);
                login.setRole(1);
                return super.login(login);
            }
        };
        Login login=new Login();
        MockHttpSession session=new MockHttpSession();

        JSONObject json=controller.login(login,session);

        assertEquals("登录成功1",json.getString("message"));
        assertSame(login,session.getAttribute("login"));
    }

    private static class StubLoginService implements LoginService {
        private final String message;

        private StubLoginService(String message) {
            this.message=message;
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
            return message;
        }

        @Override
        public String regist(Login login) {
            return "";
        }
    }
}
