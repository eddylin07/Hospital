package com.hospital.service.impl;

import com.hospital.TestSupport;
import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Login;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LoginServiceImplTest {
    @Test
    public void failedLoginClearsCallerSuppliedIdentityFields() {
        LoginServiceImpl service = new LoginServiceImpl();
        TestSupport.setField(service, "loginMapper", TestSupport.proxy(LoginMapper.class, (proxy, method, args) -> {
            if (method.getName().equals("findByUsername")) {
                Login stored = new Login();
                stored.setId(7);
                stored.setRole(3);
                stored.setPassword("right-password");
                return stored;
            }
            return null;
        }));

        Login login = new Login();
        login.setUsername("patient");
        login.setPassword("wrong-password");
        login.setId(999);
        login.setRole(1);

        assertEquals("密码错误", service.login(login));
        assertNull(login.getId());
        assertNull(login.getRole());
    }

    @Test
    public void publicRegistrationDoesNotCreateAdminForBlankCertificate() {
        LoginServiceImpl service = new LoginServiceImpl();
        AtomicInteger loginInserts = new AtomicInteger();
        TestSupport.setField(service, "loginMapper", TestSupport.proxy(LoginMapper.class, (proxy, method, args) -> {
            if (method.getName().equals("insert")) {
                loginInserts.incrementAndGet();
                return 1;
            }
            return null;
        }));
        TestSupport.setField(service, "doctorMapper", TestSupport.proxy(DoctorMapper.class, (proxy, method, args) -> null));
        TestSupport.setField(service, "patientMapper", TestSupport.proxy(PatientMapper.class, (proxy, method, args) -> null));

        Login login = new Login();
        login.setUsername("new-admin");
        login.setPassword("pw");
        login.setCertId(" ");

        assertEquals("该证件信息未入库，不能注册该医生或者患者", service.regist(login));
        assertNull(login.getRole());
        assertEquals(0, loginInserts.get());
    }
}
