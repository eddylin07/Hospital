package com.hospital.service.impl;

import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Login;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LoginServiceImplTest {
    @Test
    public void publicRegistrationWithBlankCertIdDoesNotCreateAdminAccount() {
        LoginServiceImpl service = new LoginServiceImpl();
        AtomicInteger inserts = new AtomicInteger();
        service.loginMapper = mapper(LoginMapper.class, (proxy, method, args) -> {
            if ("findByUsername".equals(method.getName())) {
                return null;
            }
            if ("insert".equals(method.getName()) || "insertAdmin".equals(method.getName())) {
                inserts.incrementAndGet();
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.doctorMapper = mapper(DoctorMapper.class, (proxy, method, args) -> null);
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> null);
        Login login = new Login();
        login.setUsername("attacker");
        login.setPassword("pw");
        login.setCertId(" ");

        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertEquals(0, inserts.get());
        assertNull(login.getRole());
    }

    @SuppressWarnings("unchecked")
    private <T> T mapper(Class<T> type, java.lang.reflect.InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    private Object defaultValue(Class<?> type) {
        if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
            return 0;
        }
        return null;
    }
}
