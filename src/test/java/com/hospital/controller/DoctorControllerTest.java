package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Login;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.DrugsService;
import com.hospital.service.HospitalizationService;
import com.hospital.service.MedicalhistoryService;
import com.hospital.service.OptionService;
import com.hospital.service.PatientService;
import com.hospital.service.SeekService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DoctorControllerTest {

    @Test
    public void drugRejectsPatientWithoutDoctorAppointment() {
        DoctorController controller = new DoctorController();
        AtomicInteger patientSeekCalls = new AtomicInteger();
        controller.doctorService = doctorService();
        controller.appointmentService = appointmentServiceWithPatient(88);
        controller.patientService = patientService(patientSeekCalls);
        controller.drugsService = service(DrugsService.class);
        controller.hospitalizationService = service(HospitalizationService.class);
        controller.medicalhistoryService = service(MedicalhistoryService.class);
        controller.optionService = service(OptionService.class);
        controller.seekService = service(SeekService.class);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(20);
        login.setRole(2);
        session.setAttribute("login", login);
        Map<String, String> request = new HashMap<>();
        request.put("patientid", "99");
        request.put("1_number", "1");

        JSONObject response = controller.drug(request, session);

        Assert.assertEquals("无权操作该患者", response.getString("message"));
        Assert.assertEquals(0, patientSeekCalls.get());
    }

    private DoctorService doctorService() {
        return service(DoctorService.class, (proxy, method, args) -> {
            if ("getDoctorByLoginId".equals(method.getName())) {
                Doctor doctor = new Doctor();
                doctor.setId(2);
                return doctor;
            }
            return defaultValue(method.getReturnType());
        });
    }

    private AppointmentService appointmentServiceWithPatient(Integer patientId) {
        return service(AppointmentService.class, (proxy, method, args) -> {
            if ("selectByDoctorId".equals(method.getName())) {
                Appointment appointment = new Appointment();
                appointment.setPatientid(patientId);
                return Collections.singletonList(appointment);
            }
            return defaultValue(method.getReturnType());
        });
    }

    private PatientService patientService(AtomicInteger seekCalls) {
        return service(PatientService.class, (proxy, method, args) -> {
            if ("seek".equals(method.getName())) {
                seekCalls.incrementAndGet();
                return "更新成功";
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
