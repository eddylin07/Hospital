package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.common.CommonService;
import com.hospital.entity.Appointment;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.HospitalizationService;
import com.hospital.service.MedicalhistoryService;
import com.hospital.service.PatientService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

public class PatientControllerTest {
    @Test
    public void appointmentUsesPatientFromSessionInsteadOfRequestBody() {
        PatientController controller = new PatientController();
        AtomicReference<Appointment> savedAppointment = new AtomicReference<>();
        AtomicReference<Patient> updatedPatient = new AtomicReference<>();
        controller.patientService = proxy(PatientService.class, (proxy, method, args) -> {
            if ("findPatientByLoginId".equals(method.getName())) {
                Patient patient = new Patient();
                patient.setId(7);
                return patient;
            }
            if ("updateAppointMent".equals(method.getName())) {
                updatedPatient.set((Patient) args[0]);
                return CommonService.upd_message_success;
            }
            return defaultValue(method.getReturnType());
        });
        controller.appointmentService = proxy(AppointmentService.class, (proxy, method, args) -> {
            if ("addAppointment".equals(method.getName())) {
                Appointment appointment = (Appointment) args[0];
                savedAppointment.set(appointment);
                appointment.setId(42);
                return CommonService.add_message_success;
            }
            return defaultValue(method.getReturnType());
        });
        controller.doctorService = proxy(DoctorService.class, (proxy, method, args) -> Collections.emptyList());
        controller.hospitalizationService = proxy(HospitalizationService.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        controller.medicalhistoryService = proxy(MedicalhistoryService.class, (proxy, method, args) -> defaultValue(method.getReturnType()));
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(101);
        login.setRole(3);
        session.setAttribute("login", login);
        Appointment requestAppointment = new Appointment();
        requestAppointment.setPatientid(999);

        JSONObject json = controller.appointment(requestAppointment, session);

        assertEquals(CommonService.add_message_success, json.getString("message"));
        assertEquals(Integer.valueOf(7), savedAppointment.get().getPatientid());
        assertEquals(Integer.valueOf(7), updatedPatient.get().getId());
        assertEquals(Integer.valueOf(42), updatedPatient.get().getAppointmentid());
    }

    @SuppressWarnings("unchecked")
    private <T> T proxy(Class<T> type, java.lang.reflect.InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    private static Object defaultValue(Class<?> returnType) {
        if (returnType == Boolean.TYPE) {
            return false;
        }
        if (returnType == Integer.TYPE) {
            return 0;
        }
        return null;
    }
}
