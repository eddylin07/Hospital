package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController controller;

    @Test
    public void failedLoginDoesNotCreateAuthenticatedSession() {
        Login login = new Login();
        MockHttpSession session = new MockHttpSession();

        when(loginService.login(login)).thenReturn("登录失败");

        JSONObject response = controller.login(login, session);

        assertThat(response.getString("message")).isEqualTo("登录失败");
        assertThat(session.getAttribute("login")).isNull();
    }

    @Test
    public void successfulLoginStoresAuthenticatedSession() {
        Login login = new Login();
        MockHttpSession session = new MockHttpSession();

        when(loginService.login(login)).thenReturn("登录成功，欢迎使用");

        JSONObject response = controller.login(login, session);

        assertThat(response.getString("message")).isEqualTo("登录成功，欢迎使用");
        assertThat(session.getAttribute("login")).isSameAs(login);
    }
}
