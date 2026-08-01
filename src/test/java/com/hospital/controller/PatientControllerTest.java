package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.TestProxy;
import com.hospital.common.CommonService;
import com.hospital.entity.Appointment;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.PatientService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

public class PatientControllerTest {
    @Test
    public void appointmentUsesPatientFromSessionInsteadOfRequestBody() {
        PatientController controller = new PatientController();
        AtomicReference<Appointment> insertedAppointment = new AtomicReference<>();
        AtomicReference<Patient> updatedPatient = new AtomicReference<>();
        controller.patientService = TestProxy.of(PatientService.class, (proxy, method, args) -> {
            if ("findPatientByLoginId".equals(method.getName())) {
                Patient patient = new Patient();
                patient.setId(42);
                return patient;
            }
            if ("updateAppointMent".equals(method.getName())) {
                updatedPatient.set((Patient) args[0]);
                return CommonService.upd_message_success;
            }
            throw new AssertionError("Unexpected method: " + method.getName());
        });
        controller.appointmentService = TestProxy.of(AppointmentService.class, (proxy, method, args) -> {
            if ("addAppointment".equals(method.getName())) {
                insertedAppointment.set((Appointment) args[0]);
                return CommonService.add_message_success;
            }
            if ("selectTheLastAppointment".equals(method.getName())) {
                assertEquals(42, args[0]);
                return 99;
            }
            throw new AssertionError("Unexpected method: " + method.getName());
        });
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(7);
        login.setRole(3);
        session.setAttribute("login", login);
        Appointment appointment = new Appointment();
        appointment.setPatientid(999);

        JSONObject result = controller.appointment(appointment, session);

        assertEquals(CommonService.add_message_success, result.getString("message"));
        assertEquals(42, insertedAppointment.get().getPatientid().intValue());
        assertEquals(42, updatedPatient.get().getId().intValue());
        assertEquals(99, updatedPatient.get().getAppointmentid().intValue());
    }
}
