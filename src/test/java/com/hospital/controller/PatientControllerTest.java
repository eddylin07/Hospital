package com.hospital.controller;

import com.alibaba.fastjson.JSONObject;
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
    public void appointmentUsesSessionPatientInsteadOfRequestBodyPatientId() {
        PatientController controller=new PatientController();
        FakeAppointmentService appointmentService=new FakeAppointmentService();
        FakePatientService patientService=new FakePatientService(7);
        controller.appointmentService=appointmentService;
        controller.patientService=patientService;
        MockHttpSession session=new MockHttpSession();
        Login login=new Login();
        login.setId(11);
        login.setRole(3);
        session.setAttribute("login",login);
        Appointment appointment=new Appointment();
        appointment.setPatientid(999);

        JSONObject json=controller.appointment(appointment,session);

        assertEquals("预约成功",json.get("message"));
        assertEquals(Integer.valueOf(7),appointmentService.addedAppointment.getPatientid());
        assertEquals(Integer.valueOf(7),appointmentService.lastAppointmentPatientId);
        assertEquals(Integer.valueOf(7),patientService.updatedPatient.getId());
        assertEquals(Integer.valueOf(123),patientService.updatedPatient.getAppointmentid());
    }

    private static class FakeAppointmentService implements AppointmentService {
        private Appointment addedAppointment;
        private Integer lastAppointmentPatientId;

        @Override
        public String addAppointment(Appointment appointment) {
            addedAppointment=appointment;
            return "预约成功";
        }

        @Override
        public Integer selectTheLastAppointment(Integer patientId) {
            lastAppointmentPatientId=patientId;
            return 123;
        }

        @Override public List<Appointment> getAllAppointments() { throw new UnsupportedOperationException(); }
        @Override public List<Appointment> getAllAppointments(String doctorname,String patientname) { throw new UnsupportedOperationException(); }
        @Override public String delAppointment(Integer id) { throw new UnsupportedOperationException(); }
        @Override public Appointment getAppointment(Integer id) { throw new UnsupportedOperationException(); }
        @Override public String UpdateAppointment(Appointment appointment) { throw new UnsupportedOperationException(); }
        @Override public List<Appointment> getPatientMessage(Integer patientId) { throw new UnsupportedOperationException(); }
        @Override public List<Appointment> selectByDoctorId(Integer doctorId,String patientname,String time) { throw new UnsupportedOperationException(); }
    }

    private static class FakePatientService implements PatientService {
        private final Patient currentPatient;
        private Patient updatedPatient;

        private FakePatientService(Integer patientId) {
            currentPatient=new Patient();
            currentPatient.setId(patientId);
        }

        @Override
        public Patient findPatientByLoginId(Integer loginid) {
            return currentPatient;
        }

        @Override
        public String updateAppointMent(Patient patient) {
            updatedPatient=patient;
            return "更新成功";
        }

        @Override public List<Patient> getAllPatients(String name,String certId) { throw new UnsupportedOperationException(); }
        @Override public List<Patient> getAllPatients() { throw new UnsupportedOperationException(); }
        @Override public String delPatient(Integer id) { throw new UnsupportedOperationException(); }
        @Override public Patient getPatient(Integer id) { throw new UnsupportedOperationException(); }
        @Override public String updatePatient(Patient patient) { throw new UnsupportedOperationException(); }
        @Override public String addPatient(Patient patient) { throw new UnsupportedOperationException(); }
        @Override public String seek(Patient patient) { throw new UnsupportedOperationException(); }
        @Override public Map<String,List> serrchInfo(String name,String type) { throw new UnsupportedOperationException(); }
    }
}
