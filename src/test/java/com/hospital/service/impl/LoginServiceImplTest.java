package com.hospital.service.impl;

import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Login;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LoginServiceImplTest {
    @Test
    public void publicRegistrationWithBlankCertificateDoesNotCreateAdmin() {
        LoginServiceImpl service = new LoginServiceImpl();
        CountingLoginMapper loginMapper = new CountingLoginMapper();
        service.loginMapper = proxy(LoginMapper.class, loginMapper);
        service.doctorMapper = proxy(DoctorMapper.class, emptyHandler());
        service.patientMapper = proxy(PatientMapper.class, emptyHandler());
        Login login = new Login();
        login.setUsername("new-admin");
        login.setPassword("pw");
        login.setCertId(" ");

        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertEquals(0, loginMapper.insertCalls);
        assertNull(login.getRole());
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    private static InvocationHandler emptyHandler() {
        return new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getDeclaringClass().equals(Object.class)) {
                    return method.invoke(this, args);
                }
                return null;
            }
        };
    }

    private static class CountingLoginMapper implements InvocationHandler {
        int insertCalls;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getDeclaringClass().equals(Object.class)) {
                return method.invoke(this, args);
            }
            if ("findByUsername".equals(method.getName())) {
                return null;
            }
            if ("insert".equals(method.getName())) {
                insertCalls++;
                return 1;
            }
            return null;
        }
    }
}
