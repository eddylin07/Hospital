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

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PatientControllerTest {

    @Test
    public void appointmentUsesSessionPatientInsteadOfRequestBodyPatient() {
        PatientController controller = new PatientController();
        FakePatientService patientService = new FakePatientService();
        FakeAppointmentService appointmentService = new FakeAppointmentService();
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(10);
        login.setRole(3);
        session.setAttribute("login", login);
        Appointment appointment = new Appointment();
        appointment.setPatientid(999);
        appointment.setDoctorid(5);

        JSONObject response = controller.appointment(appointment, session);

        assertEquals(CommonService.upd_message_success, response.get("message"));
        assertEquals(Integer.valueOf(123), appointmentService.capturedPatientId);
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
            return CommonService.upd_message_success;
        }

        @Override
        public List<Patient> getAllPatients(String name, String certId) { return null; }
        @Override
        public List<Patient> getAllPatients() { return null; }
        @Override
        public String delPatient(Integer id) { return null; }
        @Override
        public Patient getPatient(Integer id) { return null; }
        @Override
        public String updatePatient(Patient patient) { return null; }
        @Override
        public String addPatient(Patient patient) { return null; }
        @Override
        public String seek(Patient patient) { return null; }
        @Override
        public Map<String, List> serrchInfo(String name, String type) { return null; }
    }

    private static class FakeAppointmentService implements AppointmentService {
        private Integer capturedPatientId;

        @Override
        public String addAppointment(Appointment appointment) {
            capturedPatientId = appointment.getPatientid();
            appointment.setId(456);
            return CommonService.add_message_success;
        }

        @Override
        public List<Appointment> getAllAppointments() { return null; }
        @Override
        public List<Appointment> getAllAppointments(String doctorname, String patientname) { return null; }
        @Override
        public String delAppointment(Integer id) { return null; }
        @Override
        public Appointment getAppointment(Integer id) { return null; }
        @Override
        public String UpdateAppointment(Appointment appointment) { return null; }
        @Override
        public List<Appointment> getPatientMessage(Integer patientId) { return null; }
        @Override
        public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) { return null; }
        @Override
        public Integer selectTheLastAppointment(Integer patientId) { return null; }
        @Override
        public boolean hasDoctorPatientAppointment(Integer doctorId, Integer patientId) { return false; }
    }
}
