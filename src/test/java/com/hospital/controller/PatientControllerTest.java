package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.common.CommonService;
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
    public void appointmentUsesSessionPatientInsteadOfRequestBodyPatient() {
        PatientController controller = new PatientController();
        StubPatientService patientService = new StubPatientService();
        StubAppointmentService appointmentService = new StubAppointmentService();
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(5);
        login.setRole(3);
        session.setAttribute("login", login);
        Appointment appointment = new Appointment();
        appointment.setPatientid(99);

        JSONObject response = controller.appointment(appointment, session);

        assertEquals(CommonService.add_message_success, response.get("message"));
        assertEquals(Integer.valueOf(7), appointmentService.addedPatientId);
        assertEquals(Integer.valueOf(7), patientService.updatedAppointmentPatientId);
    }

    private static class StubPatientService implements PatientService {
        private Integer updatedAppointmentPatientId;

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            Patient patient = new Patient();
            patient.setId(7);
            return patient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            updatedAppointmentPatientId = patient.getId();
            return CommonService.upd_message_success;
        }

        @Override
        public List<Patient> getAllPatients(String name, String certId) {
            return null;
        }

        @Override
        public List<Patient> getAllPatients() {
            return null;
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
            return null;
        }
    }

    private static class StubAppointmentService implements AppointmentService {
        private Integer addedPatientId;

        @Override
        public String addAppointment(Appointment appointment) {
            addedPatientId = appointment.getPatientid();
            return CommonService.add_message_success;
        }

        @Override
        public Integer selectTheLastAppointment(Integer patientId) {
            return 88;
        }

        @Override
        public List<Appointment> getAllAppointments() {
            return null;
        }

        @Override
        public List<Appointment> getAllAppointments(String doctorname, String patientname) {
            return null;
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
            return null;
        }

        @Override
        public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) {
            return null;
        }
    }
}
