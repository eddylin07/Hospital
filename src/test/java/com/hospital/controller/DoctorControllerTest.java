package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DoctorControllerTest {
    @Test
    public void drugRejectsPatientsNotAssignedToCurrentDoctor() {
        DoctorController controller = new DoctorController();
        CountingPatientService patientService = new CountingPatientService();
        controller.patientService = proxy(PatientService.class, patientService);
        controller.doctorService = proxy(DoctorService.class, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getDeclaringClass().equals(Object.class)) {
                    return method.invoke(this, args);
                }
                if ("getDoctorByLoginId".equals(method.getName())) {
                    Doctor doctor = new Doctor();
                    doctor.setId(5);
                    return doctor;
                }
                return null;
            }
        });
        controller.appointmentService = proxy(AppointmentService.class, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getDeclaringClass().equals(Object.class)) {
                    return method.invoke(this, args);
                }
                if ("selectByDoctorId".equals(method.getName())) {
                    Appointment appointment = new Appointment();
                    appointment.setPatientid(7);
                    return Arrays.asList(appointment);
                }
                return null;
            }
        });
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(12);
        login.setRole(2);
        session.setAttribute("login", login);
        Map<String, String> body = new HashMap<>();
        body.put("patientid", "999");
        body.put("1_number", "1");

        JSONObject json = controller.drug(body, session);

        assertEquals("无权限", json.get("message"));
        assertEquals(0, patientService.seekCalls);
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    private static class CountingPatientService implements InvocationHandler {
        int seekCalls;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getDeclaringClass().equals(Object.class)) {
                return method.invoke(this, args);
            }
            if ("seek".equals(method.getName())) {
                seekCalls++;
            }
            return null;
        }
    }
}
