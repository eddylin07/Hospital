package com.hospital.service.impl;

import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Login;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LoginServiceImplTest {
    @Test
    public void blankCertIdRegistrationDoesNotCreateAdminAccount() {
        LoginServiceImpl service = new LoginServiceImpl();
        AtomicInteger insertCount = new AtomicInteger();
        service.loginMapper = mapper(LoginMapper.class, (proxy, method, args) -> {
            if ("findByUsername".equals(method.getName())) {
                return null;
            }
            if ("insert".equals(method.getName()) || "insertAdmin".equals(method.getName())) {
                insertCount.incrementAndGet();
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.doctorMapper = mapper(DoctorMapper.class, (proxy, method, args) -> null);
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> null);

        Login login = new Login();
        login.setUsername("new-admin");
        login.setCertId(" ");

        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertNull(login.getRole());
        assertEquals(0, insertCount.get());
    }

    @SuppressWarnings("unchecked")
    private <T> T mapper(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    private Object defaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }
        if (boolean.class.equals(type)) {
            return false;
        }
        return 0;
    }
}
