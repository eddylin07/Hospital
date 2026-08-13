package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.common.CommonService;
import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Hospitalization;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.HospitalizationService;
import com.hospital.service.PatientService;
import com.hospital.service.SeekService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DoctorControllerAccessTest {

    @Test
    public void drugRejectsPatientOutsideDoctorAppointments() {
        DoctorController controller = new DoctorController();
        controller.doctorService = service(DoctorService.class, (proxy, method, args) -> {
            if ("getDoctorByLoginId".equals(method.getName())) {
                return doctor(2, "Dr. A");
            }
            return defaultValue(method.getReturnType());
        });
        controller.appointmentService = service(AppointmentService.class, (proxy, method, args) -> {
            if ("selectByDoctorId".equals(method.getName())) {
                return Arrays.asList(appointment(11));
            }
            return defaultValue(method.getReturnType());
        });
        controller.patientService = service(PatientService.class, (proxy, method, args) -> {
            if ("seek".equals(method.getName())) {
                throw new AssertionError("unauthorized dispense must not reach patient service");
            }
            return defaultValue(method.getReturnType());
        });
        Map<String, Object> request = new HashMap<>();
        request.put("patientid", 99);
        request.put("5_number", "1");

        JSONObject response = controller.drug(request, session());

        assertEquals(CommonService.upd_message_error, response.get("message"));
    }

    @Test
    public void printSeekWithoutSeekRecordReturnsError() {
        DoctorController controller = new DoctorController();
        controller.doctorService = service(DoctorService.class, (proxy, method, args) -> {
            if ("getDoctorByLoginId".equals(method.getName())) {
                return doctor(2, "Dr. A");
            }
            return defaultValue(method.getReturnType());
        });
        controller.appointmentService = service(AppointmentService.class, (proxy, method, args) -> {
            if ("selectByDoctorId".equals(method.getName())) {
                return Arrays.asList(appointment(11));
            }
            return defaultValue(method.getReturnType());
        });
        controller.seekService = service(SeekService.class, (proxy, method, args) -> null);
        controller.patientService = service(PatientService.class, (proxy, method, args) -> {
            if ("getPatient".equals(method.getName())) {
                throw new AssertionError("missing seek should stop before patient lookup");
            }
            return defaultValue(method.getReturnType());
        });

        JSONObject response = controller.printseek(11, session());

        assertEquals(CommonService.add_message_error, response.get("message"));
    }

    @Test
    public void zationRejectsPatientOutsideDoctorAppointments() {
        DoctorController controller = new DoctorController();
        controller.doctorService = service(DoctorService.class, (proxy, method, args) -> {
            if ("getDoctorByLoginId".equals(method.getName())) {
                return doctor(2, "Dr. A");
            }
            return defaultValue(method.getReturnType());
        });
        controller.appointmentService = service(AppointmentService.class, (proxy, method, args) -> {
            if ("selectByDoctorId".equals(method.getName())) {
                return Arrays.asList(appointment(11));
            }
            return defaultValue(method.getReturnType());
        });
        controller.hospitalizationService = service(HospitalizationService.class, (proxy, method, args) -> {
            if ("AddHospitalization".equals(method.getName())) {
                throw new AssertionError("unauthorized hospitalization must not reach service");
            }
            return defaultValue(method.getReturnType());
        });
        Hospitalization hospitalization = new Hospitalization();
        hospitalization.setPatientid(99);

        JSONObject response = controller.zation(hospitalization, session());

        assertEquals(CommonService.add_message_error, response.get("message"));
    }

    private MockHttpSession session() {
        Login login = new Login();
        login.setId(7);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login);
        return session;
    }

    private Doctor doctor(Integer id, String name) {
        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setName(name);
        return doctor;
    }

    private Appointment appointment(Integer patientId) {
        Appointment appointment = new Appointment();
        appointment.setPatientid(patientId);
        return appointment;
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
