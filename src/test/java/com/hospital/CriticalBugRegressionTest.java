package com.hospital;

import com.alibaba.fastjson.JSONObject;
import com.hospital.common.CommonService;
import com.hospital.controller.LoginController;
import com.hospital.controller.PatientController;
import com.hospital.config.intercepors.LoginInterceptor;
import com.hospital.dao.DrugsMapper;
import com.hospital.dao.PatientMapper;
import com.hospital.dao.SeekMapper;
import com.hospital.entity.Appointment;
import com.hospital.entity.Drugs;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import com.hospital.service.AppointmentService;
import com.hospital.service.LoginService;
import com.hospital.service.PatientService;
import com.hospital.service.impl.PatientServiceImpl;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class CriticalBugRegressionTest {

    @Test
    public void failedLoginDoesNotCreateForgedSession() {
        LoginController controller = new LoginController();
        ReflectionTestUtils.setField(controller, "loginService", proxy(LoginService.class, (target, method, args) -> {
            if ("login".equals(method.getName())) {
                return "用户名不存在";
            }
            return defaultValue(method.getReturnType());
        }));

        MockHttpSession session = new MockHttpSession();
        JSONObject response = controller.login(new Login(), session);

        assertEquals("用户名不存在", response.get("message"));
        assertNull(session.getAttribute("login"));
    }

    @Test
    public void successfulLoginStoresPopulatedPrincipalOnly() {
        LoginController controller = new LoginController();
        ReflectionTestUtils.setField(controller, "loginService", proxy(LoginService.class, (target, method, args) -> {
            if ("login".equals(method.getName())) {
                Login login = (Login) args[0];
                login.setId(7);
                login.setRole(3);
                return "登录成功3";
            }
            return defaultValue(method.getReturnType());
        }));

        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        controller.login(login, session);

        assertSame(login, session.getAttribute("login"));
    }

    @Test
    public void interceptorRejectsForgedSessionAndWrongRole() throws Exception {
        LoginInterceptor interceptor = new LoginInterceptor();

        MockHttpServletRequest forgedRequest = new MockHttpServletRequest("GET", "/patient/appointment");
        Login forged = new Login();
        forgedRequest.getSession().setAttribute("login", forged);
        MockHttpServletResponse forgedResponse = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(forgedRequest, forgedResponse, new Object()));
        assertEquals("/hospital/login", forgedResponse.getRedirectedUrl());

        MockHttpServletRequest patientOnAdmin = new MockHttpServletRequest("GET", "/admin/patientManage");
        Login patient = new Login();
        patient.setId(1);
        patient.setRole(3);
        patientOnAdmin.getSession().setAttribute("login", patient);
        MockHttpServletResponse forbiddenResponse = new MockHttpServletResponse();
        assertFalse(interceptor.preHandle(patientOnAdmin, forbiddenResponse, new Object()));
        assertEquals(403, forbiddenResponse.getStatus());

        MockHttpServletRequest doctorLookup = new MockHttpServletRequest("GET", "/doctor/cardiology");
        doctorLookup.getSession().setAttribute("login", patient);
        assertTrue(interceptor.preHandle(doctorLookup, new MockHttpServletResponse(), new Object()));
    }

    @Test
    public void patientAppointmentBindsToSessionPatient() {
        PatientController controller = new PatientController();
        Patient currentPatient = new Patient();
        currentPatient.setId(42);
        AtomicReference<Appointment> inserted = new AtomicReference<>();
        AtomicReference<Patient> updatedPatient = new AtomicReference<>();

        ReflectionTestUtils.setField(controller, "patientService", proxy(PatientService.class, (target, method, args) -> {
            if ("findPatientByLoginId".equals(method.getName())) {
                return currentPatient;
            }
            if ("updateAppointMent".equals(method.getName())) {
                updatedPatient.set((Patient) args[0]);
                return CommonService.upd_message_success;
            }
            return defaultValue(method.getReturnType());
        }));
        ReflectionTestUtils.setField(controller, "appointmentService", proxy(AppointmentService.class, (target, method, args) -> {
            if ("addAppointment".equals(method.getName())) {
                Appointment appointment = (Appointment) args[0];
                appointment.setId(700);
                inserted.set(appointment);
                return CommonService.add_message_success;
            }
            return defaultValue(method.getReturnType());
        }));

        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(11);
        login.setRole(3);
        session.setAttribute("login", login);
        Appointment requestAppointment = new Appointment();
        requestAppointment.setPatientid(99);

        JSONObject response = controller.appointment(requestAppointment, session);

        assertEquals(CommonService.add_message_success, response.get("message"));
        assertEquals(Integer.valueOf(42), inserted.get().getPatientid());
        assertEquals(Integer.valueOf(42), updatedPatient.get().getId());
        assertEquals(Integer.valueOf(700), updatedPatient.get().getAppointmentid());
    }

    @Test
    public void dispensingRejectsOverdrawWithoutUpdatingStockOrSeek() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicInteger stockUpdates = new AtomicInteger();
        AtomicInteger seekUpdates = new AtomicInteger();

        ReflectionTestUtils.setField(service, "drugsMapper", proxy(DrugsMapper.class, (target, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                Drugs drugs = new Drugs();
                drugs.setId((Integer) args[0]);
                drugs.setNumber(2);
                drugs.setPrice(new BigDecimal("5.00"));
                return drugs;
            }
            if ("updateNumber".equals(method.getName())) {
                stockUpdates.incrementAndGet();
                return 1;
            }
            return defaultValue(method.getReturnType());
        }));
        ReflectionTestUtils.setField(service, "patientMapper", proxy(PatientMapper.class, (target, method, args) -> 1));
        ReflectionTestUtils.setField(service, "seekMapper", proxy(SeekMapper.class, (target, method, args) -> {
            if ("updateDrugs".equals(method.getName())) {
                seekUpdates.incrementAndGet();
                return 1;
            }
            return defaultValue(method.getReturnType());
        }));

        Patient patient = new Patient();
        patient.setId(42);
        patient.setDrugsids("1@3");

        assertEquals("对不起库存数量不足", service.seek(patient));
        assertEquals(0, stockUpdates.get());
        assertEquals(0, seekUpdates.get());
    }

    @Test
    public void dispensingUsesAtomicStockUpdateBeforeLatestSeekUpdate() {
        PatientServiceImpl service = new PatientServiceImpl();
        AtomicReference<Drugs> stockUpdate = new AtomicReference<>();
        AtomicReference<Seek> seekUpdate = new AtomicReference<>();

        ReflectionTestUtils.setField(service, "drugsMapper", proxy(DrugsMapper.class, (target, method, args) -> {
            if ("selectByPrimaryKey".equals(method.getName())) {
                Drugs drugs = new Drugs();
                drugs.setId((Integer) args[0]);
                drugs.setNumber(5);
                drugs.setPrice(new BigDecimal("7.50"));
                return drugs;
            }
            if ("updateNumber".equals(method.getName())) {
                stockUpdate.set((Drugs) args[0]);
                return 1;
            }
            return defaultValue(method.getReturnType());
        }));
        ReflectionTestUtils.setField(service, "patientMapper", proxy(PatientMapper.class, (target, method, args) -> {
            if ("updateByPrimaryKeySelective".equals(method.getName())) {
                return 1;
            }
            return defaultValue(method.getReturnType());
        }));
        ReflectionTestUtils.setField(service, "seekMapper", proxy(SeekMapper.class, (target, method, args) -> {
            if ("updateDrugs".equals(method.getName())) {
                seekUpdate.set((Seek) args[0]);
                return 1;
            }
            return defaultValue(method.getReturnType());
        }));

        Patient patient = new Patient();
        patient.setId(42);
        patient.setDrugsids("1@2");

        assertEquals(CommonService.upd_message_success, service.seek(patient));
        assertEquals(Integer.valueOf(1), stockUpdate.get().getId());
        assertEquals(Integer.valueOf(2), stockUpdate.get().getNumber());
        assertEquals(Integer.valueOf(42), seekUpdate.get().getPatientid());
        assertEquals(new BigDecimal("15.00"), seekUpdate.get().getPrice());
    }

    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, (target, method, args) -> {
            if (method.getDeclaringClass() == Object.class) {
                if ("toString".equals(method.getName())) {
                    return type.getSimpleName() + "Proxy";
                }
                if ("hashCode".equals(method.getName())) {
                    return System.identityHashCode(target);
                }
                if ("equals".equals(method.getName())) {
                    return target == args[0];
                }
            }
            return handler.invoke(target, method, args);
        }));
    }

    private static Object defaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }
        if (type == boolean.class) {
            return false;
        }
        if (type == void.class) {
            return null;
        }
        return 0;
    }
}
