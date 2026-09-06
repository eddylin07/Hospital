package com.hospital.controller;

import com.hospital.common.CommonService;
import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DoctorControllerTest {

    @Test
    public void drugRejectsPatientNotAssignedToCurrentDoctor() {
        DoctorController controller = new DoctorController();
        FakePatientService patientService = new FakePatientService();
        controller.doctorService = new FakeDoctorService();
        controller.appointmentService = new FakeAppointmentService();
        controller.patientService = patientService;

        Login login = new Login();
        login.setId(3);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login);
        Map<String, String> body = new HashMap<>();
        body.put("patientid", "99");
        body.put("10_number", "1");

        assertEquals(CommonService.upd_message_error, controller.drug(body, session).get("message"));
        assertFalse(patientService.seekCalled);
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
            return null;
        }

        @Override
        public String addDoctor(Doctor doctor) {
            return null;
        }

        @Override
        public Doctor getDoctor(Integer id) {
            return null;
        }

        @Override
        public String upDoctor(Doctor doctor) {
            return null;
        }

        @Override
        public Doctor getDoctorByLoginId(Integer loginid) {
            Doctor doctor = new Doctor();
            doctor.setId(7);
            return doctor;
        }

        @Override
        public List<Doctor> getDoctorByDepartment(String department) {
            return Collections.emptyList();
        }

        @Override
        public String seekInfo(Map map) {
            return null;
        }
    }

    private static class FakeAppointmentService implements AppointmentService {
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
        public String addAppointment(Appointment appointment) {
            return null;
        }

        @Override
        public List<Appointment> getPatientMessage(Integer patientId) {
            return Collections.emptyList();
        }

        @Override
        public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) {
            Appointment appointment = new Appointment();
            appointment.setPatientid(42);
            return Collections.singletonList(appointment);
        }

        @Override
        public Integer selectTheLastAppointment(Integer patientId) {
            return null;
        }
    }

    private static class FakePatientService implements PatientService {
        private boolean seekCalled;

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
            seekCalled = true;
            return null;
        }

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            return null;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            return null;
        }

        @Override
        public Map<String, List> serrchInfo(String name, String type) {
            return Collections.emptyMap();
        }
    }
}
