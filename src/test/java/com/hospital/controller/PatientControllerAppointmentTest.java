package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
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

public class PatientControllerAppointmentTest {
    @Test
    public void appointmentUsesPatientFromSessionInsteadOfRequestBody() {
        PatientController controller = new PatientController();
        FakePatientService patientService = new FakePatientService();
        FakeAppointmentService appointmentService = new FakeAppointmentService();
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;
        Appointment appointment = new Appointment();
        appointment.setPatientid(999);
        appointment.setDoctorid(3);
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(42);
        login.setRole(3);
        session.setAttribute("login", login);

        JSONObject result = controller.appointment(appointment, session);

        assertEquals(CommonService.add_message_success, result.get("message"));
        assertEquals(Integer.valueOf(7), appointmentService.savedAppointment.getPatientid());
        assertEquals(Integer.valueOf(7), patientService.updatedPatient.getId());
        assertEquals(Integer.valueOf(123), patientService.updatedPatient.getAppointmentid());
    }

    private static class FakePatientService implements PatientService {
        private Patient updatedPatient;

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            Patient patient = new Patient();
            patient.setId(7);
            patient.setLoginid(loginid);
            return patient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            updatedPatient = patient;
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
        public Map<String, List> serrchInfo(String name, String type) {
            return Collections.emptyMap();
        }
    }

    private static class FakeAppointmentService implements AppointmentService {
        private Appointment savedAppointment;

        @Override
        public String addAppointment(Appointment appointment) {
            savedAppointment = appointment;
            return CommonService.add_message_success;
        }

        @Override
        public Integer selectTheLastAppointment(Integer patientId) {
            assertEquals(Integer.valueOf(7), patientId);
            return 123;
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
        public List<Appointment> getPatientMessage(Integer patientId) {
            return Collections.emptyList();
        }

        @Override
        public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) {
            return Collections.emptyList();
        }
    }
}
