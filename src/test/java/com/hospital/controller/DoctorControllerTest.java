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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DoctorControllerTest {

    @Test
    public void drugRejectsPatientsOutsideDoctorAppointments() {
        DoctorController controller = new DoctorController();
        FakePatientService patientService = new FakePatientService();
        controller.patientService = patientService;
        controller.doctorService = new FakeDoctorService();
        controller.appointmentService = new FakeAppointmentService(41);
        Map<String, String> body = new HashMap<>();
        body.put("patientid", "99");
        body.put("1_number", "1");

        JSONObject response = controller.drug(body, doctorSession());

        assertEquals("无权操作该患者", response.get("message"));
        assertNull(patientService.seekPatient);
    }

    @Test
    public void drugAllowsPatientsAssignedToDoctor() {
        DoctorController controller = new DoctorController();
        FakePatientService patientService = new FakePatientService();
        controller.patientService = patientService;
        controller.doctorService = new FakeDoctorService();
        controller.appointmentService = new FakeAppointmentService(99);
        Map<String, String> body = new HashMap<>();
        body.put("patientid", "99");
        body.put("1_number", "2");

        JSONObject response = controller.drug(body, doctorSession());

        assertEquals("更新成功", response.get("message"));
        assertEquals(Integer.valueOf(99), patientService.seekPatient.getId());
        assertEquals("1@2", patientService.seekPatient.getDrugsids());
    }

    private MockHttpSession doctorSession() {
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(7);
        login.setRole(2);
        session.setAttribute("login", login);
        return session;
    }

    private static class FakeDoctorService implements DoctorService {
        @Override
        public Doctor getDoctorByLoginId(Integer loginid) {
            Doctor doctor = new Doctor();
            doctor.setId(10);
            return doctor;
        }

        @Override
        public List<Doctor> getAllDoctor() {
            return Collections.emptyList();
        }

        @Override
        public List<Doctor> getAllDoctor(String name, String certId) {
            return Collections.emptyList();
        }

        @Override
        public String delDoctor(Integer id) {
            return null;
        }

        @Override
        public String addDoctor(Doctor doctor) {
            return null;
        }

        @Override
        public Doctor getDoctor(Integer id) {
            return null;
        }

        @Override
        public String upDoctor(Doctor doctor) {
            return null;
        }

        @Override
        public List<Doctor> getDoctorByDepartment(String department) {
            return Collections.emptyList();
        }

        @Override
        public String seekInfo(Map map) {
            return null;
        }
    }

    private static class FakeAppointmentService implements AppointmentService {
        private final Integer patientId;

        FakeAppointmentService(Integer patientId) {
            this.patientId = patientId;
        }

        @Override
        public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) {
            Appointment appointment = new Appointment();
            appointment.setPatientid(patientId);
            return Collections.singletonList(appointment);
        }

        @Override
        public List<Appointment> getAllAppointments() {
            return Collections.emptyList();
        }

        @Override
        public List<Appointment> getAllAppointments(String doctorname, String patientname) {
            return Collections.emptyList();
        }

        @Override
        public String delAppointment(Integer id) {
            return null;
        }

        @Override
        public Appointment getAppointment(Integer id) {
            return null;
        }

        @Override
        public String UpdateAppointment(Appointment appointment) {
            return null;
        }

        @Override
        public String addAppointment(Appointment appointment) {
            return null;
        }

        @Override
        public List<Appointment> getPatientMessage(Integer patientId) {
            return Collections.emptyList();
        }

        @Override
        public Integer selectTheLastAppointment(Integer patientId) {
            return null;
        }
    }

    private static class FakePatientService implements PatientService {
        Patient seekPatient;

        @Override
        public String seek(Patient patient) {
            seekPatient = patient;
            return "更新成功";
        }

        @Override
        public List<Patient> getAllPatients(String name, String certId) {
            return Collections.emptyList();
        }

        @Override
        public List<Patient> getAllPatients() {
            return Collections.emptyList();
        }

        @Override
        public String delPatient(Integer id) {
            return null;
        }

        @Override
        public Patient getPatient(Integer id) {
            return null;
        }

        @Override
        public String updatePatient(Patient patient) {
            return null;
        }

        @Override
        public String addPatient(Patient patient) {
            return null;
        }

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            return null;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            return null;
        }

        @Override
        public Map<String, List> serrchInfo(String name, String type) {
            return Collections.emptyMap();
        }
    }
}

