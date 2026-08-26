package com.hospital.controller;

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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PatientControllerTest {

    @Test
    public void appointmentUsesPatientFromSessionInsteadOfRequestBody() {
        PatientController controller = new PatientController();
        FakePatientService patientService = new FakePatientService(42);
        FakeAppointmentService appointmentService = new FakeAppointmentService(77);
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;

        Login login = new Login();
        login.setId(5);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login);

        Appointment submitted = new Appointment();
        submitted.setPatientid(999);
        submitted.setDoctorid(3);

        controller.appointment(submitted, session);

        assertEquals(Integer.valueOf(42), appointmentService.addedPatientId);
        assertEquals(Integer.valueOf(77), patientService.updatedAppointmentId);
        assertEquals(Integer.valueOf(42), patientService.updatedPatientId);
    }

    private static class FakePatientService implements PatientService {
        private final Integer sessionPatientId;
        private Integer updatedPatientId;
        private Integer updatedAppointmentId;

        private FakePatientService(Integer sessionPatientId) {
            this.sessionPatientId = sessionPatientId;
        }

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            Patient patient = new Patient();
            patient.setId(sessionPatientId);
            return patient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            updatedPatientId = patient.getId();
            updatedAppointmentId = patient.getAppointmentid();
            return CommonService.upd_message_success;
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
        private final Integer generatedId;
        private Integer addedPatientId;

        private FakeAppointmentService(Integer generatedId) {
            this.generatedId = generatedId;
        }

        @Override
        public String addAppointment(Appointment appointment) {
            addedPatientId = appointment.getPatientid();
            appointment.setId(generatedId);
            return CommonService.add_message_success;
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

        @Override
        public Integer selectTheLastAppointment(Integer patientId) {
            return null;
        }

        @Override
        public boolean hasDoctorPatientAppointment(Integer doctorId, Integer patientId) {
            return false;
        }
    }
}
