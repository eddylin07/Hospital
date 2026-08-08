package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.hospital.common.CommonService;
import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.entity.Login;
import com.hospital.entity.Patient;
import com.hospital.entity.Seek;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import com.hospital.service.SeekService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DoctorControllerTest {

    @Test
    public void drugRejectsPatientsOutsideCurrentDoctorsAppointments() {
        DoctorController controller = new DoctorController();
        FakePatientService patientService = new FakePatientService();
        controller.doctorService = new FakeDoctorService(10);
        controller.appointmentService = new FakeAppointmentService(3);
        controller.patientService = patientService;
        Map<String,String> request = new HashMap<>();
        request.put("patientid", "99");

        JSONObject response = controller.drug(request, doctorSession());

        assertEquals(CommonService.upd_message_error, response.get("message"));
        assertEquals(0, patientService.seekCalls);
    }

    @Test
    public void printSeekWithoutSeekReturnsBusinessError() {
        DoctorController controller = new DoctorController();
        controller.doctorService = new FakeDoctorService(10);
        controller.appointmentService = new FakeAppointmentService(3);
        controller.patientService = new FakePatientService();
        controller.seekService = new FakeSeekService(null);

        JSONObject response = controller.printseek(3, doctorSession());

        assertEquals("暂无数据，生成失败", response.get("message"));
    }

    private static MockHttpSession doctorSession() {
        MockHttpSession session = new MockHttpSession();
        Login login = new Login();
        login.setId(2);
        login.setRole(2);
        session.setAttribute("login", login);
        return session;
    }

    private static class FakeDoctorService implements DoctorService {
        private final Integer doctorId;

        private FakeDoctorService(Integer doctorId) {
            this.doctorId = doctorId;
        }

        @Override
        public Doctor getDoctorByLoginId(Integer loginid) {
            Doctor doctor = new Doctor();
            doctor.setId(doctorId);
            doctor.setName("doctor");
            return doctor;
        }

        @Override public List<Doctor> getAllDoctor() { throw new UnsupportedOperationException(); }
        @Override public List<Doctor> getAllDoctor(String name, String certId) { throw new UnsupportedOperationException(); }
        @Override public String delDoctor(Integer id) { throw new UnsupportedOperationException(); }
        @Override public String addDoctor(Doctor doctor) { throw new UnsupportedOperationException(); }
        @Override public Doctor getDoctor(Integer id) { throw new UnsupportedOperationException(); }
        @Override public String upDoctor(Doctor doctor) { throw new UnsupportedOperationException(); }
        @Override public List<Doctor> getDoctorByDepartment(String department) { throw new UnsupportedOperationException(); }
        @Override public String seekInfo(Map map) { throw new UnsupportedOperationException(); }
    }

    private static class FakeAppointmentService implements AppointmentService {
        private final Integer patientId;

        private FakeAppointmentService(Integer patientId) {
            this.patientId = patientId;
        }

        @Override
        public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) {
            Appointment appointment = new Appointment();
            appointment.setPatientid(patientId);
            List<Appointment> appointments = new ArrayList<>();
            appointments.add(appointment);
            return appointments;
        }

        @Override public List<Appointment> getAllAppointments() { throw new UnsupportedOperationException(); }
        @Override public List<Appointment> getAllAppointments(String doctorname, String patientname) { throw new UnsupportedOperationException(); }
        @Override public String delAppointment(Integer id) { throw new UnsupportedOperationException(); }
        @Override public Appointment getAppointment(Integer id) { throw new UnsupportedOperationException(); }
        @Override public String UpdateAppointment(Appointment appointment) { throw new UnsupportedOperationException(); }
        @Override public String addAppointment(Appointment appointment) { throw new UnsupportedOperationException(); }
        @Override public List<Appointment> getPatientMessage(Integer patientId) { throw new UnsupportedOperationException(); }
        @Override public Integer selectTheLastAppointment(Integer patientId) { throw new UnsupportedOperationException(); }
    }

    private static class FakePatientService implements PatientService {
        private int seekCalls;

        @Override
        public String seek(Patient patient) {
            seekCalls++;
            return CommonService.upd_message_success;
        }

        @Override
        public Patient getPatient(Integer id) {
            Patient patient = new Patient();
            patient.setId(id);
            patient.setName("patient");
            return patient;
        }

        @Override public List<Patient> getAllPatients(String name, String certId) { throw new UnsupportedOperationException(); }
        @Override public List<Patient> getAllPatients() { throw new UnsupportedOperationException(); }
        @Override public String delPatient(Integer id) { throw new UnsupportedOperationException(); }
        @Override public String updatePatient(Patient patient) { throw new UnsupportedOperationException(); }
        @Override public String addPatient(Patient patient) { throw new UnsupportedOperationException(); }
        @Override public Patient findPatientByLoginId(Integer loginid) { throw new UnsupportedOperationException(); }
        @Override public String updateAppointMent(Patient patient) { throw new UnsupportedOperationException(); }
        @Override public Map<String, List> serrchInfo(String name, String type) { throw new UnsupportedOperationException(); }
    }

    private static class FakeSeekService implements SeekService {
        private final Seek seek;

        private FakeSeekService(Seek seek) {
            this.seek = seek;
        }

        @Override
        public Seek getSeekByPatientId(Integer patientid) {
            return seek;
        }

        @Override public String addSeek(Seek seek) { throw new UnsupportedOperationException(); }
    }
}
