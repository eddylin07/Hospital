package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Appointment;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.PatientService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

public class PatientControllerTest {
    @Test
    public void appointmentUsesPatientFromSessionNotRequestBody() {
        PatientController controller = new PatientController();
        AtomicReference<Integer> insertedPatientId = new AtomicReference<>();
        AtomicReference<Integer> updatedPatientId = new AtomicReference<>();
        controller.patientService = service(PatientService.class, (proxy, method, args) -> {
            if ("findPatientByLoginId".equals(method.getName())) {
                Patient patient = new Patient();
                patient.setId(42);
                return patient;
            }
            if ("updateAppointMent".equals(method.getName())) {
                updatedPatientId.set(((Patient) args[0]).getId());
                return "更新成功";
            }
            return defaultValue(method.getReturnType());
        });
        controller.appointmentService = service(AppointmentService.class, (proxy, method, args) -> {
            if ("addAppointment".equals(method.getName())) {
                insertedPatientId.set(((Appointment) args[0]).getPatientid());
                return "添加成功";
            }
            if ("selectTheLastAppointment".equals(method.getName())) {
                return 777;
            }
            return defaultValue(method.getReturnType());
        });
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(5);
        login.setRole(3);
        session.setAttribute("login", login);
        Appointment forged = new Appointment();
        forged.setPatientid(999);

        JSONObject response = controller.appointment(forged, session);

        assertEquals("添加成功", response.getString("message"));
        assertEquals(Integer.valueOf(42), insertedPatientId.get());
        assertEquals(Integer.valueOf(42), updatedPatientId.get());
    }

    private static <T> T service(Class<T> type, InvocationHandler handler) {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler));
    }

    private static Object defaultValue(Class<?> returnType) {
        if (returnType == Integer.TYPE) {
            return 0;
        }
        return null;
    }
}

