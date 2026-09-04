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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DoctorControllerTest {
    @Test
    public void drugRejectsPatientWithoutAppointmentBeforeDispensing() {
        DoctorController controller=new DoctorController();
        FakePatientService patientService=new FakePatientService();
        controller.doctorService=new FakeDoctorService();
        controller.appointmentService=new FakeAppointmentService(false);
        controller.patientService=patientService;

        MockHttpSession session=new MockHttpSession();
        Login login=new Login();
        login.setId(12);
        login.setRole(2);
        session.setAttribute("login",login);

        Map<String,String> body=new HashMap<>();
        body.put("patientid","88");
        body.put("1_number","1");

        JSONObject response=controller.drug(body,session);

        assertEquals("无权操作该患者",response.getString("message"));
        assertEquals(0,patientService.seekCount);
    }

    private static class FakeDoctorService implements DoctorService {
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
        public Doctor getDoctorByLoginId(Integer loginid) {
            Doctor doctor=new Doctor();
            doctor.setId(3);
            doctor.setLoginid(loginid);
            return doctor;
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
        private final boolean allowed;

        private FakeAppointmentService(boolean allowed) {
            this.allowed=allowed;
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
        public List<Appointment> selectByDoctorId(Integer doctorId, String patientname, String time) {
            return null;
        }

        @Override
        public Integer selectTheLastAppointment(Integer patientId) {
            return null;
        }

        @Override
        public boolean hasAppointmentWithDoctor(Integer doctorId, Integer patientId) {
            return allowed;
        }
    }

    private static class FakePatientService implements PatientService {
        private int seekCount;

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
            seekCount++;
            return "更新成功";
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
