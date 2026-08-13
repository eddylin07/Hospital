package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.common.CommonService;
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

public class PatientControllerAppointmentTest {

    @Test
    public void appointmentUsesSessionPatientInsteadOfRequestBodyPatient() {
        PatientController controller = new PatientController();
        Patient currentPatient = new Patient();
        currentPatient.setId(42);
        AtomicReference<Appointment> insertedAppointment = new AtomicReference<>();
        AtomicReference<Patient> updatedPatient = new AtomicReference<>();
        controller.patientService = service(PatientService.class, (proxy, method, args) -> {
            if ("findPatientByLoginId".equals(method.getName())) {
                return currentPatient;
            }
            if ("updateAppointMent".equals(method.getName())) {
                updatedPatient.set((Patient) args[0]);
                return CommonService.upd_message_success;
            }
            return defaultValue(method.getReturnType());
        });
        controller.appointmentService = service(AppointmentService.class, (proxy, method, args) -> {
            if ("addAppointment".equals(method.getName())) {
                Appointment appointment = (Appointment) args[0];
                appointment.setId(100);
                insertedAppointment.set(appointment);
                return CommonService.add_message_success;
            }
            if ("selectTheLastAppointment".equals(method.getName())) {
                throw new AssertionError("new appointment pointer must use generated insert id");
            }
            return defaultValue(method.getReturnType());
        });
        Login login = new Login();
        login.setId(7);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login);
        Appointment requestBody = new Appointment();
        requestBody.setPatientid(99);

        JSONObject response = controller.appointment(requestBody, session);

        assertEquals(CommonService.add_message_success, response.get("message"));
        assertEquals(Integer.valueOf(42), insertedAppointment.get().getPatientid());
        assertEquals(Integer.valueOf(42), updatedPatient.get().getId());
        assertEquals(Integer.valueOf(100), updatedPatient.get().getAppointmentid());
    }

    @Test
    public void downloadPdfWithoutAppointmentReturnsErrorInsteadOfThrowing() {
        PatientController controller = new PatientController();
        Patient currentPatient = new Patient();
        currentPatient.setId(42);
        controller.patientService = service(PatientService.class, (proxy, method, args) -> {
            if ("findPatientByLoginId".equals(method.getName())) {
                return currentPatient;
            }
            return defaultValue(method.getReturnType());
        });
        controller.appointmentService = service(AppointmentService.class, (proxy, method, args) -> {
            if ("selectTheLastAppointment".equals(method.getName())) {
                return null;
            }
            return defaultValue(method.getReturnType());
        });
        Login login = new Login();
        login.setId(7);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login);

        JSONObject response = controller.downloadpdf(session);

        assertEquals(CommonService.add_message_error, response.get("message"));
    }

    @SuppressWarnings("unchecked")
    private <T> T service(Class<T> type, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    private Object defaultValue(Class<?> returnType) {
        if (returnType.equals(Integer.TYPE)) {
            return 0;
        }
        if (returnType.equals(Boolean.TYPE)) {
            return false;
        }
        return null;
    }
}
