package com.hospital.service.impl;

import com.hospital.dao.LoginMapper;
import com.hospital.entity.Login;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceImplTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private LoginMapper loginMapper;

    @Test
    public void loginCopiesIdAndRoleWhenPasswordMatches() {
        Login storedLogin = login(42, "doctor", "secret", 2);
        Login requestLogin = login(null, "doctor", "secret", null);
        when(loginMapper.findByUsername("doctor")).thenReturn(storedLogin);

        String message = loginService.login(requestLogin);

        assertEquals("登录成功2", message);
        assertEquals(Integer.valueOf(42), requestLogin.getId());
        assertEquals(Integer.valueOf(2), requestLogin.getRole());
    }

    @Test
    public void loginRejectsWrongPasswordWithoutAssigningIdentity() {
        Login storedLogin = login(42, "doctor", "secret", 2);
        Login requestLogin = login(null, "doctor", "wrong", null);
        when(loginMapper.findByUsername("doctor")).thenReturn(storedLogin);

        String message = loginService.login(requestLogin);

        assertEquals("密码错误", message);
        assertNull(requestLogin.getId());
        assertNull(requestLogin.getRole());
    }

    @Test
    public void loginRejectsUnknownUsernameWithoutAssigningIdentity() {
        Login requestLogin = login(null, "missing", "secret", null);
        when(loginMapper.findByUsername("missing")).thenReturn(null);

        String message = loginService.login(requestLogin);

        assertEquals("用户名不存在", message);
        assertNull(requestLogin.getId());
        assertNull(requestLogin.getRole());
    }

    private Login login(Integer id, String username, String password, Integer role) {
        Login login = new Login();
        login.setId(id);
        login.setUsername(username);
        login.setPassword(password);
        login.setRole(role);
        return login;
    }
}
