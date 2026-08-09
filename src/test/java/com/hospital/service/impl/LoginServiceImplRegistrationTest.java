package com.hospital.service.impl;

import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Doctor;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LoginServiceImplRegistrationTest {

    @Test
    public void rejectsDuplicateUsernameBeforeLinkingPatientCert() {
        LoginServiceImpl service = new LoginServiceImpl();
        Login existing = login(1, 3, "taken");
        Patient patient = new Patient();
        patient.setId(9);
        patient.setCertId("cert-1");
        service.loginMapper = mapper(LoginMapper.class, (proxy, method, args) -> {
            if ("findByUsername".equals(method.getName())) {
                return existing;
            }
            if ("insert".equals(method.getName())) {
                throw new AssertionError("duplicate username must not insert a new login");
            }
            return defaultValue(method.getReturnType());
        });
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> {
            if ("findPatientByCertId".equals(method.getName())) {
                return patient;
            }
            if ("updateByPrimaryKeySelective".equals(method.getName())) {
                throw new AssertionError("duplicate username must not link a patient");
            }
            return defaultValue(method.getReturnType());
        });
        service.doctorMapper = mapper(DoctorMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        Login registration = new Login();
        registration.setUsername("taken");
        registration.setCertId("cert-1");

        String message = service.regist(registration);

        assertEquals("该用户名已被注册", message);
        assertNull(patient.getLoginid());
    }

    @Test
    public void publicRegistrationCannotCreateAdminForBlankCertificate() {
        LoginServiceImpl service = new LoginServiceImpl();
        service.loginMapper = mapper(LoginMapper.class, (proxy, method, args) -> {
            if ("findByUsername".equals(method.getName())) {
                return null;
            }
            if ("insert".equals(method.getName()) || "insertAdmin".equals(method.getName())) {
                throw new AssertionError("blank public certId must not create any login row");
            }
            return defaultValue(method.getReturnType());
        });
        service.patientMapper = mapper(PatientMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        service.doctorMapper = mapper(DoctorMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        Login registration = new Login();
        registration.setUsername("new-admin");
        registration.setCertId(" ");

        String message = service.regist(registration);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertNull(registration.getRole());
    }

    private Login login(Integer id, Integer role, String username) {
        Login login = new Login();
        login.setId(id);
        login.setRole(role);
        login.setUsername(username);
        return login;
    }

    @SuppressWarnings("unchecked")
    private <T> T mapper(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    private Object defaultValue(Class<?> returnType) {
        if (returnType.equals(Integer.TYPE)) {
            return 0;
        }
        if (returnType.equals(Boolean.TYPE)) {
            return false;
        }
        return null;
    }
}
