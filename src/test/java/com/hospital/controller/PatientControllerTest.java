package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Appointment;
import com.hospital.entity.Patient;
import com.hospital.entity.Login;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.HospitalizationService;
import com.hospital.service.MedicalhistoryService;
import com.hospital.service.PatientService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PatientControllerTest {
    @Test
    public void patientAppointmentUsesSessionPatientId() {
        PatientController controller = new PatientController();
        FakePatientService patientService = new FakePatientService();
        FakeAppointmentService appointmentService = new FakeAppointmentService();
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(44);
        login.setRole(3);
        session.setAttribute("login", login);
        Appointment request = new Appointment();
        request.setPatientid(999);

        JSONObject response = controller.appointment(request, session);

        assertEquals("added", response.getString("message"));
        assertEquals(Integer.valueOf(12), appointmentService.inserted.getPatientid());
        assertEquals(Integer.valueOf(12), patientService.updated.getId());
        assertEquals(Integer.valueOf(77), patientService.updated.getAppointmentid());
    }

    private static class FakePatientService implements PatientService {
        private Patient updated;

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            Patient patient = new Patient();
            patient.setId(12);
            return patient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            this.updated = patient;
            return "updated";
        }

        @Override public List<Patient> getAllPatients(String name, String certId) { return Collections.emptyList(); }
        @Override public List<Patient> getAllPatients() { return Collections.emptyList(); }
        @Override public String delPatient(Integer id) { return null; }
        @Override public Patient getPatient(Integer id) { return null; }
        @Override public String updatePatient(Patient patient) { return null; }
        @Override public String addPatient(Patient patient) { return null; }
        @Override public String seek(Patient patient) { return null; }
        @Override public Map<String, List> serrchInfo(String name, String type) { return Collections.emptyMap(); }
    }

    private static class FakeAppointmentService implements AppointmentService {
        private Appointment inserted;

        @Override
        public String addAppointment(Appointment appointment) {
            this.inserted = appointment;
            return "added";
        }

        @Override
        public Integer selectTheLastAppointment(Integer patientId) {
            return 77;
        }

        @Override public List<Appointment> getAllAppointments() { return Collections.emptyList(); }
        @Override public List<Appointment> getAllAppointments(String doctorname, String patientname) { return Collections.emptyList(); }
        @Override public String delAppointment(Integer id) { return null; }
        @Override public Appointment getAppointment(Integer id) { return null; }
        @Override public String UpdateAppointment(Appointment appointment) { return null; }
        @Override public List<Appointment> getPatientMessage(Integer patientId) { return Collections.emptyList(); }
        @Override public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) { return Collections.emptyList(); }
    }
}
