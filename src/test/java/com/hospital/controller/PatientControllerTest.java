package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
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
    public void appointmentUsesPatientFromSessionInsteadOfRequestBody() {
        PatientController controller=new PatientController();
        FakePatientService patientService=new FakePatientService();
        FakeAppointmentService appointmentService=new FakeAppointmentService();
        controller.patientService=patientService;
        controller.appointmentService=appointmentService;

        MockHttpSession session=new MockHttpSession();
        Login login=new Login();
        login.setId(42);
        login.setRole(3);
        session.setAttribute("login",login);

        Appointment appointment=new Appointment();
        appointment.setPatientid(999);
        appointment.setDoctorid(3);

        JSONObject response=controller.appointment(appointment,session);

        assertEquals("更新成功",response.getString("message"));
        assertEquals(Integer.valueOf(5),appointmentService.addedAppointment.getPatientid());
        assertEquals(Integer.valueOf(5),patientService.updatedPatient.getId());
        assertEquals(Integer.valueOf(77),patientService.updatedPatient.getAppointmentid());
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
            Patient patient=new Patient();
            patient.setId(5);
            return patient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            updatedPatient=patient;
            return "更新成功";
        }

        @Override
        public Map<String, List> serrchInfo(String name, String type) {
            return null;
        }
    }

    private static class FakeAppointmentService implements AppointmentService {
        private Appointment addedAppointment;

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
            addedAppointment=appointment;
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
            return null;
        }

        @Override
        public boolean hasAppointmentWithDoctor(Integer doctorId, Integer patientId) {
            return false;
        }
    }
}
