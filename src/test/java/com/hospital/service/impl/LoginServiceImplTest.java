package com.hospital.service.impl;

import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Login;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;

public class LoginServiceImplTest {

    @Test
    public void registrationRejectsBlankCertIdInsteadOfCreatingAdmin() {
        LoginServiceImpl service = new LoginServiceImpl();
        LoginMapperHandler loginMapper = new LoginMapperHandler(null);
        service.loginMapper = proxy(LoginMapper.class, loginMapper);
        service.doctorMapper = proxy(DoctorMapper.class, defaultHandler());
        service.patientMapper = proxy(PatientMapper.class, defaultHandler());

        Login login = new Login();
        login.setUsername("new-user");
        login.setCertId(" ");

        assertEquals("该证件信息未入库，不能注册该医生或者患者", service.regist(login));
        assertEquals(0, loginMapper.insertCalls);
    }

    @Test
    public void registrationRejectsDuplicateUsernameBeforeBindingDoctorOrPatient() {
        Login existing = new Login();
        existing.setId(99);
        LoginServiceImpl service = new LoginServiceImpl();
        LoginMapperHandler loginMapper = new LoginMapperHandler(existing);
        service.loginMapper = proxy(LoginMapper.class, loginMapper);
        service.doctorMapper = proxy(DoctorMapper.class, defaultHandler());
        service.patientMapper = proxy(PatientMapper.class, defaultHandler());

        Login login = new Login();
        login.setUsername("existing");
        login.setCertId("CERT-1");

        assertEquals("该用户名已被注册", service.regist(login));
        assertEquals(0, loginMapper.insertCalls);
    }

    private static class LoginMapperHandler implements InvocationHandler {
        private final Login existing;
        private int insertCalls;

        private LoginMapperHandler(Login existing) {
            this.existing = existing;
        }

        @Override
        public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
            if ("findByUsername".equals(method.getName())) {
                return existing;
            }
            if ("insert".equals(method.getName()) || "insertAdmin".equals(method.getName())) {
                insertCalls++;
                return 1;
            }
            return defaultValue(method.getReturnType());
        }
    }

    private static InvocationHandler defaultHandler() {
        return (proxy, method, args) -> defaultValue(method.getReturnType());
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    private static Object defaultValue(Class<?> type) {
        if (type.equals(Boolean.TYPE)) {
            return false;
        }
        if (type.equals(Integer.TYPE)) {
            return 0;
        }
        return null;
    }
}
