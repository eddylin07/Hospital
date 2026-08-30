package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Login;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DoctorControllerTest {

    @Test
    public void doctorCannotPrescribeForUnassignedPatient() {
        DoctorController controller = new DoctorController();
        AtomicBoolean seekCalled = new AtomicBoolean(false);
        controller.doctorService = service(DoctorService.class, (proxy, method, args) -> {
            if (method.getName().equals("getDoctorByLoginId")) {
                Doctor doctor = new Doctor();
                doctor.setId(2);
                return doctor;
            }
            return defaultValue(method.getReturnType());
        });
        controller.appointmentService = service(AppointmentService.class, (proxy, method, args) -> {
            if (method.getName().equals("selectByDoctorId")) {
                Appointment appointment = new Appointment();
                appointment.setPatientid(7);
                return Arrays.asList(appointment);
            }
            return defaultValue(method.getReturnType());
        });
        controller.patientService = service(PatientService.class, (proxy, method, args) -> {
            if (method.getName().equals("seek")) {
                seekCalled.set(true);
            }
            return defaultValue(method.getReturnType());
        });

        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(20);
        login.setRole(2);
        session.setAttribute("login", login);
        Map<String, String> request = new HashMap<>();
        request.put("patientid", "8");
        request.put("1_number", "1");

        JSONObject response = controller.drug(request, session);

        assertEquals("无权操作该患者", response.get("message"));
        assertFalse(seekCalled.get());
    }

    private static <T> T service(Class<T> type, InvocationHandler handler) {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler));
    }

    private static Object defaultValue(Class<?> returnType) {
        if (returnType.equals(boolean.class)) {
            return false;
        }
        if (returnType.equals(int.class) || returnType.equals(Integer.class)) {
            return 0;
        }
        return null;
    }
}
