package com.hospital.controller;

import com.hospital.entity.Appointment;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.PatientService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PatientControllerTest {

    @Test
    public void patientAppointmentUsesSessionPatientIdInsteadOfRequestBody() {
        PatientController controller = new PatientController();
        FakePatientService patientService = new FakePatientService();
        FakeAppointmentService appointmentService = new FakeAppointmentService();
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;

        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(100);
        login.setRole(3);
        session.setAttribute("login", login);

        Appointment appointment = new Appointment();
        appointment.setPatientid(999);

        controller.appointment(appointment, session);

        assertEquals(Integer.valueOf(42), appointmentService.addedAppointment.getPatientid());
        assertEquals(Integer.valueOf(42), appointmentService.lastAppointmentPatientId);
        assertEquals(Integer.valueOf(42), patientService.updatedPatient.getId());
        assertEquals(Integer.valueOf(77), patientService.updatedPatient.getAppointmentid());
    }

    private static class FakePatientService implements PatientService {
        private Patient updatedPatient;

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            Patient patient = new Patient();
            patient.setId(42);
            return patient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            this.updatedPatient = patient;
            return "updated";
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
        public String seek(Patient patient) {
            return null;
        }

        @Override
        public Map<String, List> serrchInfo(String name, String type) {
            return Collections.emptyMap();
        }
    }

    private static class FakeAppointmentService implements AppointmentService {
        private Appointment addedAppointment;
        private Integer lastAppointmentPatientId;

        @Override
        public String addAppointment(Appointment appointment) {
            this.addedAppointment = appointment;
            return "added";
        }

        @Override
        public Integer selectTheLastAppointment(Integer patientId) {
            this.lastAppointmentPatientId = patientId;
            return 77;
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
        public List<Appointment> getPatientMessage(Integer patientId) {
            return Collections.emptyList();
        }

        @Override
        public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) {
            return Collections.emptyList();
        }
    }
}
