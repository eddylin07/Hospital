package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Appointment;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.PatientService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PatientControllerAppointmentTest {
    @Test
    public void appointmentUsesSessionPatientInsteadOfRequestPatientId() {
        PatientController controller = new PatientController();
        CapturingPatientService patientService = new CapturingPatientService();
        CapturingAppointmentService appointmentService = new CapturingAppointmentService();
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;

        Login login = new Login();
        login.setId(99);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login);

        Appointment appointment = new Appointment();
        appointment.setPatientid(666);
        appointment.setDoctorid(3);

        JSONObject result = controller.appointment(appointment, session);

        assertEquals(Integer.valueOf(99), patientService.lookupLoginId);
        assertEquals(Integer.valueOf(7), appointmentService.inserted.getPatientid());
        assertEquals(Integer.valueOf(7), appointmentService.selectLastPatientId);
        assertEquals(Integer.valueOf(7), patientService.updatedAppointmentOwner.getId());
        assertEquals(Integer.valueOf(123), patientService.updatedAppointmentOwner.getAppointmentid());
        assertEquals("added", result.getString("message"));
    }

    private static final class CapturingPatientService implements PatientService {
        private Integer lookupLoginId;
        private Patient updatedAppointmentOwner;

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            lookupLoginId = loginid;
            Patient patient = new Patient();
            patient.setId(7);
            return patient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            updatedAppointmentOwner = patient;
            return "updated";
        }

        @Override
        public List<Patient> getAllPatients(String name, String certId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Patient> getAllPatients() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String delPatient(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Patient getPatient(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String updatePatient(Patient patient) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String addPatient(Patient patient) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String seek(Patient patient) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, List> serrchInfo(String name, String type) {
            throw new UnsupportedOperationException();
        }
    }

    private static final class CapturingAppointmentService implements AppointmentService {
        private Appointment inserted;
        private Integer selectLastPatientId;

        @Override
        public String addAppointment(Appointment appointment) {
            inserted = appointment;
            return "added";
        }

        @Override
        public Integer selectTheLastAppointment(Integer patientId) {
            selectLastPatientId = patientId;
            return 123;
        }

        @Override
        public List<Appointment> getAllAppointments() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Appointment> getAllAppointments(String doctorname, String patientname) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String delAppointment(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Appointment getAppointment(Integer id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String UpdateAppointment(Appointment appointment) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Appointment> getPatientMessage(Integer patientId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) {
            throw new UnsupportedOperationException();
        }
    }
}
