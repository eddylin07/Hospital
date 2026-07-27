package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
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
        FakePatientService patientService = new FakePatientService();
        FakeAppointmentService appointmentService = new FakeAppointmentService();
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;

        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(5);
        login.setRole(3);
        session.setAttribute("login", login);

        Appointment requestAppointment = new Appointment();
        requestAppointment.setPatientid(999);
        requestAppointment.setDoctorid(20);

        JSONObject response = controller.appointment(requestAppointment, session);

        assertEquals("添加成功", response.getString("message"));
        assertEquals(Integer.valueOf(123), appointmentService.insertedPatientId);
        assertEquals(Integer.valueOf(123), patientService.updatedPatientId);
        assertEquals(Integer.valueOf(456), patientService.updatedAppointmentId);
    }

    private static class FakePatientService implements PatientService {
        private Integer updatedPatientId;
        private Integer updatedAppointmentId;

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            Patient patient = new Patient();
            patient.setId(123);
            return patient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            updatedPatientId = patient.getId();
            updatedAppointmentId = patient.getAppointmentid();
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
        public String seek(Patient patient) {
            return null;
        }

        @Override
        public Map<String, List> serrchInfo(String name, String type) {
            return Collections.emptyMap();
        }
    }

    private static class FakeAppointmentService implements AppointmentService {
        private Integer insertedPatientId;

        @Override
        public String addAppointment(Appointment appointment) {
            insertedPatientId = appointment.getPatientid();
            return "添加成功";
        }

        @Override
        public Integer selectTheLastAppointment(Integer patientId) {
            assertEquals(Integer.valueOf(123), patientId);
            return 456;
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
