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
    public void appointmentUsesPatientFromSessionInsteadOfRequestBody() {
        PatientController controller = new PatientController();
        CapturingPatientService patientService = new CapturingPatientService();
        CapturingAppointmentService appointmentService = new CapturingAppointmentService();
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;

        Login login = new Login();
        login.setId(30);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login);

        Appointment request = new Appointment();
        request.setPatientid(999);
        JSONObject result = controller.appointment(request, session);

        assertEquals("添加成功", result.getString("message"));
        assertEquals(Integer.valueOf(5), appointmentService.insertedPatientId);
        assertEquals(Integer.valueOf(5), patientService.updatedPatientId);
        assertEquals(Integer.valueOf(77), patientService.updatedAppointmentId);
    }

    private static class CapturingPatientService implements PatientService {
        Integer updatedPatientId;
        Integer updatedAppointmentId;

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
        public Patient findPatientByLoginId(Integer loginid) {
            Patient patient = new Patient();
            patient.setId(5);
            return patient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            updatedPatientId = patient.getId();
            updatedAppointmentId = patient.getAppointmentid();
            return "更新成功";
        }

        @Override
        public Map<String, List> serrchInfo(String name, String type) {
            return null;
        }
    }

    private static class CapturingAppointmentService implements AppointmentService {
        Integer insertedPatientId;

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
        public String addAppointment(Appointment appointment) {
            insertedPatientId = appointment.getPatientid();
            appointment.setId(77);
            return "添加成功";
        }

        @Override
        public List<Appointment> getPatientMessage(Integer patientId) {
            return null;
        }

        @Override
        public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) {
            return null;
        }

        @Override
        public Integer selectTheLastAppointment(Integer patientId) {
            return 1;
        }
    }
}
