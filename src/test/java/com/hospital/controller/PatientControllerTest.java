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

public class PatientControllerTest {
    @Test
    public void appointmentUsesSessionPatientAndGeneratedAppointmentId() {
        PatientController controller = new PatientController();
        StubPatientService patientService = new StubPatientService();
        StubAppointmentService appointmentService = new StubAppointmentService();
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;

        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(5);
        session.setAttribute("login", login);

        Appointment appointment = new Appointment();
        appointment.setPatientid(999);
        JSONObject result = controller.appointment(appointment, session);

        assertEquals("添加成功", result.get("message"));
        assertEquals(Integer.valueOf(7), appointmentService.inserted.getPatientid());
        assertEquals(Integer.valueOf(7), patientService.updated.getId());
        assertEquals(Integer.valueOf(42), patientService.updated.getAppointmentid());
    }

    private static class StubPatientService implements PatientService {
        private Patient updated;

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            Patient patient = new Patient();
            patient.setId(7);
            return patient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            this.updated = patient;
            return "更新成功";
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

    private static class StubAppointmentService implements AppointmentService {
        private Appointment inserted;

        @Override
        public String addAppointment(Appointment appointment) {
            inserted = appointment;
            appointment.setId(42);
            return "添加成功";
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

        @Override
        public Integer selectTheLastAppointment(Integer patientId) {
            throw new UnsupportedOperationException();
        }
    }
}
