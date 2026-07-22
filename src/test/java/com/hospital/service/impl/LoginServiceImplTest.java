package com.hospital.service.impl;

import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Login;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class LoginServiceImplTest {
    @Test
    public void publicRegistrationWithBlankCertDoesNotCreateAdmin() {
        LoginServiceImpl service = new LoginServiceImpl();
        AtomicBoolean inserted = new AtomicBoolean(false);
        service.loginMapper = mapper(LoginMapper.class, (proxy, method, args) -> {
            if ("insert".equals(method.getName()) || "insertAdmin".equals(method.getName())) {
                inserted.set(true);
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.doctorMapper = mapper(DoctorMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));

        Login login = new Login();
        login.setUsername("attacker");
        login.setPassword("pw");
        login.setCertId(" ");

        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertFalse(inserted.get());
        assertNull(login.getRole());
    }

    @SuppressWarnings("unchecked")
    private static <T> T mapper(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler);
    }

    private static Object defaultValue(Class<?> returnType) {
        if (!returnType.isPrimitive()) {
            return null;
        }
        if (boolean.class.equals(returnType)) {
            return false;
        }
        if (void.class.equals(returnType)) {
            return null;
        }
        return 0;
    }
}
