package com.hospital.service.impl;

import com.hospital.TestProxy;
import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Login;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class LoginServiceImplTest {
    @Test
    public void blankCertIdRegistrationDoesNotCreateAdmin() {
        LoginServiceImpl service = new LoginServiceImpl();
        AtomicBoolean insertedLogin = new AtomicBoolean(false);
        service.loginMapper = TestProxy.of(LoginMapper.class, (proxy, method, args) -> {
            if ("findByUsername".equals(method.getName())) {
                return null;
            }
            if ("insert".equals(method.getName()) || "insertAdmin".equals(method.getName())) {
                insertedLogin.set(true);
                return 1;
            }
            throw new AssertionError("Unexpected method: " + method.getName());
        });
        service.doctorMapper = TestProxy.of(DoctorMapper.class, (proxy, method, args) -> {
            throw new AssertionError("Doctor lookup should not run for blank certId");
        });
        service.patientMapper = TestProxy.of(PatientMapper.class, (proxy, method, args) -> {
            throw new AssertionError("Patient lookup should not run for blank certId");
        });
        Login login = new Login();
        login.setUsername("attacker");
        login.setPassword("pw");
        login.setCertId(" ");

        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertFalse(insertedLogin.get());
        assertNull(login.getRole());
    }
}
