package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {
    @Mock
    private LoginService loginService;
    @Mock
    private HttpSession session;
    @InjectMocks
    private LoginController controller;

    @Test
    public void loginDoesNotStoreSessionWhenAuthenticationFails() {
        Login login = new Login();
        login.setUsername("patient");
        login.setPassword("wrong-password");
        when(loginService.login(login)).thenReturn("密码错误");

        JSONObject response = controller.login(login, session);

        assertEquals("密码错误", response.getString("message"));
        verify(session, never()).setAttribute(anyString(), any());
    }

    @Test
    public void loginStoresAuthenticatedLoginWhenServiceReportsSuccess() {
        Login login = new Login();
        login.setUsername("patient");
        login.setPassword("correct-password");
        when(loginService.login(login)).thenAnswer(invocation -> {
            login.setId(7);
            login.setRole(3);
            return "登录成功3";
        });

        JSONObject response = controller.login(login, session);

        assertEquals("登录成功3", response.getString("message"));
        assertEquals(Integer.valueOf(7), login.getId());
        assertEquals(Integer.valueOf(3), login.getRole());
        verify(session).setAttribute("login", login);
    }
}
