package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DoctorControllerTest {
    @Test
    public void drugRejectsPatientsNotAssignedToCurrentDoctor() {
        DoctorController controller = new DoctorController();
        FakePatientService patientService = new FakePatientService();
        controller.patientService = patientService;
        controller.doctorService = new FakeDoctorService();
        controller.appointmentService = new FakeAppointmentService();

        Login login = new Login();
        login.setId(20);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("login", login);

        JSONObject response = controller.drug(map("patientid", "8", "1_number", "1"), session);

        assertEquals("无权限操作该患者", response.get("message"));
        assertEquals(0, patientService.seekCount);
    }

    private Map map(Object... values) {
        java.util.Map<Object, Object> map = new java.util.HashMap<>();
        for (int i = 0; i < values.length; i += 2) {
            map.put(values[i], values[i + 1]);
        }
        return map;
    }

    private static class FakeDoctorService implements DoctorService {
        @Override
        public Doctor getDoctorByLoginId(Integer loginid) {
            Doctor doctor = new Doctor();
            doctor.setId(2);
            return doctor;
        }

        @Override
        public List<Doctor> getAllDoctor() {
            return null;
        }

        @Override
        public List<Doctor> getAllDoctor(String name, String certId) {
            return null;
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
        public List<Doctor> getDoctorByDepartment(String department) {
            return null;
        }

        @Override
        public String seekInfo(Map map) {
            return null;
        }
    }

    private static class FakeAppointmentService implements AppointmentService {
        @Override
        public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) {
            Appointment appointment = new Appointment();
            appointment.setPatientid(7);
            return Arrays.asList(appointment);
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
        public String addAppointment(Appointment appointment) {
            return null;
        }

        @Override
        public List<Appointment> getPatientMessage(Integer patientId) {
            return null;
        }

        @Override
        public Integer selectTheLastAppointment(Integer patientId) {
            return null;
        }
    }

    private static class FakePatientService implements PatientService {
        private int seekCount;

        @Override
        public String seek(Patient patient) {
            seekCount++;
            return null;
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
        public Patient findPatientByLoginId(Integer loginid) {
            return null;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            return null;
        }

        @Override
        public Map<String, List> serrchInfo(String name, String type) {
            return null;
        }
    }
}
