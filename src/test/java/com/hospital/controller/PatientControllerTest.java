package com.hospital.controller;

import com.hospital.entity.Appointment;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.PatientService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

public class PatientControllerTest {
    @Test
    public void patientAppointmentUsesSessionPatientInsteadOfRequestBodyPatient() {
        PatientController controller = new PatientController();
        AtomicReference<Appointment> inserted = new AtomicReference<>();
        controller.patientService = patientService();
        controller.appointmentService = appointmentService(inserted);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(42);
        login.setRole(3);
        session.setAttribute("login", login);
        Appointment requestBody = new Appointment();
        requestBody.setPatientid(999);

        controller.appointment(requestBody, session);

        assertEquals(Integer.valueOf(7), inserted.get().getPatientid());
    }

    private PatientService patientService() {
        return (PatientService) Proxy.newProxyInstance(
                PatientService.class.getClassLoader(),
                new Class[]{PatientService.class},
                (proxy, method, args) -> {
                    if ("findPatientByLoginId".equals(method.getName())) {
                        Patient patient = new Patient();
                        patient.setId(7);
                        return patient;
                    }
                    if ("updateAppointMent".equals(method.getName())) {
                        return "更新成功";
                    }
                    return null;
                });
    }

    private AppointmentService appointmentService(AtomicReference<Appointment> inserted) {
        return (AppointmentService) Proxy.newProxyInstance(
                AppointmentService.class.getClassLoader(),
                new Class[]{AppointmentService.class},
                (proxy, method, args) -> {
                    if ("addAppointment".equals(method.getName())) {
                        inserted.set((Appointment) args[0]);
                        return "添加成功";
                    }
                    if ("selectTheLastAppointment".equals(method.getName())) {
                        return 88;
                    }
                    return null;
                });
    }
}
