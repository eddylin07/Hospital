package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.TestSupport;
import com.hospital.entity.Doctor;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import com.hospital.service.SeekService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class DoctorControllerTest {
    @Test
    public void drugMutationRequiresDoctorPatientAppointment() {
        DoctorController controller = new DoctorController();
        AtomicInteger patientMutations = new AtomicInteger();
        TestSupport.setField(controller, "doctorService", doctorServiceReturningCurrentDoctor());
        TestSupport.setField(controller, "appointmentService", TestSupport.proxy(AppointmentService.class, (proxy, method, args) -> false));
        TestSupport.setField(controller, "patientService", TestSupport.proxy(PatientService.class, (proxy, method, args) -> {
            patientMutations.incrementAndGet();
            return null;
        }));

        Map<String, String> body = new HashMap<>();
        body.put("patientid", "7");
        body.put("1_number", "2");

        JSONObject result = controller.drug(body, doctorSession());

        assertEquals("无权限操作该患者", result.getString("message"));
        assertEquals(0, patientMutations.get());
    }

    @Test
    public void printSeekReturnsControlledMessageWhenSeekIsMissing() {
        DoctorController controller = new DoctorController();
        TestSupport.setField(controller, "doctorService", doctorServiceReturningCurrentDoctor());
        TestSupport.setField(controller, "appointmentService", TestSupport.proxy(AppointmentService.class, (proxy, method, args) -> true));
        TestSupport.setField(controller, "seekService", TestSupport.proxy(SeekService.class, (proxy, method, args) -> null));
        TestSupport.setField(controller, "patientService", TestSupport.proxy(PatientService.class, (proxy, method, args) -> {
            Patient patient = new Patient();
            patient.setName("patient");
            return patient;
        }));

        JSONObject result = controller.printseek(7, doctorSession());

        assertEquals("未找到就诊信息", result.getString("message"));
    }

    private DoctorService doctorServiceReturningCurrentDoctor() {
        return TestSupport.proxy(DoctorService.class, (proxy, method, args) -> {
            if (method.getName().equals("getDoctorByLoginId")) {
                Doctor doctor = new Doctor();
                doctor.setId(2);
                doctor.setName("doctor");
                return doctor;
            }
            return null;
        });
    }

    private MockHttpSession doctorSession() {
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(10);
        login.setRole(2);
        session.setAttribute("login", login);
        return session;
    }
}
