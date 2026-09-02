package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Appointment;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.HospitalizationService;
import com.hospital.service.MedicalhistoryService;
import com.hospital.service.PatientService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;

public class PatientControllerTest {

    @Test
    public void appointmentUsesPatientFromSessionInsteadOfRequestBody() {
        PatientController controller = new PatientController();
        AtomicInteger insertedPatientId = new AtomicInteger();
        AtomicInteger updatedPatientId = new AtomicInteger();
        AtomicInteger updatedAppointmentId = new AtomicInteger();
        controller.patientService = patientService(updatedPatientId, updatedAppointmentId);
        controller.appointmentService = appointmentService(insertedPatientId);
        controller.doctorService = service(DoctorService.class);
        controller.hospitalizationService = service(HospitalizationService.class);
        controller.medicalhistoryService = service(MedicalhistoryService.class);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(10);
        login.setRole(3);
        session.setAttribute("login", login);
        Appointment request = new Appointment();
        request.setPatientid(777);
        request.setDoctorid(2);

        JSONObject response = controller.appointment(request, session);

        Assert.assertEquals("添加成功", response.getString("message"));
        Assert.assertEquals(99, insertedPatientId.get());
        Assert.assertEquals(99, updatedPatientId.get());
        Assert.assertEquals(123, updatedAppointmentId.get());
    }

    private PatientService patientService(AtomicInteger updatedPatientId, AtomicInteger updatedAppointmentId) {
        return service(PatientService.class, (proxy, method, args) -> {
            if ("findPatientByLoginId".equals(method.getName())) {
                Patient patient = new Patient();
                patient.setId(99);
                return patient;
            }
            if ("updateAppointMent".equals(method.getName())) {
                Patient patient = (Patient) args[0];
                updatedPatientId.set(patient.getId());
                updatedAppointmentId.set(patient.getAppointmentid());
                return "更新成功";
            }
            return defaultValue(method.getReturnType());
        });
    }

    private AppointmentService appointmentService(AtomicInteger insertedPatientId) {
        return service(AppointmentService.class, (proxy, method, args) -> {
            if ("addAppointment".equals(method.getName())) {
                Appointment appointment = (Appointment) args[0];
                insertedPatientId.set(appointment.getPatientid());
                appointment.setId(123);
                return "添加成功";
            }
            return defaultValue(method.getReturnType());
        });
    }

    private <T> T service(Class<T> type) {
        return service(type, (proxy, method, args) -> defaultValue(method.getReturnType()));
    }

    @SuppressWarnings("unchecked")
    private <T> T service(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    private Object defaultValue(Class<?> returnType) {
        if (!returnType.isPrimitive()) {
            return null;
        }
        if (returnType == boolean.class) {
            return false;
        }
        return 0;
    }
}
