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
    public void publicRegistrationWithBlankCertIdDoesNotCreateAdminLogin() {
        AtomicInteger insertCalls = new AtomicInteger();
        LoginServiceImpl service = new LoginServiceImpl();
        service.loginMapper = mapper(LoginMapper.class, (proxy, method, args) -> {
            if ("findByUsername".equals(method.getName())) {
                return null;
            }
            if ("insert".equals(method.getName()) || "insertAdmin".equals(method.getName())) {
                insertCalls.incrementAndGet();
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.doctorMapper = mapper(DoctorMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));

        Login login = new Login();
        login.setUsername("new-user");
        login.setCertId(" ");

        service.regist(login);

        assertEquals(0, insertCalls.get());
        assertNull(login.getRole());
    }

    private static <T> T mapper(Class<T> mapperClass, InvocationHandler handler) {
        return mapperClass.cast(Proxy.newProxyInstance(
                mapperClass.getClassLoader(),
                new Class[]{mapperClass},
                handler));
    }

    private static Object defaultValue(Class<?> returnType) {
        if (!returnType.isPrimitive()) {
            return null;
        }
        if (Boolean.TYPE.equals(returnType)) {
            return false;
        }
        return 0;
    }
}
