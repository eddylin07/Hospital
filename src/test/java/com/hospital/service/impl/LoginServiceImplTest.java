package com.hospital.service.impl;

import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Doctor;
import com.hospital.entity.Login;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceImplTest {

    @Mock
    private LoginMapper loginMapper;
    @Mock
    private PatientMapper patientMapper;
    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private LoginServiceImpl loginService;

    @Test
    public void loginCopiesPersistedIdentityAndRoleOnSuccess() {
        Login persisted = new Login();
        persisted.setId(42);
        persisted.setUsername("patient");
        persisted.setPassword("secret");
        persisted.setRole(3);
        when(loginMapper.findByUsername("patient")).thenReturn(persisted);

        Login submitted = new Login();
        submitted.setUsername("patient");
        submitted.setPassword("secret");

        assertEquals("登录成功3", loginService.login(submitted));
        assertEquals(Integer.valueOf(42), submitted.getId());
        assertEquals(Integer.valueOf(3), submitted.getRole());
    }

    @Test
    public void loginRejectsWrongPasswordWithoutAssigningIdentity() {
        Login persisted = new Login();
        persisted.setUsername("patient");
        persisted.setPassword("secret");
        persisted.setRole(3);
        when(loginMapper.findByUsername("patient")).thenReturn(persisted);

        Login submitted = new Login();
        submitted.setUsername("patient");
        submitted.setPassword("wrong");

        assertEquals("密码错误", loginService.login(submitted));
        assertNull(submitted.getId());
        assertNull(submitted.getRole());
    }

    @Test
    public void registLinksDoctorCertificateToNewDoctorLogin() {
        Doctor doctor = new Doctor();
        doctor.setId(7);
        doctor.setCertId("D-100");
        when(doctorMapper.getDoctorByCertId("D-100")).thenReturn(doctor);

        Login persisted = new Login();
        persisted.setId(88);
        persisted.setUsername("doctorUser");
        when(loginMapper.findByUsername("doctorUser")).thenReturn(persisted);

        Login registration = new Login();
        registration.setUsername("doctorUser");
        registration.setPassword("secret");
        registration.setCertId("D-100");

        assertEquals("注册成功", loginService.regist(registration));
        assertEquals(Integer.valueOf(2), registration.getRole());
        assertEquals(Integer.valueOf(88), doctor.getLoginid());
        verify(loginMapper).insert(registration);
        verify(doctorMapper).updateByPrimaryKeySelective(doctor);
    }

    @Test
    public void registCreatesAdminLoginWhenCertificateIsBlankAndUsernameIsFree() {
        when(loginMapper.findByUsername("adminUser")).thenReturn(null);

        Login registration = new Login();
        registration.setUsername("adminUser");
        registration.setPassword("secret");
        registration.setCertId("  ");

        assertEquals("注册成功", loginService.regist(registration));
        assertEquals(Integer.valueOf(1), registration.getRole());
        verify(loginMapper).insert(registration);
    }
}
