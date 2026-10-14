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
    public void publicRegistrationDoesNotCreateAdminForBlankCertificate() {
        LoginServiceImpl service = new LoginServiceImpl();
        AtomicBoolean inserted = new AtomicBoolean(false);
        service.loginMapper = proxy(LoginMapper.class, (ignored, method, args) -> {
            if ("insert".equals(method.getName())) {
                inserted.set(true);
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.doctorMapper = proxy(DoctorMapper.class, (ignored, method, args) -> defaultValue(method.getReturnType()));
        service.patientMapper = proxy(PatientMapper.class, (ignored, method, args) -> defaultValue(method.getReturnType()));

        Login login = new Login();
        login.setUsername("new-admin");
        login.setCertId("");

        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertFalse(inserted.get());
        assertNull(login.getRole());
    }

    @Test
    public void duplicateUsernameStopsBeforeBindingCertificateOwner() {
        LoginServiceImpl service = new LoginServiceImpl();
        AtomicBoolean doctorLookup = new AtomicBoolean(false);
        service.loginMapper = proxy(LoginMapper.class, (ignored, method, args) -> {
            if ("findByUsername".equals(method.getName())) {
                Login existing = new Login();
                existing.setId(99);
                return existing;
            }
            return defaultValue(method.getReturnType());
        });
        service.doctorMapper = proxy(DoctorMapper.class, (ignored, method, args) -> {
            if ("getDoctorByCertId".equals(method.getName())) {
                doctorLookup.set(true);
            }
            return defaultValue(method.getReturnType());
        });
        service.patientMapper = proxy(PatientMapper.class, (ignored, method, args) -> defaultValue(method.getReturnType()));

        Login login = new Login();
        login.setUsername("existing");
        login.setCertId("doctor-cert");

        assertEquals("该用户名已被注册", service.regist(login));
        assertFalse(doctorLookup.get());
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler);
    }

    private static Object defaultValue(Class<?> returnType) {
        if (!returnType.isPrimitive()) {
            return null;
        }
        if (returnType.equals(boolean.class)) {
            return false;
        }
        return 0;
    }
}
