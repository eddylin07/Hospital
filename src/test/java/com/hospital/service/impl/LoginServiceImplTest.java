package com.hospital.service.impl;

import com.hospital.dao.LoginMapper;
import com.hospital.entity.Login;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LoginServiceImplTest {

    @Test
    public void failedLoginClearsClientSuppliedIdentity() {
        Login stored = new Login();
        stored.setId(7);
        stored.setUsername("doctor");
        stored.setPassword("correct");
        stored.setRole(2);

        LoginServiceImpl service = new LoginServiceImpl();
        ReflectionTestUtils.setField(service, "loginMapper", loginMapper(stored, new AtomicInteger()));

        Login request = new Login();
        request.setUsername("doctor");
        request.setPassword("wrong");
        request.setId(999);
        request.setRole(1);

        assertEquals("密码错误", service.login(request));
        assertNull(request.getId());
        assertNull(request.getRole());
    }

    @Test
    public void blankCertRegistrationDoesNotCreateAdmin() {
        AtomicInteger inserts = new AtomicInteger();
        LoginServiceImpl service = new LoginServiceImpl();
        ReflectionTestUtils.setField(service, "loginMapper", loginMapper(null, inserts));

        Login request = new Login();
        request.setUsername("attacker");
        request.setPassword("pw");
        request.setCertId(" ");

        assertEquals("该证件信息未入库，不能注册该医生或者患者", service.regist(request));
        assertEquals(0, inserts.get());
        assertNull(request.getRole());
    }

    private LoginMapper loginMapper(final Login stored, final AtomicInteger inserts) {
        return (LoginMapper) Proxy.newProxyInstance(
                LoginMapper.class.getClassLoader(),
                new Class[]{LoginMapper.class},
                (proxy, method, args) -> {
                    if ("findByUsername".equals(method.getName())) {
                        return stored;
                    }
                    if ("insert".equals(method.getName()) || "insertAdmin".equals(method.getName())) {
                        inserts.incrementAndGet();
                        return 1;
                    }
                    return defaultValue(method.getReturnType());
                });
    }

    private Object defaultValue(Class<?> returnType) {
        if (returnType == Integer.TYPE) {
            return 0;
        }
        return null;
    }
}
