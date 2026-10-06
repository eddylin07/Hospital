package com.hospital.service.impl;

import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Login;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class LoginServiceImplTest {
    @Test
    public void publicRegistrationRejectsBlankCertificateInsteadOfCreatingAdmin() {
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
        service.doctorMapper = mapper(DoctorMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));

        Login request = new Login();
        request.setUsername("attacker");
        request.setPassword("pw");
        request.setCertId(" ");

        assertEquals("该证件信息未入库，不能注册该医生或者患者", service.regist(request));
        assertEquals(0, insertCount.get());
        assertNull(request.getRole());
    }

    @Test
    public void duplicateUsernameIsRejectedBeforeBindingDoctorOrPatient() {
        LoginServiceImpl service = new LoginServiceImpl();
        AtomicBoolean doctorLookedUp = new AtomicBoolean(false);
        service.loginMapper = mapper(LoginMapper.class, (proxy, method, args) -> {
            if ("findByUsername".equals(method.getName())) {
                return new Login();
            }
            return defaultValue(method.getReturnType());
        });
        service.doctorMapper = mapper(DoctorMapper.class, (proxy, method, args) -> {
            if ("getDoctorByCertId".equals(method.getName())) {
                doctorLookedUp.set(true);
            }
            return defaultValue(method.getReturnType());
        });
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));

        Login request = new Login();
        request.setUsername("existing");
        request.setCertId("CERT-1");

        assertEquals("该用户名已被注册", service.regist(request));
        assertFalse(doctorLookedUp.get());
    }

    @SuppressWarnings("unchecked")
    private static <T> T mapper(Class<T> mapperType, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(mapperType.getClassLoader(), new Class[]{mapperType}, handler);
    }

    private static Object defaultValue(Class<?> returnType) {
        if (!returnType.isPrimitive()) {
            return null;
        }
        if (returnType == boolean.class) {
            return false;
        }
        if (returnType == byte.class) {
            return (byte) 0;
        }
        if (returnType == short.class) {
            return (short) 0;
        }
        if (returnType == int.class) {
            return 0;
        }
        if (returnType == long.class) {
            return 0L;
        }
        if (returnType == float.class) {
            return 0F;
        }
        if (returnType == double.class) {
            return 0D;
        }
        if (returnType == char.class) {
            return '\0';
        }
        return null;
    }
}
