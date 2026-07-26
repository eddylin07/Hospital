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
    public void failedLoginDoesNotCreateSession() {
        LoginController controller=new LoginController();
        controller.loginService=new FakeLoginService("密码错误",false);
        MockHttpSession session=new MockHttpSession();
        session.setAttribute("login",new Login());

        JSONObject json=controller.login(new Login(),session);

        assertEquals("密码错误",json.get("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresAuthenticatedPrincipal() {
        LoginController controller=new LoginController();
        controller.loginService=new FakeLoginService("登录成功2",true);
        MockHttpSession session=new MockHttpSession();
        Login login=new Login();

        JSONObject json=controller.login(login,session);

        assertEquals("登录成功2",json.get("message"));
        assertSame(login,session.getAttribute("login"));
        assertEquals(Integer.valueOf(12),login.getId());
        assertEquals(Integer.valueOf(2),login.getRole());
    }

    private static class FakeLoginService implements LoginService {
        private final String message;
        private final boolean success;

        private FakeLoginService(String message,boolean success) {
            this.message=message;
            this.success=success;
        }

        @Override
        public String login(Login login) {
            if(success){
                login.setId(12);
                login.setRole(2);
            }
            return message;
        }

        @Override public List<Login> findAllAdmin(String username) { throw new UnsupportedOperationException(); }
        @Override public String addAmin(Login login) { throw new UnsupportedOperationException(); }
        @Override public String updateAdmin(Login login) { throw new UnsupportedOperationException(); }
        @Override public String delAdmin(Integer id) { throw new UnsupportedOperationException(); }
        @Override public Login getAdmin(Integer id) { throw new UnsupportedOperationException(); }
        @Override public String regist(Login login) { throw new UnsupportedOperationException(); }
    }
}
