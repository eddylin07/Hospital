package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.common.CommonService;
import com.hospital.entity.Appointment;
import com.hospital.entity.Patient;
import com.hospital.entity.Login;
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
    public void appointmentUsesSessionPatientInsteadOfRequestBodyPatientId() {
        PatientController controller = new PatientController();
        FakePatientService patientService = new FakePatientService();
        FakeAppointmentService appointmentService = new FakeAppointmentService();
        Patient sessionPatient = new Patient();
        sessionPatient.setId(7);
        patientService.sessionPatient = sessionPatient;
        appointmentService.latestAppointmentId = 55;
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;

        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(12);
        login.setRole(3);
        session.setAttribute("login", login);
        Appointment appointment = new Appointment();
        appointment.setPatientid(999);
        appointment.setDoctorid(4);

        JSONObject response = controller.appointment(appointment, session);

        assertEquals(CommonService.add_message_success, response.getString("message"));
        assertEquals(Integer.valueOf(7), appointmentService.addedAppointment.getPatientid());
        assertEquals(Integer.valueOf(7), appointmentService.selectedPatientId);
        assertEquals(Integer.valueOf(7), patientService.updatedAppointmentPatient.getId());
        assertEquals(Integer.valueOf(55), patientService.updatedAppointmentPatient.getAppointmentid());
    }

    @Test
    public void downloadPdfReturnsControlledMessageWhenPatientHasNoAppointment() {
        PatientController controller = new PatientController();
        FakePatientService patientService = new FakePatientService();
        FakeAppointmentService appointmentService = new FakeAppointmentService();
        Patient sessionPatient = new Patient();
        sessionPatient.setId(7);
        patientService.sessionPatient = sessionPatient;
        controller.patientService = patientService;
        controller.appointmentService = appointmentService;

        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(12);
        login.setRole(3);
        session.setAttribute("login", login);

        JSONObject response = controller.downloadpdf(session);

        assertEquals("暂无预约单", response.getString("message"));
    }

    private static class FakePatientService implements PatientService {
        Patient sessionPatient;
        Patient updatedAppointmentPatient;

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
            return sessionPatient;
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
        Appointment addedAppointment;
        Integer selectedPatientId;
        Integer latestAppointmentId;

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
            addedAppointment = appointment;
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
            selectedPatientId = patientId;
            return latestAppointmentId;
        }
    }
}
