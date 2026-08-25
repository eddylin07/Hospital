package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class DoctorControllerTest {

    @Test
    public void drugRejectsPatientNotAssignedToDoctor() {
        DoctorController controller = new DoctorController();
        AtomicInteger seekCalls = new AtomicInteger();
        ReflectionTestUtils.setField(controller, "doctorService", new FakeDoctorService());
        ReflectionTestUtils.setField(controller, "appointmentService", new FakeAppointmentService());
        ReflectionTestUtils.setField(controller, "patientService", new FakePatientService(seekCalls));

        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(10);
        login.setRole(2);
        session.setAttribute("login", login);

        Map<String, String> request = new HashMap<>();
        request.put("patientid", "99");
        request.put("7_number", "1");

        JSONObject response = controller.drug(request, session);

        assertEquals("无权操作该患者", response.getString("message"));
        assertEquals(0, seekCalls.get());
    }

    private static class FakeDoctorService implements DoctorService {
        @Override
        public Doctor getDoctorByLoginId(Integer loginid) {
            Doctor doctor = new Doctor();
            doctor.setId(3);
            doctor.setLoginid(loginid);
            return doctor;
        }

        @Override public List<Doctor> getAllDoctor() { return null; }
        @Override public List<Doctor> getAllDoctor(String name, String certId) { return null; }
        @Override public String delDoctor(Integer id) { return null; }
        @Override public String addDoctor(Doctor doctor) { return null; }
        @Override public Doctor getDoctor(Integer id) { return null; }
        @Override public String upDoctor(Doctor doctor) { return null; }
        @Override public List<Doctor> getDoctorByDepartment(String department) { return null; }
        @Override public String seekInfo(Map map) { return null; }
    }

    private static class FakeAppointmentService implements AppointmentService {
        @Override
        public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) {
            Appointment appointment = new Appointment();
            appointment.setDoctorid(doctorId);
            appointment.setPatientid(44);
            return Arrays.asList(appointment);
        }

        @Override public List<Appointment> getAllAppointments() { return null; }
        @Override public List<Appointment> getAllAppointments(String doctorname, String patientname) { return null; }
        @Override public String delAppointment(Integer id) { return null; }
        @Override public Appointment getAppointment(Integer id) { return null; }
        @Override public String UpdateAppointment(Appointment appointment) { return null; }
        @Override public String addAppointment(Appointment appointment) { return null; }
        @Override public List<Appointment> getPatientMessage(Integer patientId) { return null; }
        @Override public Integer selectTheLastAppointment(Integer patientId) { return null; }
    }

    private static class FakePatientService implements PatientService {
        private final AtomicInteger seekCalls;

        private FakePatientService(AtomicInteger seekCalls) {
            this.seekCalls = seekCalls;
        }

        @Override
        public String seek(Patient patient) {
            seekCalls.incrementAndGet();
            return "更新成功";
        }

        @Override public List<Patient> getAllPatients(String name, String certId) { return null; }
        @Override public List<Patient> getAllPatients() { return null; }
        @Override public String delPatient(Integer id) { return null; }
        @Override public Patient getPatient(Integer id) { return null; }
        @Override public String updatePatient(Patient patient) { return null; }
        @Override public String addPatient(Patient patient) { return null; }
        @Override public Patient findPatientByLoginId(Integer loginid) { return null; }
        @Override public String updateAppointMent(Patient patient) { return null; }
        @Override public Map<String, List> serrchInfo(String name, String type) { return null; }
    }
}
