package com.hospital.service.impl;

import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Doctor;
import com.hospital.entity.Login;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.hospital.TestProxies.defaultValue;
import static com.hospital.TestProxies.proxy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class LoginServiceImplTest {
    @Test
    public void publicRegistrationWithBlankCertIdDoesNotCreateAdmin() {
        AtomicBoolean inserted = new AtomicBoolean(false);
        LoginServiceImpl service = serviceWith(null, null, inserted);
        Login login = new Login();
        login.setUsername("new-user");
        login.setCertId(" ");

        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertFalse(inserted.get());
        assertNull(login.getRole());
    }

    @Test
    public void registrationRejectsExistingUsernameBeforeLinkingDoctor() {
        AtomicBoolean inserted = new AtomicBoolean(false);
        Login existing = new Login();
        existing.setId(1);
        existing.setUsername("taken");
        Doctor doctor = new Doctor();
        LoginServiceImpl service = serviceWith(existing, doctor, inserted);
        Login login = new Login();
        login.setUsername("taken");
        login.setCertId("doctor-cert");

        String message = service.regist(login);

        assertEquals("该用户名已被注册", message);
        assertFalse(inserted.get());
        assertNull(doctor.getLoginid());
    }

    private LoginServiceImpl serviceWith(final Login existingLogin, final Doctor doctor, final AtomicBoolean inserted) {
        LoginServiceImpl service = new LoginServiceImpl();
        service.loginMapper = proxy(LoginMapper.class, (proxy, method, args) -> {
            if ("findByUsername".equals(method.getName())) {
                return existingLogin;
            }
            if ("insert".equals(method.getName()) || "insertAdmin".equals(method.getName())) {
                inserted.set(true);
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        service.doctorMapper = proxy(DoctorMapper.class, (proxy, method, args) -> {
            if ("getDoctorByCertId".equals(method.getName())) {
                return doctor;
            }
            return defaultValue(method.getReturnType());
        });
        service.patientMapper = proxy(PatientMapper.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        return service;
    }
}
