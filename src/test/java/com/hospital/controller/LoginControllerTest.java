package com.hospital.controller;

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
    public void failedLoginClearsSessionInsteadOfAuthenticatingRequest() {
        LoginController controller=new LoginController();
        controller.loginService=new FixedLoginService("密码错误");
        MockHttpSession session=new MockHttpSession();
        session.setAttribute("login",new Login());
        Login login=new Login();
        login.setUsername("attacker");
        login.setPassword("bad");

        assertEquals("密码错误",controller.login(login,session).get("message"));

        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedIdentity() {
        LoginController controller=new LoginController();
        controller.loginService=new FixedLoginService("登录成功3",7,3);
        MockHttpSession session=new MockHttpSession();
        Login login=new Login();

        assertEquals("登录成功3",controller.login(login,session).get("message"));

        assertSame(login,session.getAttribute("login"));
    }

    private static class FixedLoginService implements LoginService {
        private final String message;
        private final Integer id;
        private final Integer role;

        private FixedLoginService(String message) {
            this(message,null,null);
        }

        private FixedLoginService(String message,Integer id,Integer role) {
            this.message=message;
            this.id=id;
            this.role=role;
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
            login.setId(id);
            login.setRole(role);
            return message;
        }

        @Override
        public String regist(Login login) {
            return "";
        }
    }
}
