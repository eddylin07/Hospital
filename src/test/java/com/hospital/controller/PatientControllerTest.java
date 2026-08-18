package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.TestSupport;
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
        TestSupport.setField(controller, "patientService", TestSupport.proxy(PatientService.class, (proxy, method, args) -> {
            if (method.getName().equals("findPatientByLoginId")) {
                Patient patient = new Patient();
                patient.setId(7);
                return patient;
            }
            if (method.getName().equals("updateAppointMent")) {
                updatedPatient.set((Patient) args[0]);
                return "更新成功";
            }
            return null;
        }));
        TestSupport.setField(controller, "appointmentService", TestSupport.proxy(AppointmentService.class, (proxy, method, args) -> {
            if (method.getName().equals("addAppointment")) {
                Appointment appointment = (Appointment) args[0];
                appointment.setId(42);
                insertedAppointment.set(appointment);
                return "添加成功";
            }
            return null;
        }));

        Appointment requestBody = new Appointment();
        requestBody.setPatientid(99);
        requestBody.setDoctorid(3);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(5);
        login.setRole(3);
        session.setAttribute("login", login);

        JSONObject result = controller.appointment(requestBody, session);

        assertEquals("添加成功", result.getString("message"));
        assertEquals(Integer.valueOf(7), insertedAppointment.get().getPatientid());
        assertEquals(Integer.valueOf(7), updatedPatient.get().getId());
        assertEquals(Integer.valueOf(42), updatedPatient.get().getAppointmentid());
    }
}
