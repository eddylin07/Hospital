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
    public void publicRegistrationWithoutKnownCertDoesNotCreateAdminAccount() {
        AtomicBoolean insertedLogin = new AtomicBoolean(false);
        LoginServiceImpl service = new LoginServiceImpl();
        service.loginMapper = mapper(LoginMapper.class, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
                if ("insert".equals(method.getName()) || "insertAdmin".equals(method.getName())) {
                    insertedLogin.set(true);
                    return 1;
                }
                return defaultValue(method.getReturnType());
            }
        });
        service.doctorMapper = mapper(DoctorMapper.class, returnsDefaults());
        service.patientMapper = mapper(PatientMapper.class, returnsDefaults());

        Login login = new Login();
        login.setUsername("attacker");
        login.setPassword("pw");
        login.setCertId("");

        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertFalse(insertedLogin.get());
        assertNull(login.getRole());
    }

    private static InvocationHandler returnsDefaults() {
        return new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
                return defaultValue(method.getReturnType());
            }
        };
    }

    private static <T> T mapper(Class<T> type, InvocationHandler handler) {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler));
    }

    private static Object defaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }
        if (boolean.class.equals(type)) {
            return false;
        }
        if (void.class.equals(type)) {
            return null;
        }
        return 0;
    }
}
