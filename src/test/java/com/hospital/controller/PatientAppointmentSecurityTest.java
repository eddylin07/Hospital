package com.hospital.controller;

import com.hospital.TestProxies;
import com.hospital.common.CommonService;
import com.hospital.entity.Appointment;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.PatientService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

public class PatientAppointmentSecurityTest {
    @Test
    public void patientAppointmentUsesSessionPatientInsteadOfRequestBodyPatient() {
        PatientController controller = new PatientController();
        Patient sessionPatient = new Patient();
        sessionPatient.setId(7);
        AtomicReference<Appointment> insertedAppointment = new AtomicReference<>();
        AtomicReference<Patient> updatedPatient = new AtomicReference<>();
        PatientService patientService = TestProxies.proxy(PatientService.class, (proxy, method, args) -> {
            if ("findPatientByLoginId".equals(method.getName())) {
                return sessionPatient;
            }
            if ("updateAppointMent".equals(method.getName())) {
                updatedPatient.set((Patient) args[0]);
                return CommonService.upd_message_success;
            }
            return null;
        });
        AppointmentService appointmentService = TestProxies.proxy(AppointmentService.class, (proxy, method, args) -> {
            if ("addAppointment".equals(method.getName())) {
                Appointment appointment = (Appointment) args[0];
                appointment.setId(123);
                insertedAppointment.set(appointment);
                return CommonService.add_message_success;
            }
            return null;
        });
        ReflectionTestUtils.setField(controller, "patientService", patientService);
        ReflectionTestUtils.setField(controller, "appointmentService", appointmentService);
        Login login = new Login();
        login.setId(42);
        login.setRole(3);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login);
        Appointment appointment = new Appointment();
        appointment.setPatientid(999);

        controller.appointment(appointment, session);

        assertEquals(Integer.valueOf(7), insertedAppointment.get().getPatientid());
        assertEquals(Integer.valueOf(7), updatedPatient.get().getId());
        assertEquals(Integer.valueOf(123), updatedPatient.get().getAppointmentid());
    }
}
