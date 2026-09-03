package com.hospital.service.impl;

import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Login;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginServiceImplTest {

    @Test
    public void failedLoginClearsCallerSuppliedIdentity() {
        Login stored = new Login();
        stored.setId(9);
        stored.setRole(1);
        stored.setUsername("admin");
        stored.setPassword("correct-password");
        LoginServiceImpl service = service(loginMapper(stored, new AtomicInteger()));
        Login request = new Login();
        request.setUsername("admin");
        request.setPassword("wrong-password");
        request.setId(1);
        request.setRole(1);

        String message = service.login(request);

        Assert.assertEquals("密码错误", message);
        Assert.assertNull(request.getId());
        Assert.assertNull(request.getRole());
    }

    @Test
    public void blankCertificateRegistrationCannotCreateAdminAccount() {
        AtomicInteger inserts = new AtomicInteger();
        LoginServiceImpl service = service(loginMapper(null, inserts));
        Login request = new Login();
        request.setUsername("attacker");
        request.setPassword("secret");
        request.setCertId(" ");

        String message = service.regist(request);

        Assert.assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        Assert.assertNull(request.getRole());
        Assert.assertEquals(0, inserts.get());
    }

    private LoginServiceImpl service(LoginMapper loginMapper) {
        LoginServiceImpl service = new LoginServiceImpl();
        service.loginMapper = loginMapper;
        service.doctorMapper = mapper(DoctorMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        return service;
    }

    private LoginMapper loginMapper(Login foundByUsername, AtomicInteger inserts) {
        return mapper(LoginMapper.class, (proxy, method, args) -> {
            if ("findByUsername".equals(method.getName())) {
                return foundByUsername;
            }
            if ("insert".equals(method.getName()) || "insertAdmin".equals(method.getName())) {
                inserts.incrementAndGet();
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
    }

    @SuppressWarnings("unchecked")
    private <T> T mapper(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    private Object defaultValue(Class<?> returnType) {
        if (!returnType.isPrimitive()) {
            return null;
        }
        if (returnType == boolean.class) {
            return false;
        }
        return 0;
    }
}
