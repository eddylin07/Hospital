package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LoginControllerTest {
    private LoginController controller;
    private StubLoginService loginService;
    private MockHttpSession session;

    @Before
    public void setUp() {
        controller=new LoginController();
        loginService=new StubLoginService();
        controller.loginService=loginService;
        session=new MockHttpSession();
    }

    @Test
    public void failedLoginDoesNotCreateAuthenticatedSession() {
        Login existingLogin=new Login();
        existingLogin.setId(1);
        existingLogin.setUsername("existing");
        session.setAttribute("login",existingLogin);

        Login submittedLogin=new Login();
        submittedLogin.setUsername("admin");
        submittedLogin.setPassword("wrong-password");
        submittedLogin.setId(1);
        submittedLogin.setRole(1);
        loginService.message="密码错误";

        JSONObject response=controller.login(submittedLogin,session);

        assertEquals("密码错误",response.getString("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresSanitizedAuthenticatedSession() {
        Login submittedLogin=new Login();
        submittedLogin.setUsername("doctor");
        submittedLogin.setPassword("correct-password");
        loginService.message="登录成功2";
        loginService.authenticated=true;

        JSONObject response=controller.login(submittedLogin,session);

        Login sessionLogin=(Login) session.getAttribute("login");
        assertEquals("登录成功2",response.getString("message"));
        assertEquals(Integer.valueOf(2),sessionLogin.getId());
        assertEquals(Integer.valueOf(2),sessionLogin.getRole());
        assertEquals("doctor",sessionLogin.getUsername());
        assertNull(sessionLogin.getPassword());
    }

    private static class StubLoginService implements LoginService {
        private String message;
        private boolean authenticated;

        @Override
        public String login(Login login) {
            if(authenticated){
                login.setId(2);
                login.setRole(2);
            }
            return message;
        }

        @Override
        public List<Login> findAllAdmin(String username) {
            throw new UnsupportedOperationException();
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
        public String regist(Login login) {
            throw new UnsupportedOperationException();
        }
    }
}
