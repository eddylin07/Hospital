package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.common.CommonService;
import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Drugs;
import com.hospital.entity.Hospitalization;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.DrugsService;
import com.hospital.service.HospitalizationService;
import com.hospital.service.MedicalhistoryService;
import com.hospital.service.OptionService;
import com.hospital.service.PatientService;
import com.hospital.service.SeekService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DoctorControllerTest {

    @Test
    public void drugRejectsPatientsNotAssignedToCurrentDoctor() {
        DoctorController controller = controllerWithAssignedPatient(7);
        FakePatientService patientService = (FakePatientService) controller.patientService;
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("patientid", "99");
        requestBody.put("3_number", "1");

        JSONObject response = controller.drug(requestBody, doctorSession());

        assertEquals("无权操作该患者", response.getString("message"));
        assertEquals(0, patientService.seekCalls);
        assertNull(patientService.lastSeekPatient);
    }

    @Test
    public void drugAllowsPatientsAssignedToCurrentDoctor() {
        DoctorController controller = controllerWithAssignedPatient(7);
        FakePatientService patientService = (FakePatientService) controller.patientService;
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("patientid", "7");
        requestBody.put("3_number", "1");

        JSONObject response = controller.drug(requestBody, doctorSession());

        assertEquals(CommonService.upd_message_success, response.getString("message"));
        assertEquals(1, patientService.seekCalls);
        assertEquals(Integer.valueOf(7), patientService.lastSeekPatient.getId());
        assertEquals("3@1", patientService.lastSeekPatient.getDrugsids());
    }

    private DoctorController controllerWithAssignedPatient(Integer patientId) {
        DoctorController controller = new DoctorController();
        FakeDoctorService doctorService = new FakeDoctorService();
        FakeAppointmentService appointmentService = new FakeAppointmentService(patientId);
        controller.doctorService = doctorService;
        controller.appointmentService = appointmentService;
        controller.patientService = new FakePatientService();
        return controller;
    }

    private MockHttpSession doctorSession() {
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(21);
        login.setRole(2);
        session.setAttribute("login", login);
        return session;
    }

    private static class FakeDoctorService implements DoctorService {
        @Override
        public List<Doctor> getAllDoctor() {
            return Collections.emptyList();
        }

        @Override
        public List<Doctor> getAllDoctor(String name, String certId) {
            return Collections.emptyList();
        }

        @Override
        public String delDoctor(Integer id) {
            return "";
        }

        @Override
        public String addDoctor(Doctor doctor) {
            return "";
        }

        @Override
        public Doctor getDoctor(Integer id) {
            return null;
        }

        @Override
        public String upDoctor(Doctor doctor) {
            return "";
        }

        @Override
        public Doctor getDoctorByLoginId(Integer loginid) {
            Doctor doctor = new Doctor();
            doctor.setId(5);
            return doctor;
        }

        @Override
        public List<Doctor> getDoctorByDepartment(String department) {
            return Collections.emptyList();
        }

        @Override
        public String seekInfo(Map map) {
            return "";
        }
    }

    private static class FakeAppointmentService implements AppointmentService {
        private final Integer patientId;

        FakeAppointmentService(Integer patientId) {
            this.patientId = patientId;
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
        public String addAppointment(Appointment appointment) {
            return "";
        }

        @Override
        public List<Appointment> getPatientMessage(Integer patientId) {
            return Collections.emptyList();
        }

        @Override
        public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) {
            Appointment appointment = new Appointment();
            appointment.setPatientid(patientId);
            return Collections.singletonList(appointment);
        }

        @Override
        public Integer selectTheLastAppointment(Integer patientId) {
            return null;
        }
    }

    private static class FakePatientService implements PatientService {
        int seekCalls;
        Patient lastSeekPatient;

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
            seekCalls++;
            lastSeekPatient = patient;
            return CommonService.upd_message_success;
        }

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            return null;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            return "";
        }

        @Override
        public Map<String, List> serrchInfo(String name, String type) {
            return Collections.emptyMap();
        }
    }
}
