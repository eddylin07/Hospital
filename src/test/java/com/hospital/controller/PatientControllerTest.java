package com.hospital.controller;

import com.hospital.common.CommonService;
import com.hospital.entity.Appointment;
import com.hospital.entity.Patient;
import com.hospital.entity.Login;
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
    public void appointmentUsesPatientFromSessionInsteadOfRequestBody() {
        PatientController controller = new PatientController();
        FakePatientService patientService = new FakePatientService();
        FakeAppointmentService appointmentService = new FakeAppointmentService();
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(501);
        login.setRole(3);
        session.setAttribute("login", login);
        Appointment appointment = new Appointment();
        appointment.setPatientid(999);

        controller.appointment(appointment, session);

        assertEquals(Integer.valueOf(1), appointmentService.inserted.getPatientid());
        assertEquals(Integer.valueOf(1), patientService.updatedAppointmentPatient.getId());
        assertEquals(Integer.valueOf(44), patientService.updatedAppointmentPatient.getAppointmentid());
    }

    private static class FakePatientService implements PatientService {
        private Patient updatedAppointmentPatient;

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
            return "";
        }

        @Override
        public Patient getPatient(Integer id) {
            return null;
        }

        @Override
        public String updatePatient(Patient patient) {
            return "";
        }

        @Override
        public String addPatient(Patient patient) {
            return "";
        }

        @Override
        public String seek(Patient patient) {
            return "";
        }

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            Patient patient = new Patient();
            patient.setId(1);
            return patient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            updatedAppointmentPatient = patient;
            return CommonService.upd_message_success;
        }

        @Override
        public Map<String, List> serrchInfo(String name, String type) {
            return Collections.emptyMap();
        }
    }

    private static class FakeAppointmentService implements AppointmentService {
        private Appointment inserted;

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
            return "";
        }

        @Override
        public Appointment getAppointment(Integer id) {
            return null;
        }

        @Override
        public String UpdateAppointment(Appointment appointment) {
            return "";
        }

        @Override
        public String addAppointment(Appointment appointment) {
            inserted = appointment;
            return CommonService.add_message_success;
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
            return 44;
        }
    }
}
