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
    public void appointmentAlwaysUsesPatientFromSession() {
        PatientController controller = new PatientController();
        FakePatientService patientService = new FakePatientService();
        FakeAppointmentService appointmentService = new FakeAppointmentService();
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;

        Login login = new Login();
        login.setId(10);
        login.setRole(3);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login);

        Appointment requestAppointment = new Appointment();
        requestAppointment.setPatientid(999);
        requestAppointment.setDoctorid(4);

        JSONObject response = controller.appointment(requestAppointment, session);

        assertEquals(CommonService.add_message_success, response.get("message"));
        assertEquals(Integer.valueOf(7), appointmentService.insertedAppointment.getPatientid());
        assertEquals(Integer.valueOf(123), patientService.updatedPatient.getAppointmentid());
        assertEquals(Integer.valueOf(7), patientService.updatedPatient.getId());
    }

    private static class FakePatientService implements PatientService {
        private Patient updatedPatient;

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
            patient.setId(7);
            return patient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            this.updatedPatient = patient;
            return CommonService.upd_message_success;
        }

        @Override
        public Map<String, List> serrchInfo(String name, String type) {
            return null;
        }
    }

    private static class FakeAppointmentService implements AppointmentService {
        private Appointment insertedAppointment;

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
            this.insertedAppointment = appointment;
            appointment.setId(123);
            return CommonService.add_message_success;
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
            return 122;
        }
    }
}
