package com.hospital.service.impl;

import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Login;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LoginServiceImplTest {
    @Test
    public void publicRegistrationDoesNotCreateAdminForBlankCertId() {
        LoginServiceImpl service = new LoginServiceImpl();
        AtomicBoolean insertedLogin = new AtomicBoolean(false);
        service.loginMapper = mapper(LoginMapper.class, insertedLogin);
        service.doctorMapper = mapper(DoctorMapper.class, new AtomicBoolean(false));
        service.patientMapper = mapper(PatientMapper.class, new AtomicBoolean(false));

        Login login = new Login();
        login.setUsername("attacker");
        login.setPassword("pw");
        login.setCertId("");

        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertFalse(insertedLogin.get());
    }

    @SuppressWarnings("unchecked")
    private <T> T mapper(Class<T> type, AtomicBoolean inserted) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, (proxy, method, args) -> {
            if ("insert".equals(method.getName()) || "insertAdmin".equals(method.getName())) {
                inserted.set(true);
                return 1;
            }
            if (method.getReturnType().equals(Integer.TYPE)) {
                return 0;
            }
            return null;
        });
    }
}
