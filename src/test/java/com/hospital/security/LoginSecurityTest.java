package com.hospital.security;

import com.hospital.TestProxies;
import com.hospital.config.intercepors.LoginInterceptor;
import com.hospital.controller.LoginController;
import com.hospital.dao.DoctorMapper;
import com.hospital.dao.LoginMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.entity.Login;
import com.hospital.service.impl.LoginServiceImpl;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class LoginSecurityTest {
    @Test
    public void failedLoginClearsSpoofedSessionPrincipal() {
        LoginController controller = new LoginController();
        ReflectionTestUtils.setField(controller, "loginService", loginServiceWithUser(null));

        Login login = new Login();
        login.setUsername("missing");
        login.setPassword("bad");
        login.setId(1);
        login.setRole(1);
        MockHttpSession session = new MockHttpSession();

        controller.login(login, session);

        assertNull(session.getAttribute("login"));
        assertNull(login.getId());
        assertNull(login.getRole());
    }

    @Test
    public void successfulLoginStoresResolvedPrincipal() {
        Login stored = new Login();
        stored.setId(7);
        stored.setUsername("patient");
        stored.setPassword("pw");
        stored.setRole(3);
        LoginController controller = new LoginController();
        ReflectionTestUtils.setField(controller, "loginService", loginServiceWithUser(stored));

        Login login = new Login();
        login.setUsername("patient");
        login.setPassword("pw");
        MockHttpSession session = new MockHttpSession();

        controller.login(login, session);

        Login sessionLogin = (Login) session.getAttribute("login");
        assertNotNull(sessionLogin);
        assertEquals(Integer.valueOf(7), sessionLogin.getId());
        assertEquals(Integer.valueOf(3), sessionLogin.getRole());
    }

    @Test
    public void interceptorRejectsRoleMismatch() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/patientManage");
        request.setRequestURI("/admin/patientManage");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Login patientLogin = new Login();
        patientLogin.setId(3);
        patientLogin.setRole(3);
        request.getSession().setAttribute("login", patientLogin);

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(403, response.getStatus());
    }

    @Test
    public void interceptorAllowsAuthenticatedDoctorLookupForPatients() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/doctor/cardiology");
        request.setRequestURI("/doctor/cardiology");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Login patientLogin = new Login();
        patientLogin.setId(3);
        patientLogin.setRole(3);
        request.getSession().setAttribute("login", patientLogin);

        assertTrue(interceptor.preHandle(request, response, new Object()));
        assertEquals(200, response.getStatus());
    }

    @Test
    public void publicRegistrationDoesNotCreateAdminForBlankCertId() {
        LoginServiceImpl service = new LoginServiceImpl();
        AtomicInteger insertCount = new AtomicInteger();
        LoginMapper loginMapper = TestProxies.proxy(LoginMapper.class, (proxy, method, args) -> {
            if ("findByUsername".equals(method.getName())) {
                return null;
            }
            if ("insert".equals(method.getName()) || "insertAdmin".equals(method.getName())) {
                insertCount.incrementAndGet();
                return 1;
            }
            return null;
        });
        DoctorMapper doctorMapper = TestProxies.proxy(DoctorMapper.class, (proxy, method, args) -> {
            fail("blank certId should be rejected before doctor lookup");
            return null;
        });
        PatientMapper patientMapper = TestProxies.proxy(PatientMapper.class, (proxy, method, args) -> {
            fail("blank certId should be rejected before patient lookup");
            return null;
        });
        ReflectionTestUtils.setField(service, "loginMapper", loginMapper);
        ReflectionTestUtils.setField(service, "doctorMapper", doctorMapper);
        ReflectionTestUtils.setField(service, "patientMapper", patientMapper);
        Login login = new Login();
        login.setUsername("new-admin");
        login.setPassword("pw");
        login.setCertId(" ");

        String message = service.regist(login);

        assertEquals("该证件信息未入库，不能注册该医生或者患者", message);
        assertEquals(0, insertCount.get());
        assertNull(login.getRole());
    }

    private LoginServiceImpl loginServiceWithUser(Login stored) {
        LoginServiceImpl service = new LoginServiceImpl();
        LoginMapper mapper = TestProxies.proxy(LoginMapper.class, (proxy, method, args) -> {
            if ("findByUsername".equals(method.getName())) {
                return stored;
            }
            return null;
        });
        ReflectionTestUtils.setField(service, "loginMapper", mapper);
        return service;
    }
}
