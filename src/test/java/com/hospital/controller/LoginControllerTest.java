package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Login;
import com.hospital.service.LoginService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginControllerTest {

    private LoginController controller;
    private LoginService loginService;

    @Before
    public void setUp() {
        controller = new LoginController();
        loginService = mock(LoginService.class);
        ReflectionTestUtils.setField(controller, "loginService", loginService);
    }

    @Test
    public void loginReturnsServiceMessageAndStoresAuthenticatedUserInSession() {
        Login login = new Login();
        login.setUsername("doctor");
        MockHttpSession session = new MockHttpSession();
        when(loginService.login(login)).thenReturn("登录成功");

        JSONObject response = controller.login(login, session);

        assertEquals("登录成功", response.get("message"));
        assertSame(login, session.getAttribute("login"));
        verify(loginService).login(login);
    }

    @Test
    public void loginoutClearsLoginSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", new Login());

        String view = controller.loginout(session);

        assertEquals("/hospital", view);
        assertEquals(null, session.getAttribute("login"));
    }
}
