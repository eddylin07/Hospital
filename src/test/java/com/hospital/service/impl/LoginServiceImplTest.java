package com.hospital.service.impl;

import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Doctor;
import com.hospital.entity.Login;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LoginServiceImplTest {
    @Test
    public void publicRegistrationWithBlankCertDoesNotCreateAdmin() {
        LoginServiceImpl service = new LoginServiceImpl();
        AtomicInteger inserts = new AtomicInteger();
        service.loginMapper = mapper(LoginMapper.class, (proxy, method, args) -> {
            if ("findByUsername".equals(method.getName())) {
                return null;
            }
            if ("insert".equals(method.getName())) {
                inserts.incrementAndGet();
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.doctorMapper = mapper(DoctorMapper.class, (proxy, method, args) -> null);
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> null);
        Login login = new Login();
        login.setUsername("eviladmin");
        login.setPassword("pw");
        login.setCertId("");

        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertNull(login.getRole());
        assertEquals(0, inserts.get());
    }

    @Test
    public void duplicateUsernameIsRejectedBeforeDoctorBinding() {
        LoginServiceImpl service = new LoginServiceImpl();
        AtomicInteger inserts = new AtomicInteger();
        AtomicInteger doctorUpdates = new AtomicInteger();
        service.loginMapper = mapper(LoginMapper.class, (proxy, method, args) -> {
            if ("findByUsername".equals(method.getName())) {
                Login existing = new Login();
                existing.setId(1);
                return existing;
            }
            if ("insert".equals(method.getName())) {
                inserts.incrementAndGet();
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.doctorMapper = mapper(DoctorMapper.class, (proxy, method, args) -> {
            if ("getDoctorByCertId".equals(method.getName())) {
                return new Doctor();
            }
            if ("updateByPrimaryKeySelective".equals(method.getName())) {
                doctorUpdates.incrementAndGet();
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> null);
        Login login = new Login();
        login.setUsername("taken");
        login.setPassword("pw");
        login.setCertId("doctor-cert");

        String message = service.regist(login);

        assertEquals("该用户名已被注册", message);
        assertEquals(0, inserts.get());
        assertEquals(0, doctorUpdates.get());
    }

    private static <T> T mapper(Class<T> type, InvocationHandler handler) {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler));
    }

    private static Object defaultValue(Class<?> returnType) {
        if (returnType == Integer.TYPE) {
            return 0;
        }
        return null;
    }
}

