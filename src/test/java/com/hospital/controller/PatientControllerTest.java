package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Appointment;
import com.hospital.entity.Patient;
import com.hospital.entity.Login;
import com.hospital.service.AppointmentService;
import com.hospital.service.HospitalizationService;
import com.hospital.service.MedicalhistoryService;
import com.hospital.service.PatientService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;
import java.util.Map;

public class PatientControllerTest {
    @Test
    public void appointmentUsesPatientFromSessionInsteadOfRequestBody() {
        PatientController controller = new PatientController();
        RecordingPatientService patientService = new RecordingPatientService();
        RecordingAppointmentService appointmentService = new RecordingAppointmentService();
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;

        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(42);
        login.setRole(3);
        session.setAttribute("login", login);

        Appointment appointment = new Appointment();
        appointment.setPatientid(999);
        appointment.setDoctorid(5);

        JSONObject response = controller.appointment(appointment, session);

        Assert.assertEquals("添加成功", response.getString("message"));
        Assert.assertEquals(Integer.valueOf(7), appointmentService.insertedAppointment.getPatientid());
        Assert.assertEquals(Integer.valueOf(7), patientService.updatedAppointmentPatient.getId());
        Assert.assertEquals(Integer.valueOf(123), patientService.updatedAppointmentPatient.getAppointmentid());
    }

    private static class RecordingPatientService implements PatientService {
        private Patient updatedAppointmentPatient;

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            Patient patient = new Patient();
            patient.setId(7);
            patient.setLoginid(loginid);
            return patient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            this.updatedAppointmentPatient = patient;
            return "更新成功";
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

    private static class RecordingAppointmentService implements AppointmentService {
        private Appointment insertedAppointment;

        @Override
        public String addAppointment(Appointment appointment) {
            this.insertedAppointment = appointment;
            appointment.setId(123);
            return "添加成功";
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

        @Override
        public Integer selectTheLastAppointment(Integer patientId) {
            return null;
        }
    }
}
